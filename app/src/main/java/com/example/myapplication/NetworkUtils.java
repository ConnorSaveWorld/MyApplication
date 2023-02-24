package com.example.myapplication;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NetworkUtils {

    private static final String JSON_URL = "https://example.com/items.json";

    public static List<Item> fetchItems() {
        List<Item> itemList = new ArrayList<>();

        try {
            URL url = new URL(JSON_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();

                String jsonString = stringBuilder.toString();

                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    int listId = jsonObject.getInt("listId");
                    String name = jsonObject.optString("name");

                    if (!TextUtils.isEmpty(name)) {
                        Item item = new Item(id, listId, name);
                        itemList.add(item);
                    }
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemList;
    }

}

