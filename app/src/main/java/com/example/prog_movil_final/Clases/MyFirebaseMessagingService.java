package com.example.prog_movil_final.Clases;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Looper.prepare();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
//                Log.e("DEBUG_LATER",remoteMessage.getNotification().getTitle().toString());
                Toast.makeText(getBaseContext(),remoteMessage.getNotification().getTitle(), Toast.LENGTH_LONG).show();
            }
        });

        Looper.loop();

    }

    public static String getToken(Context context){
        return context.getSharedPreferences("_",MODE_PRIVATE).getString("fb","empty");
    }


}
