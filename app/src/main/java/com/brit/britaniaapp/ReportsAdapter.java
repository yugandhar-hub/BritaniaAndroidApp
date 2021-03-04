package com.brit.britaniaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {
    ArrayList<Report> list = new ArrayList<>();

    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.report_list_item, parent, false);
        ReportsAdapter.ViewHolder viewHolder = new ReportsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ViewHolder holder, int position) {
        if (list != null && list.size() > 0) {
            holder.checkBox.setText(list.get(position).getReportName());
            holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                list.get(position).isChecked = b;
            });
            ((TextView) holder.itemView.findViewById(R.id.reportNamecb)).setText(list.get(position).getReportName());
        }
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0)
            return list.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.reportNamecb);
        }
    }

    public void updateList(ArrayList<Report> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}

