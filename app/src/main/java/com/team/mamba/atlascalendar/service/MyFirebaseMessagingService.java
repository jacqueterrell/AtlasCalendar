package com.team.mamba.atlascalendar.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.userInterface.AtlasApplication;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivity;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.util.Map;

import static com.team.mamba.atlascalendar.utils.AppSharedPrefs.FIREBASE_DEVICE_TOKEN;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

//    @Inject
//    AppDataManager dataManager;

    private static String NOTIFICATION_ID = "my_channel_02";
    private static PublishSubject<String>  data = PublishSubject.create();
    private static final String BUSINESS_NOTIFICATION = "businessNotification";
    private static final String CONTACT_NOTIFICATION = "contactNotification";
    private String notificationType = "";
    private SharedPreferences sharedPreferences;
    public static boolean isBusinessAnnouncement = false;
    private String subscribedTopic = "";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        String deviceToken = s;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setFirebaseDeviceToken(deviceToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages, data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        //check if message contains data payload
        if (remoteMessage.getData().size() > 0) {

           String from = remoteMessage.getFrom();

            for (Map.Entry<String,String> map : remoteMessage.getData().entrySet()){

                String value = map.getValue();

                if (from.contains("topic")){

                    subscribedTopic = from.replaceAll("/topics/","");
                }

                if (value.equals(AppConstants.NOTIFICATION_NEW_CONNECTION)){//new connection request
                    Logger.d("new connection request");
                    DashBoardActivity.newRequestCount += 1;
                    notificationType = CONTACT_NOTIFICATION;
                    break;


                } else if (value.equals(AppConstants.NOTIFICATION_NEW_ANNOUNCEMENT)){

                    DashBoardActivity.newAnnouncementCount += 1;
                    notificationType = BUSINESS_NOTIFICATION;
                    break;

                }
            }

            prepareMessage();

        }

    }


    private void prepareMessage(){

        if (notificationType.equals(CONTACT_NOTIFICATION)){//new contact notification received

            if (AtlasApplication.isAppInBackGround){

                sendNewRequestNotification("New connection request","A new user wants to connect with you!");

            } else {
                data.onNext(AppConstants.NOTIFICATION_NEW_CONNECTION);
            }

        } else if (notificationType.equals(BUSINESS_NOTIFICATION)){// new business announcement notification received

            if (AtlasApplication.isAppInBackGround){

                isBusinessAnnouncement = true;

                if (subscribedTopic != null){

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(AppConstants.BUSINESSES_COLLECTION)
                            .whereEqualTo("id",subscribedTopic)
                            .get()
                            .addOnCompleteListener(task -> {

                                if (task.isSuccessful()){

                                    BusinessProfile profile = task.getResult().toObjects((BusinessProfile.class)).get(0);
                                    sendNewRequestNotification(profile.getName() + " Sending an Announcement","You have new announcements!");

                                }
                            });

                } else {

                    sendNewRequestNotification("New Business Announcement","You have new business announcements!");

                }


            } else {
                data.onNext(AppConstants.NOTIFICATION_NEW_ANNOUNCEMENT);
            }
        }
    }

    public void setFirebaseDeviceToken(String token) {

        sharedPreferences
                .edit()
                .putString(FIREBASE_DEVICE_TOKEN, token)
                .apply();
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     */
    private void sendNewRequestNotification(String title,String body) {

        long[] pattern = {0, 1000};

        MediaPlayer mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.alert_chime);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Intent intent = WelcomeActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Atlas Notification");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(pattern);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(pattern)
                .setContentIntent(pendingIntent);

        //  mediaPlayer.start();


        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    /**
     * The {@link DashBoardActivity} will subscribe to this and observe
     * All messages.
     *
     * @return
     */
    public static Observable<String> getObservable() {

        return data;
    }
}
