package com.example.easygreen.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.codepath.asynchttpclient.AbsCallback;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;
import com.example.easygreen.adapters.VideoAdapter;
import com.example.easygreen.models.Video;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;


import static android.app.Activity.RESULT_OK;

public class DiscoverFragment extends Fragment {
    /***** Local Variables *************/
    private ImageView shareButton;
    public String accessToken;
    public String title = "";
    public String description = "";
    private String videoFileName = "post.mp4";

    /***** Local references *************/
    private ViewPager2 vpVideoContainer;
    private VideoAdapter videoAdapter;
    private List<Video> videoFeed = new ArrayList<>();
    private File videoFile;
    private StorageReference mStorageRef;


    /****** Inflate Fragment layout ************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    /************* Initial State of Fragment ********************/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accessToken = getActivity().getResources().getString(R.string.access_token);
        shareButton = getActivity().findViewById(R.id.ivShareFacebook);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordVideo();
            }
        });
        getEasyGreenFeed();
        displayFeed(getActivity());
    }

    private void displayFeed(FragmentActivity activity) {
        vpVideoContainer = getActivity().findViewById(R.id.vpVideoContainer);
        videoAdapter = new VideoAdapter(videoFeed);
        vpVideoContainer.setAdapter(videoAdapter);
    }

    /************* Send video to Facebook Page - the feed ****************/
    public void postVideo(String takenVideo) throws JSONException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("graph.facebook.com")
                .addPathSegment("EasyGreenFBU")
                .addPathSegment("videos")
                .addQueryParameter("file_url", takenVideo)
                .build();
        final String request = url+"&title="+title+"&description="+description+
                "&access_token="+accessToken;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("Gustavo", "onSuccess: " + json);
                getEasyGreenFeed();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("Gustavo", "onSuccess: " + response);
            }
        });
    }

    /***** Get all videos from Facebook Page - acts as a feed *******/
    private void getEasyGreenFeed() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("graph.facebook.com")
                .addPathSegment("EasyGreenFBU")
                .addPathSegment("videos")
                .addQueryParameter("fields", "source,title,description")
                .build();

        final String request = url+"&access_token="+accessToken;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject feedJson = json.jsonObject;
                try {
                    JSONArray data = feedJson.getJSONArray("data");
                    for(int i = 0; i < data.length(); i++){
                        Video currentVideo = new Video();
                        JSONObject videoObject = data.getJSONObject(i);
                        if(videoObject.getString("source") != null &&
                                videoObject.getString("title") != null &&
                                videoObject.getString("description") != null){
                            currentVideo.setVideoURL(videoObject.getString("source"));
                            currentVideo.setVideoTitle(videoObject.getString("title"));
                            currentVideo.setVideoDescription(videoObject.getString("description") );
                        }

                        currentVideo.setLikeCount(0);
                        videoFeed.add(currentVideo);
                        videoAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("Gustavo", "onFailure: " + response);
            }
        });
    }


    /***** Open Phone Camera *******/
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoFile = getVideoFileUri(videoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.EasyGreen", videoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    /***** Get Picture Path File *******/
    public File getVideoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "EasyGreen");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    /******* Pass Picture back to Fragment **********/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            uploadFirebase(videoFile.getAbsolutePath());
        } else {
            Toast.makeText(getContext(), "Video wasn't recorded!", Toast.LENGTH_SHORT).show();
        }
    }

    /******* Send video to Firebase cloud storage **********/
    private void uploadFirebase(String videoFile) {
        Uri file = Uri.fromFile(new File(videoFile));
        mStorageRef = mStorageRef.child("post.mp4");

        mStorageRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                getUserInput(task.getResult().toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("Gustavo", "onFailure: " + exception.getMessage());
                    }
                });
    }

    /******* User input for creating a post **********/
    private void getUserInput(final String fileLink) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Creating a new Post");
        alert.setMessage("Enter Title and Description");

        final LinearLayout lay = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_alert, null);
        alert.setView(lay);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText inputTitle = lay.findViewById(R.id.inputTitle);
                EditText inputDescription = lay.findViewById(R.id.inputDescription);
                title = inputTitle.getText().toString();
                description = inputDescription.getText().toString();
                try {
                    postVideo(fileLink);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
    }
}