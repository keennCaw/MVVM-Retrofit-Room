package com.keennhoward.mvvmrestdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.keennhoward.mvvmrestdb.model.Data;
import com.keennhoward.mvvmrestdb.repository.UserRepository;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
        userResponseLiveData = repository.getDataResponseLiveData();
    }

    //database repository methods

    public void Insert(User user){ repository.insert(user); }

    public LiveData<List<User>> getAllUsers(){ return allUsers; }

    //API repository

    private LiveData<List<Data>> userResponseLiveData;

    public void init(){
        //userResponseLiveData = repository.getDataResponseLiveData();
        repository.makeApiCall();
    }

    public LiveData<List<Data>> getUserResponseLiveData(){
        return userResponseLiveData;
    }
}
