package com.app.sb.sbservices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;


import com.app.sb.sbservices.DescriptionActivity.MainActivity;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.GlobalVariable;
import com.app.sb.sbservices.Utils.PrefManager;

public class SplashActivity extends AppCompatActivity {

  AppCompatImageView ivImage;
    PrefManager prefManager;
Handler handler;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        prefManager = new PrefManager(this);


        getSupportActionBar().hide();
        ivImage = findViewById(R.id.logo_one);
        prefManager = new PrefManager(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        GlobalVariable.deviceWidth = displayMetrics.widthPixels;
        GlobalVariable.deviceHeight = displayMetrics.heightPixels;


        getNotifications();


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!prefManager.getBoolean(AppConstants.APP_USER_LOGIN)) {
                    Intent intent = new Intent(SplashActivity.this, GuestLoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, BottomNavActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }
            }

        }, 3000);

    }

    private void getNotifications() {


        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                //  Log.d(Application.APPTAG, "Extras received at onCreate:  Key: " + key + " Value: " + value);

                Log.e("cbgdvcgdvcg","dcbdcvdgv "+"Extras received at onCreate:  Key: " + key + " Value: " + value);
            }
            String title = extras.getString("title");
            String message = extras.getString("body");
            String tag = extras.getString("tag");

            String  image =  extras.getString("image");
            String icon =  extras.getString("icon");

            String video_id = extras.getString("video_id");

            String cat_list_sub_id = extras.getString("cat_id");

            String id = extras.getString("id");

            //     String user_id = extras.getString("user_id");


            Log.e("cbgdvcgdvcg","spalesh title "+title);
            //    Log.e("cbgdvcgdvcg","spalesh title "+SharedPreference.getStringPreference(this,"NOTIFICATIONSTATUS"));
            Log.e("cbgdvcgdvcg","messgae "+message);
            Log.e("cbgdvcgdvcg","tag............. "+tag);

            Log.e("cbgdvcgdvcg","catlist id............. "+cat_list_sub_id);

            Log.e("cbgdvcgdvcg","image.......... "+image);
            Log.e("cbgdvcgdvcg","icon............ "+icon);

            //     Log.e("cbgdvcgdvcg","user id............. "+user_id);



            if (message!=null && message.length()>0) {
                getIntent().removeExtra("body");

                if (tag.equalsIgnoreCase("manikratna"))
                {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("id",video_id);
                    intent.putExtra("name",title);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    //    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    Intent intent = new Intent(this, MainActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }

                //  showNotificationInADialog(title, message);
            }
        }

        // notifications channel creation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create channel to show notifications.
            String channelId = getResources().getString(R.string.default_notification_channel_id);
            String channelName = getResources().getString(R.string.common_google_play_services_notification_channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));



        }




    }
}



