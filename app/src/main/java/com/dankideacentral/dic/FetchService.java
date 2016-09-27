package com.dankideacentral.dic;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;


public class FetchService extends Service {
    TransactionHelper dbHelper;
    SQLiteDatabase db;
    Runnable mRunnable;

    public FetchService() {
        dbHelper = null;
        db = null;
        mRunnable = null;
    }

    public void onCreate() {
        super.onCreate();
        String [] columns = {"id INTEGER PRIMARY KEY", "key TEXT", "secret TEXT"};
        this.dbHelper = new TransactionHelper(
            getApplicationContext(),
            "store.db",
            "table_Credentials",
            columns
        );
        this.db = dbHelper.getReadableDatabase();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler mHandler = new Handler();
        final SQLiteDatabase db = this.db;
        this.mRunnable = new Runnable() {
            @Override
            public void run() {

                mHandler.postDelayed(mRunnable, 10 * 1000);
            }
        }
        mHandler.postDelayed(mRunnable, 10 * 1000);

        return super.onStartCommand(intent, flags, startId);
    }

}
