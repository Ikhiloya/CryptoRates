package com.loya.android.currencyconverter.Activites;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loya.android.currencyconverter.Adapter.CurrencyAdapter;
import com.loya.android.currencyconverter.R;
import com.loya.android.currencyconverter.Utils.CurrencyLoader;
import com.loya.android.currencyconverter.Utils.CursorLoader;
import com.loya.android.currencyconverter.Utils.Helper;
import com.loya.android.currencyconverter.data.CurrencyContract;
import com.loya.android.currencyconverter.data.CurrencyDbHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements CurrencyAdapter.ListItemClickListener {
    private CurrencyDbHelper mDbHelper;

    private String currentTime;

    private static final String REQUEST_URL = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=ETH,BTC&tsyms=USD,EUR,NGN";

    //LoaderManager.LoaderCallbacks objects to be used for background tasks
    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks;
    private LoaderManager.LoaderCallbacks<JSONObject> currencyLoaderCallbacks;

    //adapter to be used for the list view
    private CurrencyAdapter mCurrencyAdapter;
    //recycles view object
    private RecyclerView mRecycler;

    //integer Loader constant for the loader, could be any number
    private static final int DB_LOADER_ID = 0;
    private static final int CURRENCY_LOADER_ID = 1;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //set up recycler view
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setHasFixedSize(true);

        //set up adapter and attach to recycler view
        mCurrencyAdapter = new CurrencyAdapter(this, null, this);
        mRecycler.setAdapter(mCurrencyAdapter);

        //init the DbHelper object
        mDbHelper = new CurrencyDbHelper(this);

        //creates an object of LoaderCallbacks so as to perform Loading operation
        currencyLoaderCallbacks = new LoaderManager.LoaderCallbacks<JSONObject>() {
            @Override
            public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
                return new CurrencyLoader(MainActivity.this, REQUEST_URL);
            }

            @Override
            public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
                //gets current time
                currentTime = Helper.getCurrentTime();
                // If there is no result, do nothing.
                if (data == null) {
                    return;
                }
                //sends the JSONObject to update the database with the current rates
                updateDatabase(data);
            }

            @Override
            public void onLoaderReset(Loader<JSONObject> loader) {
            }
        };

        /**
         *
         *  Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         * created, otherwise the last created loader is re-used to fetch current rates from the CryptoCompare API
         *
         */
        getSupportLoaderManager().initLoader(CURRENCY_LOADER_ID, null, currencyLoaderCallbacks);


        //creates an object of LoaderCallbacks so as to perform Loading operation
        cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(MainActivity.this, mDbHelper);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                // Update the data that the adapter uses to create ViewHolders
                mCurrencyAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                // Update the data that the adapter uses to create ViewHolders
                mCurrencyAdapter.swapCursor(null);
            }
        };


        /** Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         * and uses callbacks to signal when a user is performing these actions.
         * */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                //test
                Toast.makeText(MainActivity.this, "" + id, Toast.LENGTH_LONG).show();
                showDeleteConfirmationDialog(id);
                // Restart the loader to re-query for all tasks after a deletion
                getSupportLoaderManager().restartLoader(DB_LOADER_ID, null, cursorLoaderCallbacks);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mRecycler);


        /**
         * Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(DB_LOADER_ID, null, cursorLoaderCallbacks);

        //sets a click listener on the FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomCurrency.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Prompts the user to confirm that they want to delete the current card when swiped.
     */
    private void showDeleteConfirmationDialog(final int currentItem) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_this_entry);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the entry.
                deleteCurrency(currentItem);
                //restarts the loader so as to get current data
                getSupportLoaderManager().restartLoader(DB_LOADER_ID, null, cursorLoaderCallbacks);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * This method deletes the current card from  the database based on the id
     *
     * @param id the current id of the card selected
     */
    private void deleteCurrency(int id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = CurrencyContract.CurrencyEntry.USER_ID + "=?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
        // Issue SQL statement.
        db.delete(CurrencyContract.CurrencyEntry.TABLE3_NAME, selection, selectionArgs);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //restarts the loader so as to get updated currencies
        getSupportLoaderManager().restartLoader(DB_LOADER_ID, null, cursorLoaderCallbacks);
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


    /**
     * Update the Database with the current Conversion Rates information from the CryptoCompare API.
     */
    private void updateDatabase(JSONObject jsonObject) {
        //a SQLiteDatabase object used to delete items fro the database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //delete the existing currencies in the Database so as to have only current conversion rates in the Database
        db.delete(CurrencyContract.CurrencyEntry.TABLE1_NAME, null, null);
        db.delete(CurrencyContract.CurrencyEntry.TABLE2_NAME, null, null);
        try {
            //get ETH conversion Rates
            JSONObject ethJsonObject = jsonObject.getJSONObject("ETH");
            double usd = ethJsonObject.getDouble("USD");
            double eur = ethJsonObject.getDouble("EUR");
            double ngn = ethJsonObject.getDouble("NGN");

            //get BTC conversion Rates
            JSONObject btcJsonObject = jsonObject.getJSONObject("BTC");
            double usd1 = btcJsonObject.getDouble("USD");
            double eur1 = btcJsonObject.getDouble("EUR");
            double ngn1 = btcJsonObject.getDouble("NGN");

            //content value object to insert the fetched data to the database
            ContentValues btcValues = new ContentValues();
            btcValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOEUR, eur1);
            btcValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOUSD, usd1);
            btcValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTONGN, ngn1);
            btcValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTIMESTAMP, currentTime);

            //content value object to insert the fetched data to the database
            ContentValues ethValues = new ContentValues();
            ethValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOEUR, eur);
            ethValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOUSD, usd);
            ethValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTONGN, ngn);
            ethValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTIMESTAMP, currentTime);

            //test
            long table1_rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE1_NAME, null, btcValues);
            Toast.makeText(MainActivity.this, "inserted in table 1 with Row id: " + table1_rowId, Toast.LENGTH_LONG).show();
            //test
            long table2_rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE2_NAME, null, ethValues);
            Toast.makeText(MainActivity.this, "inserted in table 2 with Row id: " + table2_rowId, Toast.LENGTH_LONG).show();

            Log.v("USD:", String.valueOf(usd));
            Log.v("EUR:", String.valueOf(eur));
            Log.v("NGN", String.valueOf(ngn));


            Log.v("USD1:", String.valueOf(usd1));
            Log.v("EUR1:", String.valueOf(eur1));
            Log.v("NGN1", String.valueOf(ngn1));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onListItemClick(String cryptoName, String currencyName, double currencyValue) {
        //test
        if (mToast != null) {
            mToast.cancel();
        }
        String message = "Crypto Name:" + cryptoName + " \nCurrency Name: " + currencyName + "\nCurrency Value: " + currencyValue;
        mToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        mToast.show();

        //passes data to the next activity
        Intent convertIntent = new Intent(MainActivity.this, ConvertCurrency.class);
        convertIntent.putExtra("cryptoName", cryptoName);
        convertIntent.putExtra("currencyName", currencyName);
        convertIntent.putExtra("currencyValue", currencyValue);
        startActivity(convertIntent);
    }

}
