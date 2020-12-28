package com.keennhoward.mvvmrestdb.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.keennhoward.mvvmrestdb.R;
import com.keennhoward.mvvmrestdb.UserResultsAdapter;
import com.keennhoward.mvvmrestdb.UserViewModel;
import com.keennhoward.mvvmrestdb.model.Data;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView usersRecyclerView;
    private List<Data> userList;
    private UserViewModel userViewModel;
    private TextView noResultTextView;

    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Users");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);

        UserResultsAdapter userResultsAdapter = new UserResultsAdapter(v.getContext(), userList);

        noResultTextView = v.findViewById(R.id.no_result_textView);

        usersRecyclerView = v.findViewById(R.id.user_recycleView);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        usersRecyclerView.setAdapter(userResultsAdapter);

        userViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(UserViewModel.class);

        userViewModel.getUserResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> data) {
                if(data != null){
                    userList = data;
                    userResultsAdapter.setUserList(userList);
                    noResultTextView.setVisibility(View.GONE);
                }else {
                    noResultTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        userViewModel.init();

        userViewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("Observed Value", s);
                if(s.equals("null")){ }
                else{
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();}
            }
        });

        userViewModel.getSearchedUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null){
                    Log.d("searched user", "null");
                }else{
                    Log.d("searched user", "user " + user.getId());
                    createUpdateDialog(user).show();
                }
            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                userViewModel.deleteUser(userResultsAdapter.getDataAt(viewHolder.getAdapterPosition()).getId());
                userResultsAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(usersRecyclerView);




        userResultsAdapter.setOnItemClickedListener(new UserResultsAdapter.onItemClicked() {
            @Override
            public void onItemClick(Data data) {
                Log.d("Data", data.toString());

                user = new User(data.getEmail(),data.getFirst_name(),data.getLast_name(),data.getAvatar());
                user.setId(data.getId());

                userViewModel.searchUser(user);
                userViewModel.insertApiUser(user);
            }
        });

        return v;
    }

    public Dialog createUpdateDialog(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("User Already Exists")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userViewModel.updateUserDB(user);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

}
