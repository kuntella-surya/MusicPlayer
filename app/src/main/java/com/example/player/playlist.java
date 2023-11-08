package com.example.player;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class playlist extends Fragment {
    Database database;
   RecyclerView recyclerView;
    String[] items;

    public playlist() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);
       recyclerView = v.findViewById(R.id.rvview);
        database = new Database(requireActivity());
        displaysongs();

        return v;
    }
    public void displaysongs() {
        final ArrayList<File> mysongs = findsng(Environment.getExternalStorageDirectory());
        ArrayList<Integer> index = database.readAllData();
        items = new String[mysongs.size()];
        for (int i = 0; i < mysongs.size(); i++) {
            items[i] = mysongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        ArrayList<File> newsongs = new ArrayList<>();
        for (int i : index) {
            File songToCopy = mysongs.get(i);
            newsongs.add(songToCopy);
        }
         customadaptr rvadapter = new customadaptr(requireActivity(), items, newsongs,index);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvadapter);
    }
    public void hello(){

    }

    public ArrayList<File> findsng(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File singlefile : files) {
                if (singlefile.isDirectory() && !singlefile.isHidden()) {
                    arrayList.addAll(findsng(singlefile));
                } else {
                    if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {
                        arrayList.add(singlefile);
                    }
                }
            }
        }
        return arrayList;
    }
}