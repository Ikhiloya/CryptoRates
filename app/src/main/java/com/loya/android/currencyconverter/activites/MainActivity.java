package com.loya.android.currencyconverter.activites;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loya.android.currencyconverter.R;
import com.loya.android.currencyconverter.adapter.CurrencyAdapter;
import com.loya.android.currencyconverter.adapter.EmptyRecyclerView;
import com.loya.android.currencyconverter.data.CurrencyContract;
import com.loya.android.currencyconverter.data.CurrencyDbHelper;
import com.loya.android.currencyconverter.listeners.RecyclerClickListener;
import com.loya.android.currencyconverter.listeners.RecyclerTouchListener;
import com.loya.android.currencyconverter.utils.CurrencyLoader;
import com.loya.android.currencyconverter.utils.CursorLoader;
import com.loya.android.currencyconverter.utils.Helper;
import com.loya.android.currencyconverter.utils.Toolbar_ActionMode_Callback;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements CurrencyAdapter.ListItemClickListener {
    private CurrencyDbHelper mDbHelper;

    private String currentTime;

    private static final String REQUEST_URL = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=ETH,BTC&tsyms=AUD,CAD,CHF,CNY,DKK,EUR,GBP,INR,JPY,KRW,MXN,NGN,NOK,NZD,RUB,SAR,SEK,SGD,TRY,USD";

    //LoaderManager.LoaderCallbacks objects to be used for background tasks
    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks;
    private LoaderManager.LoaderCallbacks<JSONObject> currencyLoaderCallbacks;

    //adapter to be used for the list view
    private CurrencyAdapter mCurrencyAdapter;
    //recycles view object
    //  private RecyclerView mRecycler;

    private EmptyRecyclerView mRecycler;


    //integer Loader constant for the loader, could be any number
    private static final int DB_LOADER_ID = 0;
    private static final int CURRENCY_LOADER_ID = 1;

    private Toast mToast;

    private ActionMode mActionMode;


    private ConnectivityManager cm;

    private boolean isConnected;

    private NetworkInfo activeNetwork;
    //view for the loading indicator
    View loadingIndicator;

    private boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isRefresh = false;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //get a reference to the loading indicator
        loadingIndicator = findViewById(R.id.loading_indicator);
        //set up recycler view
        mRecycler = (EmptyRecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setHasFixedSize(true);

        // Fetch the empty view from the layout and set it on
        // the new recycler view
        View emptyView = findViewById(R.id.todo_list_empty_view);
        mRecycler.setEmptyView(emptyView);

        //set up adapter and attach to recycler view
        mCurrencyAdapter = new CurrencyAdapter(this, null, this);
        mRecycler.setAdapter(mCurrencyAdapter);

        //init the DbHelper object
        mDbHelper = new CurrencyDbHelper(this);


        //creates an object of LoaderCallbacks so as to perform Loading operation
        currencyLoaderCallbacks = new LoaderManager.LoaderCallbacks<JSONObject>() {
            @Override
            public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
                if (isRefresh == true) {
                    showLoading();
                }

                return new CurrencyLoader(MainActivity.this, REQUEST_URL);
            }

            @Override
            public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
                //gets current time
                currentTime = Helper.getCurrentTime();
                dismissLoading();
                // If there is no result, do nothing.
                if (data == null) {
                    Toast.makeText(MainActivity.this, R.string.could_not_get_exchange_rate, Toast.LENGTH_LONG).show();
                    return;
                }
                if (isRefresh == true) {
                    Toast.makeText(MainActivity.this, R.string.current_exchange_rates_fetched_successfully, Toast.LENGTH_LONG).show();
                }
                //sends the JSONObject to update the database with the current rates
                updateDatabase(data);
            }

            @Override
            public void onLoaderReset(Loader<JSONObject> loader) {
            }
        };

        //check if there is a network connection
        // if there is a network connection the LoaderManager is called but
        //  displays a message if there's no network connection
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            /**
             *
             *  Ensure a loader is initialized and active. If the loader doesn't already exist, one is
             * created, otherwise the last created loader is re-used to fetch current rates from the CryptoCompare API
             *
             */
            getSupportLoaderManager().initLoader(CURRENCY_LOADER_ID, null, currencyLoaderCallbacks);
        } else {
            loadingIndicator.setVisibility(View.GONE);
//            mEmptyStateTextView.setText(R.string.no_internet_connection);
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }


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
        /**
         * Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(DB_LOADER_ID, null, cursorLoaderCallbacks);
        implementRecyclerViewClickListeners();
        //sets a click listener on the FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomCurrency.class);
                startActivity(intent);
            }
        });
    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        mRecycler.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, mRecycler, new RecyclerClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder viewHolder) {
                //If ActionMode not null select item
                if (mActionMode != null) {
                    onListItemSelect((int) viewHolder.itemView.getTag());
                }
            }

            @Override
            public void onLongClick(View view, RecyclerView.ViewHolder viewHolder) {
                //Select item on long click
                onListItemSelect((int) viewHolder.itemView.getTag());
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        mCurrencyAdapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = mCurrencyAdapter.getSelectedCount() > 0;//Check if any items are already selected or not

        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
            // Toolbar_ActionMode_Callback actionModeCallBack = new Toolbar_ActionMode_Callback(MainActivity.this, mCurrencyAdapter);
            mActionMode = startSupportActionMode(new Toolbar_ActionMode_Callback(MainActivity.this, mCurrencyAdapter));

        } else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
        }
        if (mActionMode != null) {
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(mCurrencyAdapter.getSelectedCount()) + " selected");
        }
    }

    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null) {
            mActionMode = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //restarts the loader so as to get user selected cards

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
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_get_current_rates) {
            isRefresh = true;
            //restart the loader to get current exchange rates from cryptoCompare API
            getSupportLoaderManager().restartLoader(CURRENCY_LOADER_ID, null, currencyLoaderCallbacks);
            return true;
        } else if (id == R.id.action_delete_all_cards) {
            if (mCurrencyAdapter.getItemCount() > 0) {
                showDeleteConfirmationDialog();
            } else {
                Toast.makeText(MainActivity.this, R.string.no_card_to_delete, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Delete selected rows---handle delete from db
    public void deleteRows() {
        SparseBooleanArray selected = mCurrencyAdapter.getSelectedIds();//Get selected ids
        showDeleteConfirmationDialog(selected);
    }


    /**
     * Prompts the user to confirm that they want to delete the current card
     */
    private void showDeleteConfirmationDialog(final SparseBooleanArray selected) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (selected.size() > 1) {
            builder.setMessage(getString((R.string.delete)) + " " + selected.size() + getString(R.string.cards));
        } else {
            builder.setMessage(getString(R.string.delete_this_card));
        }
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the entry.
                //Loop all selected ids
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        //If current id is selected remove the item via key---delete each item in db
                        deleteCurrency(selected.keyAt(i));
                        //   Toast.makeText(MainActivity.this, selected.size() + " entries deleted", Toast.LENGTH_SHORT).show();
                        mCurrencyAdapter.notifyDataSetChanged();//notify adapter
                    }
                }
                if (selected.size() > 1) {
                    Toast.makeText(MainActivity.this, selected.size() + " " + getString(R.string.cards_deleted), Toast.LENGTH_SHORT).show();//Show Toast
                } else {
                    Toast.makeText(MainActivity.this, selected.size() + " " + getString(R.string.card_deleted), Toast.LENGTH_SHORT).show();//Show Toast
                }
                mActionMode.finish();//Finish action mode after use
                //restarts the loader so as to get current data
                getSupportLoaderManager().restartLoader(DB_LOADER_ID, null, cursorLoaderCallbacks);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                    mActionMode.finish();
                    setNullToActionMode();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_cards);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAll();
                Toast.makeText(MainActivity.this, R.string.entries_deleted, Toast.LENGTH_LONG).show();
                getSupportLoaderManager().restartLoader(DB_LOADER_ID, null, cursorLoaderCallbacks);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
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
     * This method deletes the current card from the database based on the id
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

    private void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(CurrencyContract.CurrencyEntry.TABLE3_NAME, null, null);
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
            double aud = ethJsonObject.getDouble("AUD");
            double cad = ethJsonObject.getDouble("CAD");
            double chf = ethJsonObject.getDouble("CHF");
            double cny = ethJsonObject.getDouble("CNY");
            double dkk = ethJsonObject.getDouble("DKK");
            double eur = ethJsonObject.getDouble("EUR");
            double gbp = ethJsonObject.getDouble("GBP");
            double inr = ethJsonObject.getDouble("INR");
            double jpy = ethJsonObject.getDouble("JPY");
            double krw = ethJsonObject.getDouble("KRW");
            double mxn = ethJsonObject.getDouble("MXN");
            double ngn = ethJsonObject.getDouble("NGN");
            double nok = ethJsonObject.getDouble("NOK");
            double nzd = ethJsonObject.getDouble("NZD");
            double rub = ethJsonObject.getDouble("RUB");
            double sar = ethJsonObject.getDouble("SAR");
            double sek = ethJsonObject.getDouble("SEK");
            double sgd = ethJsonObject.getDouble("SGD");
            double atry = ethJsonObject.getDouble("TRY");
            double usd = ethJsonObject.getDouble("USD");


            //get BTC conversion Rates
            JSONObject btcJsonObject = jsonObject.getJSONObject("BTC");
            double aud1 = btcJsonObject.getDouble("AUD");
            double cad1 = btcJsonObject.getDouble("CAD");
            double chf1 = btcJsonObject.getDouble("CHF");
            double cny1 = btcJsonObject.getDouble("CNY");
            double dkk1 = btcJsonObject.getDouble("DKK");
            double eur1 = btcJsonObject.getDouble("EUR");
            double gbp1 = btcJsonObject.getDouble("GBP");
            double inr1 = btcJsonObject.getDouble("INR");
            double jpy1 = btcJsonObject.getDouble("JPY");
            double krw1 = btcJsonObject.getDouble("KRW");
            double mxn1 = btcJsonObject.getDouble("MXN");
            double ngn1 = btcJsonObject.getDouble("NGN");
            double nok1 = btcJsonObject.getDouble("NOK");
            double nzd1 = btcJsonObject.getDouble("NZD");
            double rub1 = btcJsonObject.getDouble("RUB");
            double sar1 = btcJsonObject.getDouble("SAR");
            double sek1 = btcJsonObject.getDouble("SEK");
            double sgd1 = btcJsonObject.getDouble("SGD");
            double try1 = btcJsonObject.getDouble("TRY");
            double usd1 = btcJsonObject.getDouble("USD");

            //content value object to insert the fetched data to the database
            ContentValues ethContentValues = new ContentValues();
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOAUD, aud);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOCAD, cad);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOCHF, chf);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOCNY, cny);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTODKK, dkk);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOEUR, eur);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOGBP, gbp);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOINR, inr);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOJPY, jpy);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOKRW, krw);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOMXN, mxn);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTONGN, ngn);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTONOK, nok);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTONZD, nzd);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTORUB, rub);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOSAR, sar);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOSEK, sek);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOSGD, sgd);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOTRY, atry);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTOUSD, usd);
            ethContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_ETHTIMESTAMP, currentTime);


            //content value object to insert the fetched data to the database
            ContentValues btcContentValues = new ContentValues();
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOAUD, aud1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOCAD, cad1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOCHF, chf1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOCNY, cny1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTODKK, dkk1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOEUR, eur1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOGBP, gbp1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOINR, inr1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOJPY, jpy1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOKRW, krw1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOMXN, mxn1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTONGN, ngn1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTONOK, nok1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTONZD, nzd1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTORUB, rub1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOSAR, sar1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOSEK, sek1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOSGD, sgd1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOTRY, try1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTOUSD, usd1);
            btcContentValues.put(CurrencyContract.CurrencyEntry.COLUMN_BTCTIMESTAMP, currentTime);


            //test
            long table2_rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE2_NAME, null, ethContentValues);
            Toast.makeText(MainActivity.this, "inserted in table 2 with Row id: " + table2_rowId, Toast.LENGTH_LONG).show();

            //test
            long table1_rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE1_NAME, null, btcContentValues);
            Toast.makeText(MainActivity.this, "inserted in table 1 with Row id: " + table1_rowId, Toast.LENGTH_LONG).show();

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

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void dismissLoading() {
        loadingIndicator.setVisibility(View.GONE);
    }
}
