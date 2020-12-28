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
        message = repository.getMessage();


        searchedUser = repository.getSearchedUser();

    }

    //Test
    private LiveData<String> message;
    public LiveData<String> getMessage(){
        return message;
    }

    private LiveData<User> searchedUser;

    public LiveData<User> getSearchedUser(){return searchedUser;}




    //API repository

    private LiveData<List<Data>> userResponseLiveData;

    public void init(){
        //userResponseLiveData = repository.getDataResponseLiveData();
        repository.getUsersApiCall();

        repository.reInitRepositoryVariables();
    }

    public void insertApiUser(User user){ repository.insertApiUser(user);}

    public void insertUserDB(User user){ repository.insert(user);}

    public void deleteUser(int id){
        repository.deleteUserApiCall(id);
    }

    public void searchUser(User user){ repository.searchUserDB(user);}

    public void updateUserDB(User user){repository.update(user);}

    public LiveData<List<Data>> getUserResponseLiveData(){
        return userResponseLiveData;
    }

}
