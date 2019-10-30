package com.ikhiloyaimokhai.currencyconverter.utils;

/**
 * Created by Ikhiloya on 10/30/2017.
 */

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;


import com.ikhiloyaimokhai.currencyconverter.R;

import org.json.JSONObject;

/**
 * Loads a JSONObject that contains currency conversion rates  by using an AsyncTask to perform the
 * network request to the given URL from CryptoCompare API.
 */
public class CurrencyLoader extends AsyncTaskLoader<JSONObject> {
    /**
     * Tag for log messages
     */
    private final String LOG_TAG = CurrencyLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link CurrencyLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public CurrencyLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.v(LOG_TAG, this.getContext().getString(R.string.in_onStartLoading));
        //force a new load
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public JSONObject loadInBackground() {
        Log.v(LOG_TAG, this.getContext().getString(R.string.in_LoadInBackground));
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a JSONObject
        JSONObject jsonObject = NetworkUtils.fetchJsonData(mUrl);
        return jsonObject;
    }
}