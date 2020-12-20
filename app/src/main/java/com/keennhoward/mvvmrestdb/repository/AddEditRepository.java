package com.keennhoward.mvvmrestdb.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.keennhoward.mvvmrestdb.room.User;
import com.keennhoward.mvvmrestdb.room.UserDao;
import com.keennhoward.mvvmrestdb.room.UserDatabase;

import java.util.List;

public class AddEditRepository {

    private UserDao userDao;

    public AddEditRepository(Application application){
        UserDatabase database = UserDatabase.getInstance(application);
        userDao = database.userDao();
    }

    public void insert(User user){
        new AddEditRepository.InsertUserAsyncTask(userDao).execute(user);
    }
    public void update(User user){
        new AddEditRepository.UpdateUserAsyncTask(userDao).execute(user);
    }

    private class InsertUserAsyncTask extends AsyncTask<User,Void,Void> {
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
    private class UpdateUserAsyncTask extends AsyncTask<User,Void,Void>{
        private UserDao userDao;

        public UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }
}
