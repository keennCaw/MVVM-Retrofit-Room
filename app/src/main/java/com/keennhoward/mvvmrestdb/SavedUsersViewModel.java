package com.keennhoward.mvvmrestdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.keennhoward.mvvmrestdb.repository.UserDBRepository;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.List;

public class SavedUsersViewModel extends AndroidViewModel {

    private UserDBRepository repository;

    private LiveData<List<User>> allUsers;

    public SavedUsersViewModel(@NonNull Application application) {
        super(application);

        repository = new UserDBRepository(application);
        allUsers = repository.getAllUsers();
    }

    public void delete(User user){
        repository.delete(user);
    }
    public void deleteAllUsers(User user){
        repository.deleteAllUsers();
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }
}
