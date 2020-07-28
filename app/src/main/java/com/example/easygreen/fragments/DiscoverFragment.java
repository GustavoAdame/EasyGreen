package com.example.easygreen.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;
import com.example.easygreen.adapters.VideoAdapter;
import com.example.easygreen.models.Video;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;

import static android.app.Activity.RESULT_OK;

public class DiscoverFragment extends Fragment {
    /***** Local Variables *************/
    private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private final String APP_TAG = "EasyGreen";
    private File photoFile;
    private String photoFileName = "photo.jpg";
    private ShareDialog shareDialog;
    private ImageView shareButton;
    private String accessToken;
    /***** Local Variables *************/
    private ViewPager2 vpVideoContainer;
    private VideoAdapter videoAdapter;
    private List<Video> videoFeed = new ArrayList<>();;

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
        getEasyGreenFeed();
        displayFeed(getActivity());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
    }

    private void displayFeed(FragmentActivity activity) {
        vpVideoContainer = getActivity().findViewById(R.id.vpVideoContainer);
        videoAdapter = new VideoAdapter(videoFeed);
        vpVideoContainer.setAdapter(videoAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareDialog = new ShareDialog(getActivity());
    }

    private void publishImage(Bitmap takenImage){
        FacebookSdk.sdkInitialize(getActivity());
        final SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(takenImage)
                .setCaption("Photo taken from EasyGreen")
                .build();

        final Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Sharing on Facebook....", Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();

        if(ShareDialog.canShow(SharePhotoContent.class)){
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(content);
        }
    }


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

                        currentVideo.setVideoURL(videoObject.getString("source"));
                        currentVideo.setVideoTitle(videoObject.getString("title"));
                        currentVideo.setVideoDescription(videoObject.getString("description") );
                        videoFeed.add(currentVideo);
                        videoAdapter.notifyDataSetChanged();
                    }
                    Log.d("Gustavo", "onSuccess: " + videoFeed.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("getEasyGreenFeed()", response);
            }
        });
    }


    /***** Open Phone Camera *******/
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.EasyGreen", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /***** Get Picture Path File *******/
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    /******* Pass Picture back to Fragment **********/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap image = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                publishImage(image);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}