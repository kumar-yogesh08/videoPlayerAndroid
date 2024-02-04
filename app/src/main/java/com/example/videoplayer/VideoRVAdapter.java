package com.example.videoplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoRVAdapter extends RecyclerView.Adapter<VideoRVAdapter.ViewHolder> {
    private ArrayList<VideoRVmodal> videoRVmodalArrayList;
    private Context context;
    private VideoClickInterface videoClickInterface;

    public VideoRVAdapter(ArrayList<VideoRVmodal> videoRVmodalArrayList, Context context, VideoClickInterface videoClickInterface) {
        this.videoRVmodalArrayList = videoRVmodalArrayList;
        this.context = context;
        this.videoClickInterface = videoClickInterface;
    }

    @NonNull
    @Override
    public VideoRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoRVAdapter.ViewHolder holder,int position) {
    VideoRVmodal videoRVmodal=videoRVmodalArrayList.get(position);
    holder.thumbnailiv.setImageBitmap(videoRVmodal.getThumbnail());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("myerror", "getVideos: onclick "+videoRVmodalArrayList.get(holder.getAdapterPosition()).getVideopath());
            Log.d("myerror", "getVideos: onclick "+videoRVmodalArrayList.get(holder.getAdapterPosition()).getVideoname());
         videoClickInterface.onVideoClick(holder.getAdapterPosition());
        }
    });
    }

    @Override
    public int getItemCount() {
        return videoRVmodalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView thumbnailiv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailiv=itemView.findViewById(R.id.idIVThumbnail);
        }
    }
    public interface VideoClickInterface{
        void onVideoClick(int position);



    }
}
