// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.app.mediapicker.cropper;


import static com.app.mediapicker.cropper.Constants.APP_DOCUMENTS_DIRECTORY_NAME;
import static com.app.mediapicker.cropper.Constants.CROPPED_FILE_PATH;
import static com.app.mediapicker.cropper.Constants.FILENAME_IMAGE_CROPPER;
import static com.app.mediapicker.cropper.Constants.FILENAME_TEMP;
import static com.app.mediapicker.cropper.Constants.FILE_IMAGE;
import static com.app.mediapicker.cropper.Constants.FILE_PROVIDER;
import static com.app.mediapicker.cropper.Constants.REQUEST_CAMERA_PICK_IMAGE;
import static com.app.mediapicker.cropper.Constants.REQUEST_GALLERY_PICK_IMAGE;
import static com.app.mediapicker.cropper.Constants.TEMP_DIRECTORY_NAME;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.viewbinding.BuildConfig;

import com.app.mediapicker.R;
import com.app.mediapicker.utils.CompressImageUtils;
import com.app.mediapicker.utils.FileUtil;
import com.app.mediapicker.utils.RealPathUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CropActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnCropImageCompleteListener {

    private CropDemoPreset mDemoPreset;
    private CropImageView mCropImageView;
    private MaterialButton btnCrop, btnPickUp;
    private BottomSheetDialog mBottomSheetDialog;
    private BottomSheetBehavior mBehavior;
    private BottomSheetBehavior mBottomSheetBehavior;
    private LinearLayout lincamera, lingallery;
    private View bottom_sheet, bottom_sheet_content;
    private int pick;
    //    pickCancel;
    private String tempFilePath = "", camBase64 = "", galBase64 = "";
    private Bitmap bitmap;
    private AppCompatImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        mCropImageView = findViewById(R.id.cropImageView);
        btnCrop = findViewById(R.id.btnCrop);
        btnPickUp = findViewById(R.id.btnPickUp);
//        bottom_sheet = findViewById(R.id.bottom_sheet);
        imageView = findViewById(R.id.resultImageView);
//        bottom_sheet_content = findViewById(R.id.bottom_sheet_content);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
        mDemoPreset = CropDemoPreset.RECT;
//        pickCancel=1;

        showDialog();
//        mCropImageView.setImageResource(R.drawable.cat);
//        initBottomSheet();

        btnCrop.setOnClickListener(v -> mCropImageView.getCroppedImageAsync());

//        btnPickUp.setOnClickListener(v -> CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(MainActivity2.this));

        btnPickUp.setOnClickListener(v -> showDialog());

    }

    private void showDialog() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle(getString(R.string.alert))
                .setMessage(getResources().getString(R.string.choose_images))
                .setIcon(R.drawable.ic_vector_alert)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.camera)
                        , (dialogInterface, i) -> {
                            pickImageFromCamera(REQUEST_CAMERA_PICK_IMAGE, TEMP_DIRECTORY_NAME, FILENAME_TEMP);
                        })
                .setNeutralButton(getResources().getString(R.string.cancel)
                        , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            finish();
//                            if (pickCancel == 1) {
//                                finish();
//                            }
                        })
                .setNegativeButton(getResources().getString(R.string.gallery),
                        (dialogInterface, i) -> {
                            pickImageFromGallery(REQUEST_GALLERY_PICK_IMAGE);
                        })
                .show();
    }


    private void pickImageFromCamera(int imageCode, String foldername, String filename) {
        try {
            tempFilePath = "";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,
                    "com.app.mediapicker" + FILE_PROVIDER, FileUtil.Companion.createImageFiles(foldername, filename)));
            startActivityForResult(intent, imageCode);
            tempFilePath = FileUtil.Companion.createImageFiles(foldername, filename).getPath();
            System.out.println("-->tempFilePath-->"+tempFilePath);
            System.out.println("-->imageCode-->"+imageCode);
