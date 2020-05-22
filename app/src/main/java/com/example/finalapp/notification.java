package com.example.finalapp;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.Routes;

import org.json.JSONObject;


public class notification extends Service {
    int startMode;
    IBinder mbinder;
    JSONObject userinfo;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try{
            Thread.sleep(3000);
String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9tdWZpeC5vcmdcL2FwaV9zdWJfZG9tYWluXC9wdWJsaWNcL2FwaVwvdjFcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNTg5NzYyNDE0LCJleHAiOjE1ODk3NjYwMTQsIm5iZiI6MTU4OTc2MjQxNCwianRpIjoiV2VFeFdRR2VteWdMaDJiNSIsInN1YiI6MSwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.gj2RlbaftIUVPwy2c7i8pSB6GWE3EjxguY9oh0gBzA0";
             userinfo=Kernal.sendSingleGetRequest(Routes.student_notification+"20?token="+token);
            //System.out.println(userinfo.toString());
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher) //icon
                    .setContentTitle("Test") //tittle
                    .setAutoCancel(true)//swipe for delete
                    .setContentText("Hello Hello"); //content
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, builder.build()
            );

        }catch (Exception e){

        }
        try{
            Intent intent1=new Intent(this, MyFirebaseMessagingService.class);
            intent1.putExtra("data",userinfo.get("data").toString());
            sendBroadcast(intent1);
        }catch (Exception e){

        }

//        return super.onStartCommand(intent, flags, startId);
        return  startMode;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

}
