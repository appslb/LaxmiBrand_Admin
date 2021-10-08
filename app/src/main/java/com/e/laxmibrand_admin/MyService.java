package com.e.laxmibrand_admin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.e.laxmibrand_admin.admin.AdminAboutUsActivity;
import com.e.laxmibrand_admin.admin.AdminOrderFragment;
import com.e.laxmibrand_admin.admin.MainActivity;


public class MyService extends Service {
    public static final String CHANNEL_ID = "LAXMICHANNEL";



    @Override
    public void onCreate(){
        super.onCreate();

    }


        @Override
        public int onStartCommand(Intent intent,int flags,int startId){
         /* createNotificationChannel();
          String input= intent.getStringExtra("inputExtra");
          Intent notificationIntent = new Intent(this, MainActivity.class);
          PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

            Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle("Laxmi Namkeen")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .build();
                          startForeground(1,notification);

                            return START_NOT_STICKY;
               //return super.onStartCommand(intent,flags,startId);*/
                showNotification("Laxmi Namkeen","You received new Order");
            return super.onStartCommand(intent,flags,startId);

    }

        @Override
        public void onDestroy(){
                super.onDestroy();
        }


    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;

//        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    void showNotification(String title, String message) {
        /*NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("WingsApp",
                    "WingsApp",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Wings Chat App");
            mNotificationManager.createNotificationChannel(channel);
        }
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "WingsApp")
                .setContentTitle(title) // title for notification
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setContentText(message)// message for notification
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), ChatsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());*/

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), (CHANNEL_ID));
        Intent intent = new Intent(getApplicationContext(), AdminOrderFragment.class);
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.tone);
        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(title);
        bigText.setBigContentTitle(message);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setLargeIcon(rawBitmap);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(soundUri);
        mBuilder.setStyle(bigText);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "LaxmiNamkeen", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            channel.setSound(soundUri,audioAttributes);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }


}