package com.keennhoward.mvvmrestdb.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keennhoward.mvvmrestdb.R;
import com.keennhoward.mvvmrestdb.SavedUsersAdapter;
import com.keennhoward.mvvmrestdb.SavedUsersViewModel;
import com.keennhoward.mvvmrestdb.UserViewModel;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.List;


public class SavedUsersFragment extends Fragment {

    private SavedUsersViewModel savedUsersViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_users, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.saved_users_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setHasFixedSize(true);

        final SavedUsersAdapter savedUsersAdapter = new SavedUsersAdapter(v.getContext());
        recyclerView.setAdapter(savedUsersAdapter);

        savedUsersViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(SavedUsersViewModel.class);

        savedUsersViewModel.getAllUsers().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                savedUsersAdapter.submitList(users);
            }
        });

        return v;
    }
}
