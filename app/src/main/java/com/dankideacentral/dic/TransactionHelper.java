package com.dankideacentral.dic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by srowhani on 9/25/16.
 */
public class TransactionHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private String name;
    private String[] columns;

    public TransactionHelper (Context context, String dbName, String tableName, String[] tableColumns) {
        super(context, dbName, null, DATABASE_VERSION);

        this.name = tableName;
        this.columns = tableColumns;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS " + this.name + " "
            + TextUtils.join(", ", this.columns));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
