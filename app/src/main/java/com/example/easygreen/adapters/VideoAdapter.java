package com.example.easygreen.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.models.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context context;
    private VideoView vvVideo;
    private TextView tvVideoTitle, tvVideoDescription;
    private List<Video> videoFeed;

    public VideoAdapter(List<Video> videoFeed) {
        this.videoFeed = videoFeed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tvVideoTitle.setText(videoFeed.get(position).getVideoTitle());
        tvVideoDescription.setText(videoFeed.get(position).getVideoDescription());
        vvVideo.setVideoURI(Uri.parse(videoFeed.get(position).getVideoURL()));

        vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoFeed.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vvVideo = itemView.findViewById(R.id.vvVideo);
            tvVideoTitle = itemView.findViewById(R.id.tvVideoTitle);
            tvVideoDescription = itemView.findViewById(R.id.tvVideoDescription);
        }
    }
}