//            pickCancel=0;
//            collapseBottomSheet();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pickImageFromGallery(int imageCode) {
        try {
            //Create an Intent with action as ACTION_PICK
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            // Launching the Intent
            startActivityForResult(intent, imageCode);
//            pickCancel=0;
//            collapseBottomSheet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(this, "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(this, "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    private void setIntentData(String filePath) {
        Intent intent = new Intent();
        intent.putExtra(CROPPED_FILE_PATH, filePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("data->"+data);
        System.out.println("requestCode->"+requestCode);
        System.out.println("resultCode->"+resultCode);
        System.out.println("tempFilePath->"+tempFilePath);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_PICK_IMAGE:
                    btnPickUp.setVisibility(View.VISIBLE);
                    btnCrop.setVisibility(View.VISIBLE);
                    String camerapath = Uri.parse(tempFilePath).getPath();
                    camerapath = CompressImageUtils.compressImage(APP_DOCUMENTS_DIRECTORY_NAME + "/"
                            , camerapath, FILENAME_IMAGE_CROPPER);
                    System.out.println("camerapath-->" + camerapath);
                    mCropImageView.setImageBitmap(BitmapFactory.decodeFile(camerapath));
//                    camBase64 = convertToBase64(camerapath);
//                    System.out.println("B" + camBase64);
//                    deleteFolder(TEMP_DIRECTORY_NAME);
                    break;
                case REQUEST_GALLERY_PICK_IMAGE:
                    btnPickUp.setVisibility(View.VISIBLE);
                    btnCrop.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    String path = RealPathUtils.Companion.getUriRealPath(this, uri);
                    path = CompressImageUtils.compressImage(APP_DOCUMENTS_DIRECTORY_NAME + "/"
                            , path, FILENAME_IMAGE_CROPPER);
                    System.out.println("gallerypath-->" + path);
                    mCropImageView.setImageBitmap(BitmapFactory.decodeFile(path));
//                    galBase64 = convertToBase64(path);
//                    System.out.println("B" + galBase64);
//                    upload_driving_license.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_vector_check_grey, 0);
//                    upload_driving_license.requestFocus();
//                    deleteFolder(TEMP_DIRECTORY_NAME);
                    break;
//            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                System.out.println("Data-->" + data);
//                Uri uri = data.getData();
//                System.out.println("uri-->" + uri);
//                String path = getUriRealPath(this, uri);
//                System.out.println("path-->" + path);
//
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                System.out.println("result-->" + result);
//                handleCropResult(result);
//                break;
//            case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
//                CropImage.ActivityResult results = CropImage.getActivityResult(data);
//                Exception error = results.getError();
//                System.out.println("error-->" + error);
//                break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result != null) {
            if (result.getError() == null) {
                bitmap = mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
                        ? CropImage.toOvalBitmap(result.getBitmap())
                        : result.getBitmap();
                mCropImageView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss" , Locale.ENGLISH);
                String currentTimeStamp = dateFormat.format(new Date());
                File file = FileUtil.Companion.bitmapToFile(this, bitmap, FILE_IMAGE + currentTimeStamp);
                if (file != null) {
                    System.out.println("file-->" + file.getAbsolutePath());
                    setIntentData(file.getAbsolutePath());
                } else {
                    Toast.makeText(this, "Unable to crop", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e("AIC", "Failed to crop image", result.getError());
                Toast.makeText(
                        this,
                        "Image crop failed: " + result.getError().getMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}

//    private void handleCropResult(CropImageView.CropResult result) {
//        if (result != null) {
//            if (result.getError() == null) {
//                Intent intent = new Intent(this, CropResultActivity.class);
//                intent.putExtra("SAMPLE_SIZE", result.getSampleSize());
//                if (result.getUri() != null) {
//                    intent.putExtra("URI", result.getUri());
//                    System.out.println("called 1");
//                } else {
//                    System.out.println("called 2");
//                    bitmap = mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
//                            ? CropImage.toOvalBitmap(result.getBitmap())
//                            : result.getBitmap();
//                    imageView.setVisibility(View.VISIBLE);
//                    imageView.setImageBitmap(bitmap);
//                    mCropImageView.setVisibility(View.VISIBLE);
//
//                }
//                startActivity(intent);
//            } else {
//                Log.e("AIC", "Failed to crop image", result.getError());
//                Toast.makeText(
//                        this,
//                        "Image crop failed: " + result.getError().getMessage(),
//                        Toast.LENGTH_LONG)
//                        .show();
//            }
//        }
//    }
//}
