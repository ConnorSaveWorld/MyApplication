package com.example.myapplication;

import retrofit2.Call;


import java.util.List;

import retrofit2.http.GET;

public interface ItemService {
    @GET("hiring.json")
    Call<List<Item>> getItems();
}