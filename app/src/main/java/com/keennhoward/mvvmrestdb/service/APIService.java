package com.keennhoward.mvvmrestdb.service;

import com.keennhoward.mvvmrestdb.model.Users;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("api/users?page=2")
    Call<Users> getUsers();
}
