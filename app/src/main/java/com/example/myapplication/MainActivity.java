package com.example.myapplication;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//
//private ItemAdapter adapter;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//    }
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ItemService itemService = retrofit.create(ItemService.class);

        Call<List<Item>> call = itemService.getItems();

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Item> itemList = response.body();

                // Filter out any items where "name" is blank or null.
                List<Item> filteredItemList = new ArrayList<>();
                for (Item item : itemList) {
                    if (item.getName() != null && !item.getName().isEmpty()) {
                        filteredItemList.add(item);
                    }
                }

                // Sort the items first by "listId" then by "name".
                Collections.sort(filteredItemList, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        int listId1 = item1.getListId();
                        int listId2 = item2.getListId();
                        String name1 = item1.getName();
                        String name2 = item2.getName();

                        if (listId1 != listId2) {
                            return Integer.compare(listId1, listId2);
                        } else {
                            return name1.compareTo(name2);
                        }
                    }
                });

                // Group the items by "listId".
                List<List<Item>> groupedItemList = new ArrayList<>();
                int maxListId = 0;
                for (Item item : filteredItemList) {
                    int listId = item.getListId();

                    // If a new listId is encountered, create a new list for it.
                    if (listId > maxListId) {
                        maxListId = listId;
                        groupedItemList.add(new ArrayList<Item>());
                    }

                    // Add the item to the appropriate list.
                    groupedItemList.get(listId - 1).add(item);
                }

                // Set up the RecyclerView with the grouped and sorted list of items.
                itemAdapter = new ItemAdapter(groupedItemList);
                recyclerView.setAdapter(itemAdapter);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}

