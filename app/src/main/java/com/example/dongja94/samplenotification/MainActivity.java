package com.example.dongja94.samplenotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    NotificationManager mNM;

    EditText inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        inputView = (EditText)findViewById(R.id.edit_message);
        Button btn = (Button)findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        btn = (Button)findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });

        btn = (Button)findViewById(R.id.btn_progress);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();
            }
        });

        btn = (Button)findViewById(R.id.btn_big_text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
                style.setBigContentTitle("TextView....");
                style.setSummaryText("summary....");
                style.bigText("A notification is a message you can display to the user outside of your application's normal UI. When you tell the system to issue a notification, it first appears as an icon in the notification area. To see the details of the notification, the user opens the notification drawer. Both the notification area and the notification drawer are system-controlled areas that the user can view at any time.");
                sendNotification(style);
            }
        });
        mNM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

    }

    Handler mHander = new Handler();

    private void startProgress() {
        mHander.removeCallbacks(progressRunnable);
        progress = 0;
        mHander.post(progressRunnable);
    }

    int progress = 0;
    int mProgressId = 100;

    Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (progress <= 100) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                builder.setTicker("download progress");
                builder.setContentTitle("a file download...");
                builder.setContentText("download : " + progress);
                builder.setProgress(100, progress, false);
                builder.setOngoing(true);
                builder.setOnlyAlertOnce(true);
                Notification notification = builder.build();

                mNM.notify(mProgressId, notification);

                progress+=5;

                mHander.postDelayed(this, 500);
            } else {
                mNM.cancel(mProgressId);
            }
        }

    };

    int mId = 0;

    private void sendNotification() {
        sendNotification(null);
    }

    private void sendNotification(NotificationCompat.Style style) {
        mId++;
        String message = inputView.getText().toString();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Notification Ticker....");
        builder.setContentTitle("Content Title..." + mId);
        builder.setContentText("Content Text..." + message);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
        if (style != null) {
            builder.setStyle(style);
        }
        Intent[] intents = new Intent[2];
        intents[0] = new Intent(this, MainActivity.class);
        intents[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent intent = new Intent(this, NotifyActivity.class);
        intent.setData(Uri.parse("myscheme://com.example.dongja94.samplenotification/" + mId));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(NotifyActivity.EXTRA_MESSAGE, message);
        intents[1] = intent;
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pi = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Notification notification = builder.build();
        mNM.notify(mId, notification);
    }

    private void cancelNotification() {
        mNM.cancel(mId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
