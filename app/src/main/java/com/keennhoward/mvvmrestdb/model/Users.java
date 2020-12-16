package com.keennhoward.mvvmrestdb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Users {

    @SerializedName("page")
    private int page;

    @SerializedName("per_page")
    private int per_page;

    @SerializedName("total")
    private int total;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("data")
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }
}
