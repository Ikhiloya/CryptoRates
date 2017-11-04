package com.loya.android.currencyconverter.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loya.android.currencyconverter.data.CurrencyContract;
import com.loya.android.currencyconverter.data.CurrencyDbHelper;

/**
 * Created by Ikhiloya on 10/31/2017.
 * This Class uses AsyncTask loader to load user's selection from the background to the recycler view on the UI thread
 */

public class CursorLoader extends AsyncTaskLoader<Cursor> {
    /**
     * Tag for log messages
     */
    private final String LOG_TAG = CursorLoader.class.getName();


    private Context context;

    private CurrencyDbHelper mDbHelper;

    /**
     * @param context   the context of the calling activity
     * @param mDbHelper the database helper used to
     */
    public CursorLoader(Context context, CurrencyDbHelper mDbHelper) {
        super(context);
        this.context = context;
        this.mDbHelper = mDbHelper;

    }

    // Initialize a Cursor, this will hold all the task data
    Cursor cursor = null;

    // onStartLoading() is called when a loader first starts loading data
    @Override
    protected void onStartLoading() {
        if (cursor != null) {
            // Delivers any previously loaded data immediately
            deliverResult(cursor);
        } else {
            //force a new load
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                CurrencyContract.CurrencyEntry.USER_ID,
                CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME,
                CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME,
                CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE,
                CurrencyContract.CurrencyEntry.COLUMN_TIMESTAMP,
        };
        // Perform  query on user table
        try {
            return db.query(
                    CurrencyContract.CurrencyEntry.TABLE3_NAME, //The table to query
                    projection,          //The column to return
                    null,                //The column for the WHERE clause
                    null,                //The values for the WHERE clause
                    null,                //don't group the rows
                    null,                //don't filter the row groups
                    null);               //The sort order
        } catch (Exception e) {
            Log.e("TAG", "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    // deliverResult sends the result of the load, a Cursor, to the registered listener
    public void deliverResult(Cursor data) {
        cursor = data;
        super.deliverResult(data);
    }

}

