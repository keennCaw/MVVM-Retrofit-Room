package com.keennhoward.mvvmrestdb.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class UserAPIRepository {

    //API
    private MutableLiveData<List<Data>> dataList;


    //TEST
    private MutableLiveData<String> message;



    private Application app;

    //DB
    private UserDao userDao;

    public void getUsersApiCall(){
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

    public void searchUser(int id){

    }

    public void deleteUserApiCall(int id){
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<Void> call = apiService.deleteUser(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                message.setValue("User Deleted Code:" + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                message.setValue("Check Network Connection");
            }
        });
    }

    public LiveData<List<Data>> getDataResponseLiveData(){
        return dataList;
    }

    //TEST
    public LiveData<String> getMessage(){
        return message;
    }


    public void insert(User user){
        new UserAPIRepository.InsertUserWithIdAsyncTask(userDao).execute(user);
    }

    public void update(User user){
        new UserAPIRepository.UpdateUserWithIdAsyncTask(userDao).execute(user);
    }

    public void insertApiUser(User user){
        new UserAPIRepository.InsertOrUpdateUser(userDao).execute(user);
    }

    public void reInitMessage(){
        message.setValue("null");
    }

    //constructor
    public UserAPIRepository(Application application){
        //DB
        UserDatabase database = UserDatabase.getInstance(application);
        userDao = database.userDao();

        dataList = new MutableLiveData<>();

        //Test
        message = new MutableLiveData<>("null");

        app = application;

    }

    private class InsertOrUpdateUser extends AsyncTask<User,Void,Boolean>{
        private UserDao userDao;
        //private User resultUser;
        private User inputUser;

        public InsertOrUpdateUser(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Boolean doInBackground(User... users) {
            inputUser = users[0];
            //resultUser = userDao.getUserWithId(users[0].getId());
            if(userDao.getUserWithId(inputUser.getId()) != null) {
                return true;
            }else {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                update(inputUser);
                //Test
                message.setValue("Updated User: " + inputUser.getId());
            }else{
                insert(inputUser);
                //Test
                message.setValue("Saved User: " + inputUser.getId());
            }
        }
    }

    private class InsertUserWithIdAsyncTask extends AsyncTask<User,Void,Void> {
        private UserDao userDao;

        public InsertUserWithIdAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            userDao.insert(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class UpdateUserWithIdAsyncTask extends AsyncTask<User,Void,Void> {
        private UserDao userDao;

        public UpdateUserWithIdAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            userDao.update(users[0]);
            return null;
        }
    }
}
