package com.example.player;

import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class allsongs extends Fragment {
    String[] items;
    RecyclerView recyclerView;
    String prerms;
    Database database;
    private boolean isPermissionGranted = false;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public allsongs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frag1 = inflater.inflate(R.layout.fragment_allsongs, container, false);
        recyclerView = frag1.findViewById(R.id.rvallsongs);
        prerms = Manifest.permission.READ_EXTERNAL_STORAGE;
        database = new Database(requireActivity());
        if (ContextCompat.checkSelfPermission(requireActivity(), prerms) == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true;
            displaysongs();
        } else {
            // Request permission from the user
            requestPermissions(new String[]{prerms}, PERMISSION_REQUEST_CODE);
        }

        // Inflate the layout for this fragment
        return frag1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, set the flag
                isPermissionGranted = true;
                // Update the RecyclerView
                if (isPermissionGranted) {
                    displaysongs();
                }
            } else {
                // Permission denied, handle it as needed (e.g., show a message to the user)
            }
        }
    }

    public void displaysongs() {
        final ArrayList<File> mysongs = findsng(Environment.getExternalStorageDirectory());
        items = new String[mysongs.size()];
        for (int i = 0; i < mysongs.size(); i++) {
            items[i] = mysongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        rvadapter rvadapter = new rvadapter(requireActivity(), items, mysongs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvadapter);
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

