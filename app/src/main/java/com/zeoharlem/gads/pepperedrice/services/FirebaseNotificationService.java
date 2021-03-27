package com.zeoharlem.gads.pepperedrice.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.activities.ChatActivity;
import com.zeoharlem.gads.pepperedrice.utils.Helpers;
import com.zeoharlem.gads.pepperedrice.utils.L;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private String myUsername;
    private String myImageUrl;
    private String myPhoneNum;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> map = remoteMessage.getData();
            String title    = map.get("title");
            String message  = map.get("message");
            String hisID    = map.get("hisId");
            String hisImage = map.get("hisImage");
            String chatID   = map.get("chatId");
            //My Properties
            myUsername      = map.get("myUsername");
            myImageUrl      = map.get("myImageUrl");
            myPhoneNum      = map.get("myPhoneNum");

            Log.d("onMessageReceived", "onMessageReceived: chatID is " + chatID + "\n hisID" + hisID);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createOreoNotification(myUsername, message, hisID, hisImage, chatID);
            }
            else {
                createNormalNotification(myUsername, message, hisID, hisImage, chatID);
            }
        }
        else {
            Log.d("onMessageReceived", "onMessageReceived: no data ");
        }
        //super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        sendUpdatedToken(s);
        super.onNewToken(s);
        Log.d("RefrehsedToken", "onNewToken: "+s);
    }

    private void sendUpdatedToken(String token) {
        FirebaseAuth firebaseAuth   = FirebaseAuth.getInstance();
        String firebaseUid          = FirebaseAuth.getInstance().getUid();
        Log.d("TAG", "sendUpdatedToken: "+firebaseUid);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(firebaseUid);
        Map<String, Object> map             = new HashMap<>();
        map.put("token", token);
        databaseReference.updateChildren(map);

    }

    private void createNormalNotification(String title, String message, String hisID, String hisImage, String chatID){
        Uri uri                             = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(this, Helpers.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_outline_food_bank_white_24)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.colorRed, null))
                .setSound(uri);

        builder.setContentIntent(pendingIntentAction(hisID, hisImage, chatID));

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85 - 65), builder.build());

    }

    private void createOreoNotification(String title, String message, String hisID, String hisImage, String chatID) {
        NotificationChannel channel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(Helpers.CHANNEL_ID, "PepperedRice", NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            PendingIntent pendingIntent = pendingIntentAction(hisID, hisImage, chatID);

            Notification notification = new Notification.Builder(this, Helpers.CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setChannelId(Helpers.CHANNEL_ID)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.colorRed, null))
                    .setSmallIcon(R.drawable.ic_outline_food_bank_white_24)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(100, notification);
        }

    }

    private PendingIntent pendingIntentAction(String hisID, String hisImage, String chatID){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("hisId", hisID);
        intent.putExtra("imageUrl", hisImage);
        intent.putExtra("chatId", chatID);
        //My Properties
        intent.putExtra("myUsernameR", myUsername);
        intent.putExtra("myPhoneNumR", myPhoneNum);
        intent.putExtra("myImageUrlR", myImageUrl);

        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    public void createQNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId  = 1;
        String channelId    = Helpers.CHANNEL_ID;
        String channelName  = "PepperedRice";
        int importance      = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel    = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder   = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent   = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

}