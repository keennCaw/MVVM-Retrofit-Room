package com.keennhoward.mvvmrestdb.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keennhoward.mvvmrestdb.R;
import com.keennhoward.mvvmrestdb.UserResultsAdapter;
import com.keennhoward.mvvmrestdb.UserViewModel;
import com.keennhoward.mvvmrestdb.model.Data;

import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView usersRecyclerView;
    private List<Data> userList;
    private UserViewModel userViewModel;
    private TextView noResultTextView;

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

        userViewModel.getUserResponseLiveData().observe(getActivity(), new Observer<List<Data>>() {
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

        return v;
    }
}
