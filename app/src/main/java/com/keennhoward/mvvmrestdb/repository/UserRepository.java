package com.keennhoward.mvvmrestdb.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.keennhoward.mvvmrestdb.model.Data;
import com.keennhoward.mvvmrestdb.model.Users;
import com.keennhoward.mvvmrestdb.room.User;
import com.keennhoward.mvvmrestdb.room.UserDao;
import com.keennhoward.mvvmrestdb.room.UserDatabase;
import com.keennhoward.mvvmrestdb.service.APIService;
import com.keennhoward.mvvmrestdb.service.RetroInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRepository {

    //API
    private MutableLiveData<List<Data>> dataList;

    public void makeApiCall(){
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<Users> call = apiService.getUsers();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Users users = response.body();
                Log.d("Api", users.getData().toString());

                dataList.postValue(users.getData());
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                dataList.postValue(null);
                Log.d("error", t.getMessage());
            }
        });
    }

    public LiveData<List<Data>> getDataResponseLiveData(){
        return dataList;
    }


    //constructor
    public UserRepository(Application application){
        dataList = new MutableLiveData<>();
        UserDatabase database = UserDatabase.getInstance(application);

        userDao = database.userDao();
        allUsers = userDao.getAllUsers();

    }


    //Database

    private UserDao userDao;

    private LiveData<List<User>> allUsers;

    public void insert(User user){
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }


    //AsyncTasks
    private static class InsertUserAsyncTask extends AsyncTask<User,Void, Void>{
        private UserDao userDao;

        public InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

}
