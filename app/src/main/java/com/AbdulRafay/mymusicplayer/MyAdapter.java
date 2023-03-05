package com.AbdulRafay.mymusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final String[] songsItem;
    OnItemClickedListener onItemClickedListener;

    public MyAdapter(String[] songsItem, OnItemClickedListener onItemClickedListener) {
        this.songsItem = songsItem;
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_adapter_items_layout, parent, false);
        return new ViewHolder(view, onItemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(songsItem[position]);
    }

    @Override
    public int getItemCount() {
        return songsItem.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textView;
        private OnItemClickedListener onItemClickedListener;

        public ViewHolder(@NonNull View itemView, OnItemClickedListener onItemClickedListener) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.textViewSongName);
            this.onItemClickedListener = onItemClickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemClickedListener {
        public void onItemClicked(int position);
    }
}
