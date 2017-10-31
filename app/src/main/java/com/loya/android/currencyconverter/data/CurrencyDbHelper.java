package com.loya.android.currencyconverter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ikhiloya on 10/10/2017.
 */

public class CurrencyDbHelper extends SQLiteOpenHelper {


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CurrencyContract.CurrencyEntry.TABLE1_NAME;
    private static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + CurrencyContract.CurrencyEntry.TABLE2_NAME;
    private static final String SQL_DELETE_ENTRIES3 = "DROP TABLE IF EXISTS " + CurrencyContract.CurrencyEntry.TABLE3_NAME;


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;

    /**
     * name of the Database file
     **/
    public static final String DATABASE_NAME = "currency.db";

    public CurrencyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE pets (id INTEGER PRIMARY KEY, name TEXT, weight INTEGER);
        //create a String that contains the SQL Entries
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CurrencyContract.CurrencyEntry.TABLE1_NAME + "("
                        + CurrencyContract.CurrencyEntry.BTC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOEUR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOUSD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTONGN + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTIMESTAMP + " TEXT NOT NULL);";

        String SQL_CREATE_ENTRIES2 =
                "CREATE TABLE " + CurrencyContract.CurrencyEntry.TABLE2_NAME + "("
                        + CurrencyContract.CurrencyEntry.ETH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOEUR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOUSD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTONGN + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTIMESTAMP + " TEXT NOT NULL);";

        String SQL_CREATE_ENTRIES3 =
                "CREATE TABLE " + CurrencyContract.CurrencyEntry.TABLE3_NAME + "("
                        + CurrencyContract.CurrencyEntry.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME + " TEXT NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME + " TEXT NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);
        db.execSQL(SQL_CREATE_ENTRIES3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES2);
        db.execSQL(SQL_DELETE_ENTRIES3);
        onCreate(db);

    }


}
