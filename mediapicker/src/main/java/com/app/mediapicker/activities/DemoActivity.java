package com.app.mediapicker.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.mediapicker.MediaItem;
import com.app.mediapicker.MediaOptions;
import com.app.mediapicker.R;
import com.app.mediapicker.activities.MediaPickerActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;
import java.util.Random;



public class DemoActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "DemoActivity";

    private static final int REQUEST_MEDIA = 100;
    private LinearLayout mLinearLayout;
    private List<MediaItem> mMediaSelectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mLinearLayout = (LinearLayout) findViewById(R.id.list_image);
        findViewById(R.id.pick).setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA) {
            if (resultCode == RESULT_OK) {
                mMediaSelectedList = MediaPickerActivity.Companion
                        .getMediaItemSelected(data);
                if (mMediaSelectedList != null) {
                    for (MediaItem mediaItem : mMediaSelectedList) {
                        addImages(mediaItem);
                    }
                } else {
                    Log.e(TAG, "Error to get media, NULL");
                }
            }
        }
    }

    private void addImages(MediaItem mediaItem) {
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item, null);

        ImageView imageView = (ImageView) root.findViewById(R.id.image);
        TextView textView = (TextView) root.findViewById(R.id.textView);

        String info = String.format("Original Uri [%s]\nOriginal Path [%s] \n\nCropped Uri [%s] \nCropped Path[%s]", mediaItem.getUriOrigin(), mediaItem.getUriCropped(), mediaItem.getPathOrigin(this), mediaItem.getPathCropped(this));
        textView.setText(info);
        if (mediaItem.getUriCropped() == null) {
            Glide.with(this).load(mediaItem.getUriOrigin()).into(imageView);
        } else {
            Glide.with(this).load(mediaItem.getUriCropped()).into(imageView);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 5;
        mLinearLayout.addView(root, params);
    }

    private void handleOptionDemoSelected(int option) {
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options = null;
        switch (option) {
            case 0:
                options = MediaOptions.Companion.createDefault();
                break;
            case 1:
                options = builder.setIsCropped(true).setFixAspectRatio(true)
                        .build();
                break;
            case 2:
                File file = new File(
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        new Random().nextLong() + ".jpg");
                options = builder.setIsCropped(true).setFixAspectRatio(true)
                        .setCroppedFile(file).build();
                break;
            case 3:
                options = builder.setIsCropped(true).setFixAspectRatio(true)
                        .setAspectX(3).setAspectY(1).build();
                break;
            case 4:
                options = builder.setIsCropped(true).setFixAspectRatio(false)
                        .build();
                break;

            case 5:
                options = builder.selectVideo().canSelectMultiVideo(true).build();
                break;
            case 6:
                options = builder.selectVideo().setMaxVideoDuration(3 * 1000)
                        .build();
                break;
            case 7:
                options = builder.selectVideo().setMinVideoDuration(2 * 1000)
                        .build();
                break;
            case 8:
                options = builder.selectVideo().setMaxVideoDuration(3 * 1000)
                        .setShowWarningBeforeRecordVideo(true).build();
                break;
            case 9:
                options = builder.canSelectBothPhotoVideo()
                        .canSelectMultiPhoto(true).canSelectMultiVideo(true)
                        .build();
                break;
            case 10:
                options = builder.canSelectMultiPhoto(true)
                        .canSelectMultiVideo(true).canSelectBothPhotoVideo()
                        .setMediaListSelected(mMediaSelectedList).build();
                break;
            case 11:

                break;
        }
        if (options != null) {
            clearImages();
            MediaPickerActivity.Companion.open(this, REQUEST_MEDIA, options);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pick) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getString(R.string.select_demo))
                    .setItems(R.array.options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handleOptionDemoSelected(which);
                        }
                    });
            dialogBuilder.show();
        }
    }

    private void clearImages() {
        mLinearLayout.removeAllViews();
    }
}