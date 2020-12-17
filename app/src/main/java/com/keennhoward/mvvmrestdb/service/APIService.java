package com.keennhoward.mvvmrestdb.service;

import com.keennhoward.mvvmrestdb.model.Users;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("api/users?page=2")
    Call<Users> getUsers();

    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);
}
