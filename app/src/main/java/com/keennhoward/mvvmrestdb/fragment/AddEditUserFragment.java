package com.keennhoward.mvvmrestdb.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.keennhoward.mvvmrestdb.AddEditViewModel;
import com.keennhoward.mvvmrestdb.R;
import com.keennhoward.mvvmrestdb.SavedUsersViewModel;
import com.keennhoward.mvvmrestdb.room.User;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class AddEditUserFragment extends Fragment {

    private static final int SELECT_PICTURE = 1;

    private String selectedImage;

    private AddEditViewModel viewModel;

    private EditText idEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private ImageView avatarImageView;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Add/Edit User");

        int savedID = AddEditUserFragmentArgs.fromBundle(getArguments()).getId();
        String fName = AddEditUserFragmentArgs.fromBundle(getArguments()).getFirstName();
        String lName = AddEditUserFragmentArgs.fromBundle(getArguments()).getLastName();
        String email = AddEditUserFragmentArgs.fromBundle(getArguments()).getEmail();


        Log.d("ID", String.valueOf(savedID)+ fName);

        View v = inflater.inflate(R.layout.fragment_add_edit_user, container, false);

        idEditText = v.findViewById(R.id.id_edit_text);
        firstNameEditText = v.findViewById(R.id.first_name_edit_text);
        lastNameEditText = v.findViewById(R.id.last_name_edit_text);
        emailEditText = v.findViewById(R.id.email_edit_text);
        avatarImageView = v.findViewById(R.id.add_avatar_image_view);
        saveButton = v.findViewById(R.id.save_user_button);

        if(savedID != 0){
            selectedImage = AddEditUserFragmentArgs.fromBundle(getArguments()).getAvatar();
            idEditText.setText(String.valueOf(savedID));
            idEditText.setEnabled(false);
            idEditText.setVisibility(View.VISIBLE);
            firstNameEditText.setText(fName);
            lastNameEditText.setText(lName);
            emailEditText.setText(email);

            Glide.with(getContext())
                    .load(selectedImage)
                    .transform(new CircleCrop())
                    .into(avatarImageView);
        }else{
            idEditText.setVisibility(View.GONE);
        }

        viewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(AddEditViewModel.class);

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(savedID == 0) {
                    saveUser();
                    Navigation.findNavController(v).navigate(R.id.navigateAddEditToSaved);
                }else {
                    updateUser(savedID);
                    Navigation.findNavController(v).navigate(R.id.navigateAddEditToSaved);
                }
            }
        });

        return v;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                Log.d("uri" , selectedImageUri.toString());
                selectedImage = selectedImageUri.toString();

                //Uri uri = Uri.parse("content://com.android.providers.media.documents/document/image%3A26");

                Glide.with(getContext())
                        .load(selectedImageUri)
                        .transform(new CircleCrop())
                        .into(avatarImageView);
            }
        }
    }

    private void saveUser(){
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if(firstName.trim().isEmpty() || lastName.trim().isEmpty() || email.trim().isEmpty()){
            Toast.makeText(getContext(), "Please Complete Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(email,firstName,lastName,selectedImage);

        Log.d("user", user.toString());

        viewModel.insert(user);

        Toast.makeText(getActivity(), "User Saved", Toast.LENGTH_SHORT).show();
    }

    private void updateUser(int id){
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if(firstName.trim().isEmpty() || lastName.trim().isEmpty() || email.trim().isEmpty()){
            Toast.makeText(getContext(), "Please Complete Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(email,firstName,lastName,selectedImage);
        user.setId(id);

        Log.d("user", user.toString());

        viewModel.update(user);

        Toast.makeText(getActivity(), "User Updated", Toast.LENGTH_SHORT).show();


    }
}
