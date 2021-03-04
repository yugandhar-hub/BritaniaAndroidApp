package com.brit.britaniaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WebUrlRecyclerView extends RecyclerView.Adapter<WebUrlRecyclerView.ViewHolder> {
    ArrayList<Report> list = null;

    public WebUrlRecyclerView(ArrayList<Report> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list != null && list.size() > 0) {
            ((TextView) holder.itemView.findViewById(R.id.listItem_reportName)).setText(list.get(position).reportName);
            ((TextView) holder.itemView.findViewById(R.id.listItem_webUrl)).setText(list.get(position).webUrl);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0)
            return list.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
