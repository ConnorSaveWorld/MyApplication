package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<List<Item>> itemList;

    public ItemAdapter(List<List<Item>> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<Item> items = itemList.get(position);
        holder.listIdTextView.setText("ListId: " + items.get(0).getListId());

        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            if (item.getName() != null && !item.getName().isEmpty()) {
                sb.append(item.getName()).append(", ");
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space
            holder.nameTextView.setText("Name: " + sb.toString());
        } else {
            holder.nameTextView.setText("Name: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listIdTextView;
        public TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            listIdTextView = view.findViewById(R.id.listIdTextView);
            nameTextView = view.findViewById(R.id.nameTextView);
        }
    }
}
