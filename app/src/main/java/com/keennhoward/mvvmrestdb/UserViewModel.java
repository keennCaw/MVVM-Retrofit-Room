package com.keennhoward.mvvmrestdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.keennhoward.mvvmrestdb.model.Data;
import com.keennhoward.mvvmrestdb.repository.UserAPIRepository;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserAPIRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserAPIRepository(application);
        userResponseLiveData = repository.getDataResponseLiveData();
    }

    //API repository

    private LiveData<List<Data>> userResponseLiveData;

    public void init(){
        //userResponseLiveData = repository.getDataResponseLiveData();
        repository.getUsersApiCall();
    }

    public void deleteUser(int id){
        repository.deleteUserApiCall(id);
    }

    public LiveData<List<Data>> getUserResponseLiveData(){
        return userResponseLiveData;
    }
}
