package com.milnestmobile.androidlabs.notifications_dial_test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**По нажатию соответствующих кнопок выводит уведомления, объединённые в группу и разделённые
 * по каналам и создаёт диалоговый фрагмент
 * */
public class MainActivity extends AppCompatActivity implements MyDialogFragment.MyDialogListener{
    private final static String messageTitle = "My notification";
    private final static String messageTitle2 = "My second notification";
    private final static String messageText = "Hello World!";
    private final static String messageText2 = "Hello World too!";
    private final static String CHANNEL_ID = "1";
    private final static String NOTIFICATION_CHANNEL_NAME = "MyChannel";
    private final static String CHANNEL_ID2 = "2";
    private final static String NOTIFICATION_CHANNEL_NAME2 = "MyChannel2";
    private final static int SUMMARY_ID = 0;
    private final static String GROUP_KEY =
            "com.milnestmobile.androidlabs.notifications_dial_test.GROUP_KEY";
    private final static int mId = 1;
    private final static int mId2 = 2;
    private Button mNotifButton;
    private TextView mResTextView;
    private NotificationCompat.Builder mBuilder;
    private NotificationCompat.Builder mBuilder2;
    private NotificationCompat.Builder mBuilderSummary;
    private NotificationManager mNotificationManager;
    private MyDialogFragment mMyDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotifButton = (Button) findViewById(R.id.notifButton);
        mResTextView =(TextView) findViewById(R.id.resTextView);
        mMyDialogFragment = new MyDialogFragment();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        Intent resultIntent = new Intent(this, MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.createNotificationChannel(notificationChannel);
            NotificationChannel notificationChannel2 = new NotificationChannel(
                    CHANNEL_ID2, NOTIFICATION_CHANNEL_NAME2, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(notificationChannel2);
        }

         mBuilder= new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageText
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY);

        mBuilder2 = new NotificationCompat.Builder(this, CHANNEL_ID2)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(messageTitle2)
                .setContentText(messageText2)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY);

         mBuilderSummary =
                new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("Line 1")
                                .addLine("Line 2")
                                .setBigContentTitle("Some new messages")
                                .setSummaryText("Text"))
                        //Устанавливает группу ждя summary-уведомления
                        .setGroup(GROUP_KEY)
                        .setAutoCancel(true)
                        //Устанавливает это уведомление, как summary для группы
                        .setGroupSummary(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            //Отправляет первое уведомление и группу для него
            case R.id.notifButton:
                mNotificationManager.notify(mId, mBuilder.build());
                mNotificationManager.notify(SUMMARY_ID, mBuilderSummary.build());
                break;
            //Отправляет второе уведомление и группу(ту же, что и для 1-го) для него
            case R.id.notifButton2:
                mNotificationManager.notify(mId2, mBuilder2.build());
                mNotificationManager.notify(SUMMARY_ID, mBuilderSummary.build());
                break;
             //Вызывает диалог
            case R.id.dialogButton:
                mMyDialogFragment.show(getSupportFragmentManager(), "myDialog");
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mResTextView.setText("Positive clicked");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        mResTextView.setText("Negative clicked");
    }
}
