package com.example.storageper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String [] required_permissions = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
    };

    ImageView img_deny_images, img_given_images;
    ImageView img_deny_video, img_given_video;

    ImageView img_deny_audio, img_given_audio;

    Button btn_check_all_permission;

    boolean is_storage_image_permitted = false;
    boolean is_storage_video_permitted = false;
    boolean is_storage_audio_permitted = false;

    String TAG = "Permission";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_deny_images =(ImageView) findViewById(R.id.img_deny_images);
        img_given_images = (ImageView) findViewById (R.id.img_given_images);

        img_deny_video = (ImageView) findViewById(R.id.img_deny_video);
        img_given_video = (ImageView) findViewById(R.id.img_given_video);

        img_deny_audio = (ImageView) findViewById(R.id.img_deny_audio);
        img_given_audio =(ImageView) findViewById(R.id.img_given_audio);

        btn_check_all_permission = (Button) findViewById(R.id.btn_check_all_permission);


        btn_check_all_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!allPermissionResultCheck())
                {
                    requestPermissionStorageImages();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "All Storage Permission Granted..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public  boolean allPermissionResultCheck()
    {
        return is_storage_image_permitted && is_storage_video_permitted && is_storage_audio_permitted;
    }


    //     Code for read Storage media Storage
    public void requestPermissionStorageImages(){

        if (ContextCompat.checkSelfPermission(MainActivity.this,required_permissions[0])== PackageManager.PERMISSION_GRANTED){

            Log.d(TAG,required_permissions[0]+ "Granted");

            is_storage_image_permitted = true;
            img_deny_images.setVisibility(View.GONE);
            img_given_images.setVisibility(View.VISIBLE);

            if (!allPermissionResultCheck())
            {
                requestPermissionStorageVideo();
            }
        }
        else
        {
            request_permission_launcher_storage_images.launch(required_permissions[0]);
        }

    }
    private ActivityResultLauncher<String> request_permission_launcher_storage_images =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted ->{
                        if (isGranted)
                        {
                            Log.d(TAG,required_permissions[0]+ "Granted");

                            is_storage_image_permitted = true;
                            img_deny_images.setVisibility(View.GONE);
                            img_given_images.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Log.d(TAG,required_permissions[0]+ "Not Granted");

                            is_storage_image_permitted = false;
                        }
                        // if we want to make hierarchy of permission another check over here ...

                        if (!allPermissionResultCheck())
                        {
                            requestPermissionStorageVideo();
                        }
                    });
    //     Code for read media image ends

    //     Code for read media video start
    public void requestPermissionStorageVideo()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this,required_permissions[1])== PackageManager.PERMISSION_GRANTED){

            Log.d(TAG,required_permissions[1]+ "Granted");

            is_storage_video_permitted = true;
            img_deny_video.setVisibility(View.GONE);
            img_given_video.setVisibility(View.VISIBLE);

            if (!allPermissionResultCheck())
            {
                requestPermissionStorageAudio();
            }
        }
        else
        {
            request_permission_launcher_storage_video.launch(required_permissions[1]);
        }

    }
    private ActivityResultLauncher<String> request_permission_launcher_storage_video =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted ->{
                        if (isGranted)
                        {
                            Log.d(TAG,required_permissions[1]+ "Granted");

                            is_storage_video_permitted = true;
                            img_deny_video.setVisibility(View.GONE);
                            img_given_video.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Log.d(TAG,required_permissions[1]+ "Not Granted");

                            is_storage_video_permitted = false;
                        }
                        // if we want to make hierarchy of permission another check over here ...

                        if (!allPermissionResultCheck())
                        {
                            requestPermissionStorageAudio();
                        }

                    });
    //     Code for read media video ends

    public void requestPermissionStorageAudio(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,required_permissions[2])== PackageManager.PERMISSION_GRANTED){

            Log.d(TAG,required_permissions[2]+ "Granted");

            is_storage_audio_permitted = true;
            img_deny_audio.setVisibility(View.GONE);
            img_given_audio.setVisibility(View.VISIBLE);

        }
        else
        {
            request_permission_launcher_storage_audio.launch(required_permissions[2]);
        }
    }
    private ActivityResultLauncher<String> request_permission_launcher_storage_audio =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted ->{
                        if (isGranted)
                        {
                            Log.d(TAG,required_permissions[2]+ "Granted");

                            is_storage_audio_permitted = true;
                            img_deny_audio.setVisibility(View.GONE);
                            img_given_audio.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Log.d(TAG,required_permissions[2]+ "Not Granted");

                            is_storage_audio_permitted = false;

                            sendToSettingDialog();
                        }

                    });
    public void sendToSettingDialog()
    {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Alert for Permission")
                .setMessage("Go to settings for Permissions")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //      code to go to setting of application

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri .fromParts("package",getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }
}