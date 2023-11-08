package com.example.player;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class play extends AppCompatActivity {
    Button btnplay,btnnext,btnprevious,btnforward,btnbackward;
    TextView txtsname,txtstart,txtstop;
    ImageView imageView;
    SeekBar seekmusic;
    String sname;
    public static final String Extra_name = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mysongs;
    Thread updateseekbar;
    Toolbar toolbar;
    Database database;
    customadaptr customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        btnplay = findViewById(R.id.playbtn);
        btnnext = findViewById(R.id.next);
        btnprevious = findViewById(R.id.previous);
        btnbackward = findViewById(R.id.backward);
        btnforward = findViewById(R.id.forward);
        seekmusic = findViewById(R.id.seekBar);
        txtsname = findViewById(R.id.txtsng);
        txtstart = findViewById(R.id.txtstart);
        txtstop = findViewById(R.id.txtend);
        imageView = findViewById(R.id.imageViewsng);
        toolbar = findViewById(R.id.toolbar);
        database = new Database(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mysongs =(ArrayList) bundle.getParcelableArrayList("songs");
        String songname = i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        txtsname.setSelected(true);
        Uri uri = Uri.parse(mysongs.get(position).toString());
        sname = mysongs.get(position).getName();
        txtsname.setText(sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        updateseekbar = new Thread(){
            @Override
            public void run(){
                int totalduration = mediaPlayer.getDuration();
                int currentposition =0;
                while (currentposition <totalduration){
                    try {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentposition);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        String endtime = createtime(mediaPlayer.getDuration());
        txtstop.setText(endtime);
        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currenttime = createtime(mediaPlayer.getCurrentPosition());
                txtstart.setText(currenttime);
                handler.postDelayed(this,delay);
            }
        },delay);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1) % mysongs.size());
                Uri u= Uri.parse(mysongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mysongs.get(position).getName();
                txtsname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.baseline_pause_24);
                startAnimation(imageView);
                int storedres = database.getCountByPosition(position);
                if(storedres == 0) {
                    long res = database.store(songname, position);
                    if (res > 0) {
                        Toast.makeText(play.this, "stored", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(play.this, "not stored", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    int res = database.updateCount(position);
                    if(res > 0){
                        Toast.makeText(play.this, "updated", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(play.this, "not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    btnplay.setBackgroundResource(R.drawable.baseline_play_arrow_24);
                    mediaPlayer.pause();
                } else {
                    btnplay.setBackgroundResource(R.drawable.baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });
        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position -1)<0)?(mysongs.size() -1):(position -1);
                Uri u= Uri.parse(mysongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mysongs.get(position).getName();
                txtsname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.baseline_pause_24);
                startAnimation(imageView);
                int storedres = database.getCountByPosition(position);
                if(storedres == 0) {
                    database.store(songname, position);
                }
                else {
                    database.updateCount(position);

                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnnext.performClick();
            }
        });
        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });
        btnbackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });
    }
    public void startAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }
    public String createtime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        time+=min+":";
        if (sec<10){
            time+="0";
        }
        time+=sec;
        return time;
    }
}