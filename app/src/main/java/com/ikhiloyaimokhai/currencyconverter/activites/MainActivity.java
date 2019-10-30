package com.ikhiloyaimokhai.currencyconverter.activites;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.ikhiloyaimokhai.currencyconverter.R;
import com.ikhiloyaimokhai.currencyconverter.adapter.CurrencyAdapter;
import com.ikhiloyaimokhai.currencyconverter.adapter.EmptyRecyclerView;
import com.ikhiloyaimokhai.currencyconverter.data.CurrencyContract;
import com.ikhiloyaimokhai.currencyconverter.data.CurrencyDbHelper;
import com.ikhiloyaimokhai.currencyconverter.listeners.RecyclerClickListener;
import com.ikhiloyaimokhai.currencyconverter.listeners.RecyclerTouchListener;
import com.ikhiloyaimokhai.currencyconverter.utils.Constant;
import com.ikhiloyaimokhai.currencyconverter.utils.CurrencyLoader;
import com.ikhiloyaimokhai.currencyconverter.utils.CursorLoader;
import com.ikhiloyaimokhai.currencyconverter.utils.Helper;
import com.ikhiloyaimokhai.currencyconverter.utils.Toolbar_ActionMode_Callback;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements CurrencyAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private CurrencyDbHelper mDbHelper;

    private String currentTime;

    //LoaderManager.LoaderCallbacks objects to be used for background tasks
    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks;
    private LoaderManager.LoaderCallbacks<JSONObject> currencyLoaderCallbacks;

    //adapter to be used for the recycler view
    private CurrencyAdapter mCurrencyAdapter;
    //EmptyRecyclerView  variable
    private EmptyRecyclerView mRecycler;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    //integer Loader constant for the loader, could be any integer
    private static final int DB_LOADER_ID = 0;
    private static final int CURRENCY_LOADER_ID = 1;

    //Action made variable used to activate the contextual Action Mode
    private ActionMode mActionMode;

    //variable used to check for the connection state of the device
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
    //view for the loading indicator
    private View loadingIndicator;

    //boolean variables
    private boolean isConnected;
    private boolean isRefresh;

    private Snackbar messageSnackbar;
    private CoordinatorLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isRefresh = false;
        final FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.addFab);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        //get a reference to the loading indicator
        loadingIndicator = findViewById(R.id.loading_indicator);
        //set up recycler view
        mRecycler = (EmptyRecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setHasFixedSize(true);

        // Fetch the empty view from the layout and set it on the new recycler view
        View emptyView = findViewById(R.id.todo_list_empty_view);
        mRecycler.setEmptyView(emptyView);

        //set up adapter and attach to recycler view
        mCurrencyAdapter = new CurrencyAdapter(this, null, this);
        mRecycler.setAdapter(mCurrencyAdapter);

        //init the DbHelper object to be used to perform database operations
        mDbHelper = new CurrencyDbHelper(this);

        //creates an object of LoaderCallbacks so as to perform Loading operation
        currencyLoaderCallbacks = new LoaderManager.LoaderCallbacks<JSONObject>() {
            @Override
            public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
                if (isRefresh) {
                    showLoading();
                }
                return new CurrencyLoader(MainActivity.this, Constant.BASE_URL);
            }

            @Override
            public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
                //gets current time at which the data was fetched
                currentTime = Helper.getCurrentTime();
                dismissLoading();
                // If there is no result, do nothing.
                if (data == null) {
                    Toast.makeText(MainActivity.this, R.string.could_not_get_exchange_rate, Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                if (isRefresh) {
                    Toast.makeText(MainActivity.this, R.string.current_exchange_rates_fetched_successfully, Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                //sends the JSONObject to update the database with the current rates
                Helper.updateDatabase(data, MainActivity.this, currentTime);
                isRefresh = false;
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
            //init loader
            getSupportLoaderManager().initLoader(CURRENCY_LOADER_ID, null, currencyLoaderCallbacks);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }

        //creates an object of LoaderCallbacks so as to perform Loading operation on the database
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
        //implement the listeners to handle clicks and contextual action mode
        implementRecyclerViewClickListeners();
        //sets a click listener on the FAB
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomCurrency.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);


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

    //recycler item select method
    private void onListItemSelect(int position) {
        mCurrencyAdapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = mCurrencyAdapter.getSelectedCount() > 0;//Check if any items are already selected or not

        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
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
        //restarts the loader so as to get user selected cards from the database
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
            getCurrentRates();
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

    private void getCurrentRates() {
        isRefresh = true; //set to true before restarting the loader
        //restart the loader to get current exchange rates from cryptoCompare API
        getSupportLoaderManager().restartLoader(CURRENCY_LOADER_ID, null, currencyLoaderCallbacks);
    }

    //Delete selected rows
    public void deleteRows() {
        SparseBooleanArray selected = mCurrencyAdapter.getSelectedIds();//Get selected ids
        showDeleteConfirmationDialog(selected);
    }

    /**
     * Prompts the user to confirm that they want to delete the current card
     */
    private void showDeleteConfirmationDialog(final SparseBooleanArray selected) {
        // Create an AlertDialog.Builder and set the message,
        //  and click listeners for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (selected.size() > 1) {
            builder.setMessage(getString((R.string.delete)) + " " + selected.size() + " " + getString(R.string.cards));
        } else {
            builder.setMessage(getString(R.string.delete_this_card));
        }
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the card.
                //Loop all selected ids
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        //If current id is selected remove the item via key---delete each item in db
                        deleteCurrency(selected.keyAt(i));
                        mCurrencyAdapter.notifyDataSetChanged();//notify adapter
                    }
                }
                if (selected.size() > 1) {
                    Toast.makeText(MainActivity.this, selected.size() + "  " + getString(R.string.cards_deleted), Toast.LENGTH_SHORT).show();//Show Toast

                } else {
                    Toast.makeText(MainActivity.this, selected.size() + "  " + getString(R.string.card_deleted), Toast.LENGTH_SHORT).show();//Show Toast

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

    /**
     * Prompts the user to confirm that they want to all current cards
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_cards);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete all cards.
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
        db.delete(CurrencyContract.CurrencyEntry.USER_SELECTION, selection, selectionArgs);
    }

    /**
     * This deletes all cards in the database
     */
    private void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(CurrencyContract.CurrencyEntry.USER_SELECTION, null, null);
    }

    @Override
    public void onListItemClick(String cryptoName, String currencyName, double currencyValue) {
        //passes data to the next activity
        Intent convertIntent = new Intent(MainActivity.this, ConvertCurrency.class);
        convertIntent.putExtra(Constant.CRYPTO_NAME, cryptoName);
        convertIntent.putExtra(Constant.CURRENCY_NAME, currencyName);
        convertIntent.putExtra(Constant.CURRENCY_VALUE, currencyValue);
        startActivity(convertIntent);
    }

    /**
     * shows the progress bar
     */
    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * dismisses the progress bar
     */
    private void dismissLoading() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        getCurrentRates();
    }

}
