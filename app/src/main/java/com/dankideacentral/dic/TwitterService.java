package com.dankideacentral.dic;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;


public class TwitterService extends Service {
    public void onCreate() {
        super.onCreate();
        TransactionHelper dbHelper;
        String [] columns = {"id INTEGER PRIMARY KEY", "key TEXT", "secret TEXT"};
        dbHelper = new TransactionHelper(getApplicationContext(), "store.db", "credentials", columns);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
