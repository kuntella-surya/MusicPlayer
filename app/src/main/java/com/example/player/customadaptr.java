package com.example.player;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class customadaptr extends RecyclerView.Adapter<customadaptr.ViewHolder> {
    Context context;
    String[] items;
    ArrayList<File> mysongs;
    ArrayList<Integer> index;
    Database databaseHelper;
    View k;
    public customadaptr(Context ctx, String[] string, ArrayList<File> songs,ArrayList<Integer> i) {
        this.context = ctx;
        this.items = string;
        this.mysongs = songs;
        this.index = i;
        databaseHelper = new Database(context);
    }

    @NonNull
    @Override
    public customadaptr.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        k = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv, parent, false);
        return new customadaptr.ViewHolder(k);
    }

    @Override
    public void onBindViewHolder(@NonNull customadaptr.ViewHolder holder, int position) {
        int pos = index.get(position);

        // Clear the existing content of newsongs and add songs from mysongs based on the pos variable
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, play.class);
                intent.putExtra("songname", items[pos]);
                intent.putExtra("songs", mysongs);
                intent.putExtra("pos", position);
               databaseHelper.updateCount(pos);
                context.startActivity(intent);
            }
        });
        holder.txt.setText(items[pos]);
    }

    @Override
    public int getItemCount() {
        return index.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.songname);
        }
    }
}
