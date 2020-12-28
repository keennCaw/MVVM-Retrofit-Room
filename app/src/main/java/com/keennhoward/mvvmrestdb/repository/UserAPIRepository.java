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

public class UserAPIRepository {

    //API
    private MutableLiveData<List<Data>> dataList;

    private MutableLiveData<String> message;


    //TEST
    private MutableLiveData<User> searchedUser;


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

    public MutableLiveData<User> getSearchedUser() {
        return searchedUser;
    }

    //TEST
    public LiveData<String> getMessage(){
        return message;
    }


    public void insert(User user){
        new InsertUserDBAsyncTask(userDao).execute(user);
    }

    public void update(User user){
        new UpdateUserDBAsyncTask(userDao).execute(user);
    }

    public void insertApiUser(User user){
        new InsertIfNotExistsDBUser(userDao).execute(user);
    }

    public void searchUserDB(User user){
        new UserAPIRepository.SearchUserDBAsyncTask(userDao).execute(user);
    }

    public void reInitRepositoryVariables(){
        message.setValue("null");
        searchedUser.setValue(null);
    }

    //constructor
    public UserAPIRepository(Application application){
        //DB
        UserDatabase database = UserDatabase.getInstance(application);
        userDao = database.userDao();

        dataList = new MutableLiveData<>();

        //Test
        message = new MutableLiveData<>("null");

        searchedUser = new MutableLiveData<>();

        app = application;

    }

    //Test
    private class SearchUserDBAsyncTask extends AsyncTask<User,Void,Boolean>{
        private UserDao userDao;
        //private User resultUser;
        private User inputUser;

        public SearchUserDBAsyncTask(UserDao userDao) {
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
            if(aBoolean){
                searchedUser.setValue(inputUser);
            }else{
                searchedUser.setValue(null);
            }
        }
    }


    private class InsertIfNotExistsDBUser extends AsyncTask<User,Void,Boolean>{
        private UserDao userDao;
        //private User resultUser;
        private User inputUser;

        public InsertIfNotExistsDBUser(UserDao userDao) {
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
                //update(inputUser);
                //Test
                //message.setValue("Updated User: " + inputUser.getId());
            }else{
                insert(inputUser);
                //Test
                message.setValue("Saved User: " + inputUser.getId());
            }
        }
    }

    private class InsertUserDBAsyncTask extends AsyncTask<User,Void,Void> {
        private UserDao userDao;

        public InsertUserDBAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            userDao.insert(users[0]);

            message.postValue("Saved User: " + users[0].getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class UpdateUserDBAsyncTask extends AsyncTask<User,Void,Void> {
        private UserDao userDao;
        private User user;

        public UpdateUserDBAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            user = users[0];
            userDao.update(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            message.setValue("Updated User: " + user.getId());
        }
    }
}
