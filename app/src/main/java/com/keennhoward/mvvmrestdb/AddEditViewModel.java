package com.keennhoward.mvvmrestdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.keennhoward.mvvmrestdb.repository.AddEditRepository;
import com.keennhoward.mvvmrestdb.room.User;

public class AddEditViewModel extends AndroidViewModel {

    private AddEditRepository repository;


    public AddEditViewModel(@NonNull Application application) {
        super(application);
        repository = new AddEditRepository(application);
    }

    public void insert(User user){repository.insert(user);}
    public void update(User user){repository.update(user);}
}
