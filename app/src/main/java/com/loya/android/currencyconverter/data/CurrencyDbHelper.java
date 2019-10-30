package com.loya.android.currencyconverter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ikhiloya on 10/10/2017.
 * A database helper class to be used to set up the database
 */

public class CurrencyDbHelper extends SQLiteOpenHelper {

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CurrencyContract.CurrencyEntry.BTC_TO_OTHER;
    private static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + CurrencyContract.CurrencyEntry.ETH_TO_OTHER;
    private static final String SQL_DELETE_ENTRIES3 = "DROP TABLE IF EXISTS " + CurrencyContract.CurrencyEntry.USER_SELECTION;


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 8;

    /**
     * name of the Database file
     **/
    public static final String DATABASE_NAME = "currency.db";

    public CurrencyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a String that contains the SQL Entries for table 1
        String SQL_CREATE_ENTRIES_BTC_TO_OTHER =
                "CREATE TABLE " + CurrencyContract.CurrencyEntry.BTC_TO_OTHER + "("
                        + CurrencyContract.CurrencyEntry.BTC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOAUD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOCAD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOCHF + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOCNY + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTODKK + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOEUR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOGBP + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOINR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOJPY + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOKRW + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOMXN + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTONGN + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTONOK + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTONZD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTORUB + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOSAR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOSEK + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOSGD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOTRY + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTOUSD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_BTCTIMESTAMP + " TEXT NOT NULL);";


        //create a String that contains the SQL Entries for table 2
        String SQL_CREATE_ENTRIES_ETH_TO_OTHER =
                "CREATE TABLE " + CurrencyContract.CurrencyEntry.ETH_TO_OTHER + "("
                        + CurrencyContract.CurrencyEntry.ETH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOAUD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOCAD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOCHF + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOCNY + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTODKK + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOEUR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOGBP + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOINR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOJPY + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOKRW + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOMXN + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTONGN + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTONOK + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTONZD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTORUB + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOSAR + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOSEK + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOSGD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOTRY + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTOUSD + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_ETHTIMESTAMP + " TEXT NOT NULL);";

        //create a String that contains the SQL Entries for table 3
        String SQL_CREATE_ENTRIES_USER_SELECTION =
                "CREATE TABLE " + CurrencyContract.CurrencyEntry.USER_SELECTION + "("
                        + CurrencyContract.CurrencyEntry.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME + " TEXT NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME + " TEXT NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE + " REAL NOT NULL, "
                        + CurrencyContract.CurrencyEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL);";

        //execute sql
        db.execSQL(SQL_CREATE_ENTRIES_BTC_TO_OTHER);
        db.execSQL(SQL_CREATE_ENTRIES_ETH_TO_OTHER);
        db.execSQL(SQL_CREATE_ENTRIES_USER_SELECTION);
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
