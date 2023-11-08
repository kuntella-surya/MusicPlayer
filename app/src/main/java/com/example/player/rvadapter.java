package com.example.player;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class rvadapter extends RecyclerView.Adapter<rvadapter.ViewHolder> {
    Context context;
    String[] items;
    View rv;
    ArrayList<File> mysongs;
    Database databaseHelper;

    public rvadapter(Context ctx, String[] string, ArrayList<File> songs) {
        this.context = ctx;
        this.items = string;
        this.mysongs = songs;
        databaseHelper = new Database(context);
    }

    @NonNull
    @Override
    public rvadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rv = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv, parent, false);
        return new ViewHolder(rv);
    }
    @Override
    public void onBindViewHolder(@NonNull rvadapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, play.class);
                String songname = items[position];
                intent.putExtra("songname", songname);
                intent.putExtra("songs", mysongs);
                intent.putExtra("pos", position);
                int storedres = databaseHelper.getCountByPosition(position);
                if(storedres == 0) {
                    databaseHelper.store(songname, position);
                }
                else {
                    databaseHelper.updateCount(position);
                }
                context.startActivity(intent);
            }
        });
        holder.txt.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public void updateData(String[] newItems, ArrayList<File> newfiles) {
        items = newItems;
        mysongs = newfiles;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.songname);
        }
    }
}
