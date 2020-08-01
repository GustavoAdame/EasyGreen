package com.example.easygreen.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private TextView tvVideoTitle, tvVideoDescription, tvLikeCount;
    private ImageView ivLike;
    private List<Video> videoFeed;
    private Video currentVideo;

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

    /*** Add the following data into ViewHolder **********************/
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        currentVideo = videoFeed.get(position);
        tvVideoTitle.setText(currentVideo.getVideoTitle());
        tvVideoDescription.setText(currentVideo.getVideoDescription());
        vvVideo.setVideoURI(Uri.parse(currentVideo.getVideoURL()));

        if(currentVideo.isIsLiked()){
            ivLike.setImageResource(R.drawable.like_post);
        } else{
            ivLike.setImageResource(R.drawable.unlike_post);
        }

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentVideo.isIsLiked()){
                    ivLike.setImageResource(R.drawable.unlike_post);
                    currentVideo.setIsLiked(false);
                    tvLikeCount.setText(Integer.toString(currentVideo.getLikeCount()));
                } else{
                    ivLike.setImageResource(R.drawable.like_post);
                    currentVideo.setIsLiked(true);
                    tvLikeCount.setText(Integer.toString(currentVideo.getLikeCount()+1));
                }
            }
        });

        vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    /*** Part of the Adapter Interface but not in use **********************/
    @Override
    public int getItemCount() {
        return videoFeed.size();
    }

    /*** Inflate ViewHolder's view elements **********************/
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vvVideo = itemView.findViewById(R.id.vvVideo);
            tvVideoTitle = itemView.findViewById(R.id.tvVideoTitle);
            tvVideoDescription = itemView.findViewById(R.id.tvVideoDescription);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            ivLike = itemView.findViewById(R.id.ivLike);
        }
    }


}
