package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerAcitivity extends AppCompatActivity {
    private TextView videoNameTv,videoTimeTv;
    private ImageButton forwardIB,backIB,playPauseIB;
    private SeekBar seekBar;
    private VideoView videoView;
    private RelativeLayout controlsRL,videoRl;
    boolean isOpen=true;
    private String videoName,videoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_acitivity);
        videoName=getIntent().getStringExtra("videoname");
        videoPath=getIntent().getStringExtra("videopath");
        Log.d("myerror", "getVideos: ick "+videoName);
        videoTimeTv=findViewById(R.id.idTVTime);
        backIB=findViewById(R.id.idIBback);
        forwardIB=findViewById(R.id.idIBForward);
        playPauseIB=findViewById(R.id.idIBPlay);
        seekBar=findViewById(R.id.idSeekBarProgress);
        videoView=findViewById(R.id.idVideoView);
        controlsRL=findViewById(R.id.idRLControls);
        videoRl=findViewById(R.id.idRLvideo);
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });
        videoNameTv.setText(videoName);



        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.seekTo(videoView.getDuration()-10000);
            }
        });
        forwardIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.seekTo(videoView.getDuration()+10000);
            }
        });
        playPauseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isPlaying()){
                    videoView.pause();
                    playPauseIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                }
                else {
                    videoView.start();
                    playPauseIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause)) ;
                }

            }
        });

        videoRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    hidecontrol();
                    isOpen = false;
                }
                else {
                    showcontrol();
                    isOpen=true;
                }
            }
        });
        sethandler();
        initializeseekbar();
    }
     private void initializeseekbar()
     {
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(seekBar.getId()==R.id.idSeekBarProgress)
            {
                if(b)
                {
                    videoView.seekTo(i);
                    videoView.start();
                    int curpos=videoView.getCurrentPosition();
                    videoTimeTv.setText(convertime(videoView.getDuration()-curpos));
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
     }
     private void sethandler()
     {
         Handler handler=new Handler();
         Runnable runnable=new Runnable() {
             @Override
             public void run() {
                 if(videoView.getDuration()>0)
                 {
                     int curpos=videoView.getCurrentPosition();
                     seekBar.setProgress(curpos);
                     videoTimeTv.setText(""+convertime(videoView.getDuration()-curpos));
                 }
                 handler.postDelayed(this,0);
             }
         };
         handler.postDelayed(runnable,500);

     }
     private  String convertime(int ms){
        String time;
        int x,seconds,min,hrs;
        x=ms/1000;
        seconds=x%60;
        x/=60;
        min=x%60;
        x/=60;
        hrs=x%24;
        if(hrs!=0)
        {
            time=String.format("%02d",hrs)+":"+String.format("%02d",min)+":"+String.format("%02d",seconds);
        }
        else {
            time=String.format("%02d",min)+":"+String.format("%02d",seconds);
        }
        return time;
     }
    private void showcontrol() {
        controlsRL.setVisibility(View.VISIBLE);
        final Window window=this.getWindow();
        if(window==null)
        {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorview=window.getDecorView();
        if(decorview==null)
        {
            int uiOption=decorview.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT>=14)
            {
                uiOption&=~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16)
            {
                uiOption&=~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=19)
            {
                uiOption&=~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorview.setSystemUiVisibility(uiOption);
        }
    }

    private void hidecontrol() {
        controlsRL.setVisibility(View.GONE);
        final Window window=this.getWindow();
        if(window==null)
        {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorview=window.getDecorView();
        if(decorview==null)
        {
            int uiOption=decorview.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT>=14)
            {
                uiOption|=View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16)
            {
                uiOption|=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=19)
            {
                uiOption|=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorview.setSystemUiVisibility(uiOption);
        }
    }
}