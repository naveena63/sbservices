package com.app.sb.sbservices.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.app.sb.sbservices.DescriptionActivity.MainActivity;
import com.app.sb.sbservices.GuestLoginActivity;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.SharedPreference;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    PendingIntent pendingIntent;
    private Context context;
    private NotificationManager notifManager;
    private Bitmap bigPicture;

    private Bitmap iconImage;

    private Bitmap imagePicture;
    // private String imageurl;


    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);

        SharedPreference.setStringPreference(this, "TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        // TODO(developer): Handle FCM messages here.

        Log.e("djcdbcbdc", "remoteMessage " + remoteMessage.toString());
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.e(TAG, "notification: " + notification);


        Map<String, String> getData = remoteMessage.getData();
        Log.e(TAG, "getData: " + getData);

        Log.e("djcdbcbdc", "getData " + getData.toString());
        Log.e("djcdbcbdc", "getData " + getData);

//       Log.e("djcdbcbdc", "notification " + notification.toString());
        Log.e("djcdbcbdc", "notification " + notification);

        // Check if message contains a data payload.
        if (getData.size() > 0) {
            Log.d(TAG, "Message data payload: " + getData);
            sendNotification(getData);
        } else {
            // Check if message contains a notification payload.
            if (notification != null) {
                Log.d(TAG, "Message Notification Body: " + notification.getBody());
                sendNotification(notification);
            }
        }
    }


    private void sendNotification(RemoteMessage.Notification notification) {
        //Log.d(TAG, "sendNotification: " + data.toString());
        //{msg_id=7, user_id=2, body=You have a notification from Guardian, title=Guardian, message=this ia test message }
        Intent intent = null;
        String body, msg_id, title, message;

        try {
            //getting the title and the body
            title = notification.getTitle();
            message = notification.getBody();
            String icon = notification.getIcon();
            body = notification.getBody();


            //  imageurl = String.valueOf(notification.getImageUrl());

            String notificationType = notification.getTag();

            Log.e("djcdbcbdc", "iconURL " + icon);

            //   bigPicture = getBitmapfromUrl(icon);

            Log.e("djcdbcbdc", "Noti data " + notification.toString());
            Log.e("djcdbcbdc", "Noti type " + notification.getTag());


            Log.e("djcdbcbdc", "NotifyTitle " + title);
            Log.e("djcdbcbdc", "NotifyMessage " + message);
            Log.e("djcdbcbdc", "image url " + notification.getImageUrl());
            //   imagePicture = getBitmapfromUrl(notification.getImageUrl().toString());
            //    Log.e("djcdbcbdc", "image url bitmap " + getBitmapfromUrl(notification.getImageUrl().toString()));

            if (notificationType.equalsIgnoreCase("manikvideo")) {
//                intent = new Intent(this, ManikratnaActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                taskStackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.e("fvhbfhvbfhb", "fvhbfbhb " + "manikVedeo");
            }
            else {
                Log.e("fvhbfhvbfhb", "fvhbfbhb " + "others");
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            createNotification(title, body, message, bigPicture, intent);

        } catch (Exception e) {
            e.printStackTrace();

            Log.e("dcjdncjndc", "dchdbchbd " + e.toString());
        }

    }


    private void sendNotification(Map<String, String> data) {
        //Log.d(TAG, "sendNotification: " + data.toString());
        //{msg_id=7, user_id=2, body=You have a notification from Guardian, title=Guardian, message=this ia test message }
        Intent intent = null;
        String body, msg_id, title, message;

        try {
            title = data.get("title");
            //  message = data.get("message");
            //   body = data.get("body");
            message = data.get("body");
            body = data.get("message");
            String tagName = data.get("key");
            String pagename = data.get("pagename");
            String video_id = data.get("video_id");

            String icon = data.get("icon");

            String image = data.get("image");

            String cat_list_sub_id = data.get("cat_id");

            String id = data.get("id");

            bigPicture = getBitmapfromUrl(image);
            iconImage = getBitmapfromUrl(icon);

            //  String userId = data.get("user_id");


          //  SharedPreference.setStringPreference(this, "NOTIFICATIONSTATUS", "YES");


            //   imageurl = "https://png.pngtree.com/png-clipart/20190515/original/pngtree-neon-bar-circular-border-png-image_3843928.jpg";

            String newtag = data.get("tag");

            String picture_url = data.get("picture_url");

            String extraField1 = data.get("extraField1");

            //   bigPicture = getBitmapfromUrl(picture_url);

            Log.e("djcdbcbdc", "data  " + data.toString());
            Log.e("djcdbcbdc", "tag name  " + tagName);
            Log.e("djcdbcbdc", "extraField1  " + extraField1);
            Log.e("djcdbcbdc", "newtag  " + newtag);

            Log.e("djcdbcbdc", "title " + title);
            Log.e("djcdbcbdc", "message " + message);

            Log.e("djcdbcbdc", "body " + body);
            Log.e("djcdbcbdc", "pagename " + pagename);
            Log.e("djcdbcbdc", "bigPicture " + bigPicture);
            Log.e("djcdbcbdc", "vedio id " + video_id);
            Log.e("djcdbcbdc", "cat id " + cat_list_sub_id);
            //   Log.e("djcdbcbdc", "userd id " + userId);
            Log.e("djcdbcbdc", "id " + id);

            Log.e("djcdbcbdc", "image url  " + image);
            Log.e("djcdbcbdc", "icon image url  " + icon);

            Log.e(TAG, data.toString());


            if (title.equalsIgnoreCase("manikratna")) {
//                intent = new Intent(this, VedeoActivity.class);
//                intent.putExtra("id", video_id);
//                intent.putExtra("name", title);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                taskStackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.e("fvhbfhvbfhb", "fvhbfbhb " + "manikVedeo");
            }
          else {
                Log.e("fvhbfhvbfhb", "fvhbfbhb " + "others");
                intent = new Intent(this, GuestLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }


            createNotification(title, body, message, bigPicture, intent);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dcjdncjndc", "dchdbchbd " + e.toString());
        }
    }

    public void createNotification(String titles, String body, String aMessage, Bitmap bigPicture, Intent intent) {


        Log.e("dcjdncjndcfgf", "title " + titles);
        Log.e("dcjdncjndcfgf", "body " + body);
        Log.e("dcjdncjndcfgf", "message " + aMessage);


        final int NOTIFY_ID = 0; // ID of notification
        String id = getString(R.string.default_notification_channel_id); // default_channel_id
     //   String title = getString(R.string.default_notification_channel_title); // Default Channel
        String title = titles; // Default Channel


        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(aMessage);
        Log.e("bigPictureStyle", "bigPictureStyle " + bigPictureStyle);
        // assert bigPictureStyle != null;

        Bitmap pic = BitmapFactory.decodeResource(getResources(),R.drawable.sb_services_logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            try {

                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }

                builder = new NotificationCompat.Builder(this, id);


                //  Bitmap bitmap = getBitmapfromUrl(imageurl);

//                NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
//                s.setSummaryText("Image");
//                builder.setStyle(s);
//                builder.setSmallIcon(R.drawable.logo_nav_new);
//                builder.setDefaults(Notification.DEFAULT_ALL);
//                builder.setAutoCancel(true);
//                builder.setStyle(new NotificationCompat.BigPictureStyle().setSummaryText("hello").setBigContentTitle("naga"));



                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, 0);
                builder.setSmallIcon(R.drawable.sb_services_logo)// required
                        // required

                      //  .setColor(getResources().getColor(R.color.project_color))
                        .setColor(getResources().getColor(R.color.red))
                          .setContentTitle(body)// required
                        .setContentTitle(titles)// required
                        .setContentText(aMessage) // required

                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                        .setLargeIcon(bigPicture)
//                        .setLargeIcon(bigPicture)/*Notification icon image*/

                    //    .setLargeIcon(bigPicture)/*Notification icon image*/
                      //  .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                          .setStyle(bigPictureStyle)
                      //  .bigPicture(bigPicture)
                     //   .setStyle(new NotificationCompat.BigTextStyle().bigText(aMessage))
                        .setStyle(new NotificationCompat.BigPictureStyle().setSummaryText(aMessage).bigPicture(bigPicture).bigLargeIcon(pic).setBigContentTitle(title))
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                Notification notification = builder.build();
                notifManager.notify(NOTIFY_ID, notification);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                builder = new NotificationCompat.Builder(this, id);
                // intent = new Intent(this, SIdeNavigationItemsClickActivity.class);

                //  Bitmap bitmap = getBitmapfromUrl(imageurl);

//               NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
//               s.setSummaryText("Image");
//               builder.setStyle(s);
//               builder.setSmallIcon(R.drawable.logo_nav_new);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, 0);
                builder.setSmallIcon(R.drawable.sb_services_logo)   // required
                        .setColor(getResources().getColor(R.color.red))
                        .setContentTitle(body)// required
                        .setContentTitle(titles)// required
                        .setContentText(aMessage) // required

                        .setLargeIcon(bigPicture)/*Notification icon image*/

                        .setDefaults(Notification.DEFAULT_ALL)
                      //  .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setStyle(bigPictureStyle)
                     //   .setStyle(new NotificationCompat.BigTextStyle().bigText(aMessage))

                        .setStyle(new NotificationCompat.BigPictureStyle().setSummaryText(aMessage).bigPicture(bigPicture).bigLargeIcon(pic).setBigContentTitle(title))
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);

                Notification notification = builder.build();
                notifManager.notify(NOTIFY_ID, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        Notification notification = builder.build();
//        notifManager.notify(NOTIFY_ID, notification);
    }

//    public Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            Bitmap bitmap = null;
//            if (imageUrl.isEmpty()) {
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_nav_new);
//            } else {
//                URL url = new URL(imageUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                bitmap = BitmapFactory.decodeStream(input);
//
//                //                if (picture_url != null && !"".equals(picture_url)) {
////                    URL url = new URL(picture_url);
////                    bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
////                    Log.e("bigPicture", "DataLoadedPic " + bigPicture);
////                }
//            }
//            return bitmap;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//
//        }
//    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


    private void callNotificationTitleMethod(String title, String message, Intent intent, String body) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.sb_services_logo)
                .setColor(getResources().getColor(R.color.red))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        notificationBuilder.setContentText(message);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

