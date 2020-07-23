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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;
import com.example.easygreen.models.Recipe;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Headers;
import okhttp3.HttpUrl;

import static android.app.Activity.RESULT_OK;

public class DiscoverFragment extends Fragment {
    /***** Local Variables *************/
    private TextView tvPostText;
    private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private final String APP_TAG = "EasyGreen";
    private File photoFile;
    private String photoFileName = "photo.jpg";

    /************* Initial State of Fragment ********************/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton button = getActivity().findViewById(R.id.btnTakeMedia);
        tvPostText = getActivity().findViewById(R.id.tvPostText);
        getEasyGreenFeed();

        /*** User clicks on Floating Action Button ************/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
    }

    private void getEasyGreenFeed() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("graph.facebook.com")
                .addPathSegment("EasyGreenFBU")
                .addPathSegment("feed")
                .build();

        final String request = url+"?access_token=" + AccessToken.getCurrentAccessToken();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("getEasyGreenFeed()", "OnSuccess");
                JSONObject a = json.jsonObject;
                int b = statusCode;
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("getEasyGreenFeed()", response);
            }
        });
    }

    /****** Inflate Fragment layout ************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
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
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}