package com.example.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VideoRVAdapter.VideoClickInterface {
    private RecyclerView videorv;
    private ArrayList<VideoRVmodal> videoRVmodalArrayList;
    private VideoRVAdapter videoRVAdapter;
    private static final int STORAGE_PERMISSION=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videorv=findViewById(R.id.idRVVideos);
        videoRVmodalArrayList=new ArrayList<>();
        videoRVAdapter=new VideoRVAdapter(videoRVmodalArrayList,this,this::onVideoClick);
        videorv.setLayoutManager(new GridLayoutManager(this,2));
        videorv.setAdapter(videoRVAdapter);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION);
        }
        else{
            getVideos();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_PERMISSION)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                getVideos();
            }
            else {
                Toast.makeText(this, "The application will not work if permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getVideos(){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
      Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor=contentResolver.query(uri,null,null,null,null);

        if(cursor !=null && cursor.moveToFirst()) {
            do {
                     if(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)==-1||cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)==-1)
                     {
                         cursor.moveToNext();
                     }

                     String videoTiltle= cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String videoPath= cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                /*
                    Cursor cursor = getApplicationContext().getContentResolver().query(
                    MediaStore.media-type.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                );

                while (cursor.moveToNext()) {
                    // Use an ID column from the projection to get
                    // a URI representing the media item itself.
                 }
              */
                Bitmap videoThumbnail= ThumbnailUtils.createVideoThumbnail(videoPath,MediaStore.Images.Thumbnails.MINI_KIND);
                videoRVmodalArrayList.add(new VideoRVmodal(videoTiltle,videoPath,videoThumbnail));
                Log.d("myerror", "getVideos: cursor "+cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                Log.d("myerror", "getVideos: path "+cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            }while(cursor.moveToNext());
        }

        videoRVAdapter.notifyDataSetChanged();

    }
    @Override
    public void onVideoClick(int position) {
        Log.d("myerror", "getVideos: onhjkjhclick "+videoRVmodalArrayList.get(position).getVideoname());
        Intent i=new Intent(MainActivity.this,VideoPlayerAcitivity.class);
        Log.d("myerror", "getVideos: onclick "+videoRVmodalArrayList.get(position).getVideoname());
        i.putExtra("videoname",videoRVmodalArrayList.get(position).getVideoname());
        Log.d("myerror", "getVideos: oncliddck "+videoRVmodalArrayList.get(position).getVideoname());
        i.putExtra("videopath",videoRVmodalArrayList.get(position).getVideopath());
        startActivity(i);
    }
}