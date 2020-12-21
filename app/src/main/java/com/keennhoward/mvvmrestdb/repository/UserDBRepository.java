package com.keennhoward.mvvmrestdb.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.keennhoward.mvvmrestdb.room.User;
import com.keennhoward.mvvmrestdb.room.UserDao;
import com.keennhoward.mvvmrestdb.room.UserDatabase;

import java.util.List;

public class UserDBRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserDBRepository(Application application){
        UserDatabase database = UserDatabase.getInstance(application);

        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
    }


    public void delete(User user){
        new DeleteUserAsyncTask(userDao).execute(user);
    }
    public void deleteAllUsers(){
        new DeleteAllUsersAsyncTask(userDao).execute();
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }


    private class DeleteUserAsyncTask extends AsyncTask<User,Void,Void>{
        private UserDao userDao;

        public DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }
    private class DeleteAllUsersAsyncTask extends AsyncTask<User,Void,Void>{
        private UserDao userDao;

        public DeleteAllUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteAllUsers();
            return null;
        }
    }
}
