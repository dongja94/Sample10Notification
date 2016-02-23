package com.example.dongja94.samplenotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra("command", 0);
        String message;
        if (command == 0) {
            message = "back";
        } else if (command == 1) {
            message = "stop";
        } else {
            message = "next";
        }
        Toast.makeText(this, "command : " + message, Toast.LENGTH_SHORT).show();
        return Service.START_NOT_STICKY;
    }
}
