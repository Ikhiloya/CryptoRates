package com.loya.android.currencyconverter.activites;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.loya.android.currencyconverter.R;
import com.loya.android.currencyconverter.adapter.SpinnerAdapter;
import com.loya.android.currencyconverter.data.CurrencyContract;
import com.loya.android.currencyconverter.data.CurrencyDbHelper;
import com.loya.android.currencyconverter.models.SpinnerModel;

import java.util.ArrayList;

/**
 * This Activity allows the user to make preferred currency selection
 */
public class CustomCurrency extends AppCompatActivity {
    //spinner variables
    private Spinner cryptoSpinner;
    private Spinner currencySpinner;

    //button variables
    private Button okButton;


    //a helper variable to be used to perform database operations
    private CurrencyDbHelper mDbHelper;

    //ArrayList variables
    private ArrayList<SpinnerModel> countryList;
    private ArrayList<SpinnerModel> cryptoList;
    //String variables to get the user selection from the spinner
    private String mCryptoUnit = null;
    private String mCurrencyUnit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_currency);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init variables
        cryptoSpinner = (Spinner) findViewById(R.id.crypto_spinner);
        currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
        okButton = (Button) findViewById(R.id.okButton);
        mDbHelper = new CurrencyDbHelper(this);

        //init arrayLists
        countryList = new ArrayList<>();
        cryptoList = new ArrayList<>();

        //populates @countryList
        countryList.add(new SpinnerModel("AUD", R.mipmap.icon_ic_australia));
        countryList.add(new SpinnerModel("CAD", R.mipmap.icon_ic_canada));
        countryList.add(new SpinnerModel("CHF", R.mipmap.icon_ic_switzerland));
        countryList.add(new SpinnerModel("CNY", R.mipmap.icon_ic_china));
        countryList.add(new SpinnerModel("DKK", R.mipmap.icon_ic_denmark));
        countryList.add(new SpinnerModel("EUR", R.mipmap.icon_ic_euro));
        countryList.add(new SpinnerModel("GBP", R.mipmap.icon_ic_gbp));
        countryList.add(new SpinnerModel("INR", R.mipmap.icon_ic_india));
        countryList.add(new SpinnerModel("JPY", R.mipmap.icon_ic_japan));
        countryList.add(new SpinnerModel("KRW", R.mipmap.icon_ic_southkorea));
        countryList.add(new SpinnerModel("MXN", R.mipmap.icon_ic_mexico));
        countryList.add(new SpinnerModel("NGN", R.mipmap.icon_ic_nigeria));
        countryList.add(new SpinnerModel("NOK", R.mipmap.icon_ic_norway));
        countryList.add(new SpinnerModel("NZD", R.mipmap.icon_ic_newzealand));
        countryList.add(new SpinnerModel("RUB", R.mipmap.icon_ic_russia));
        countryList.add(new SpinnerModel("SAR", R.mipmap.icon_ic_southafrica));
        countryList.add(new SpinnerModel("SEK", R.mipmap.icon_ic_sweden));
        countryList.add(new SpinnerModel("SGD", R.mipmap.icon_ic_singapore));
        countryList.add(new SpinnerModel("TRY", R.mipmap.icon_ic_turkey));
        countryList.add(new SpinnerModel("USD", R.mipmap.icon_ic_usa));


        //populates cryptoList
        cryptoList.add(new SpinnerModel("BTC", R.mipmap.btc));
        cryptoList.add(new SpinnerModel("ETH", R.mipmap.eth));

        //setup Spinners
        setUpCryptoSpinner();
        setUpCurrencySpinner();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryAndInsert();
                if (isDbEmpty()) {
                    Toast.makeText(CustomCurrency.this, "No exchange rate found, refresh to get current rates", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

    }

    /**
     * This method queries btc table(Table_1) and eth(Table_2) and insert user's selection in the user table(Table_3)
     */
    private void queryAndInsert() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //concatenates the user's selection from the spinner
        String colName = mCryptoUnit + "to" + mCurrencyUnit;

        if (!colName.startsWith("btc")) {
            String[] projection = {
                    CurrencyContract.CurrencyEntry.ETH_ID,
                    colName,
                    CurrencyContract.CurrencyEntry.COLUMN_ETHTIMESTAMP
            };
            // Perform  query on eth table
            Cursor cursor = db.query(
                    CurrencyContract.CurrencyEntry.TABLE2_NAME, //The table to query
                    projection,          //The column to return
                    null,                //The column for the WHERE clause
                    null,                //The values for the WHERE clause
                    null,                //don't group the rows
                    null,                //don't filter the row groups
                    null);               //The sort order
            try {
                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(colName);
                int timeColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_ETHTIMESTAMP);

                // Iterate through all the returned rows in the cursor
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    double cryptoRate = cursor.getDouble(nameColumnIndex);
                    String currentTime = cursor.getString(timeColumnIndex);

                    //insert the values into the user table (Table_3)
                    insertCurrency(mCryptoUnit, mCurrencyUnit, cryptoRate, currentTime);
                }
            } finally {
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                assert cursor != null;
                cursor.close();
            }
        } else {
            String[] projection = {
                    CurrencyContract.CurrencyEntry.BTC_ID,
                    colName,
                    CurrencyContract.CurrencyEntry.COLUMN_BTCTIMESTAMP
            };
            // Perform  query on BTC table
            Cursor cursor = db.query(
                    CurrencyContract.CurrencyEntry.TABLE1_NAME, //The table to query
                    projection,          //The column to return
                    null,                //The column for the WHERE clause
                    null,                //The values for the WHERE clause
                    null,                //don't group the rows
                    null,                //don't filter the row groups
                    null);               //The sort order
            try {

                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(colName);
                int timeColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_BTCTIMESTAMP);

                // Iterate through all the returned rows in the cursor
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    double cryptoRate = cursor.getDouble(nameColumnIndex);
                    String currentTime = cursor.getString(timeColumnIndex);

                    //insert the values into the user table (Table_3)
                    insertCurrency(mCryptoUnit, mCurrencyUnit, cryptoRate, currentTime);
                }
            } finally {
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                assert cursor != null;
                cursor.close();
            }
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the crypto currency.
     */
    private void setUpCryptoSpinner() {
        SpinnerAdapter cryptoAdapter = new SpinnerAdapter(CustomCurrency.this, cryptoList);
        cryptoSpinner.setAdapter(cryptoAdapter);

        // Set the integer mSelected to the constant values
        cryptoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerModel currentItem = cryptoList.get(position);
                if (currentItem != null) {
                    mCryptoUnit = currentItem.getCodes().toLowerCase();
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the Currencies.
     */
    private void setUpCurrencySpinner() {
        SpinnerAdapter currencyAdapter = new SpinnerAdapter(CustomCurrency.this, countryList);
        currencySpinner.setAdapter(currencyAdapter);

        // Set the integer mSelected to the constant values
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerModel currentItem = countryList.get(position);
                if (currentItem != null) {
                    mCurrencyUnit = currentItem.getCodes().toLowerCase();
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * @param cyrptoName   is the name of the user selected crypto currency
     * @param currencyName is the name of the user selected currency
     * @param value        is the exchange rate of the user selected currencies
     * @param currentTime  is the current time
     */
    private void insertCurrency(String cyrptoName, String currencyName, double value, String currentTime) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME, cyrptoName);
        values.put(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME, currencyName);
        values.put(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE, value);
        values.put(CurrencyContract.CurrencyEntry.COLUMN_TIMESTAMP, currentTime);
        //insert into db
        long rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE3_NAME, null, values);
    }

    /**
     * This method checks if the user table (Table_3) is empty
     *
     * @return true if the user table(Table_3) is empty
     */
    private boolean isDbEmpty() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                CurrencyContract.CurrencyEntry.USER_ID,
                CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME,
                CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME,
                CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE,
        };
        // Perform  query on USER table
        Cursor cursor = db.query(
                CurrencyContract.CurrencyEntry.TABLE3_NAME, //The table to query
                projection,          //The column to return
                null,                //The column for the WHERE clause
                null,                //The values for the WHERE clause
                null,                //don't group the rows
                null,                //don't filter the row groups
                null);               //The sort order
        try {
            if (cursor.getCount() == 0) {
                return true;
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            assert cursor != null;
            cursor.close();
        }
        return false;
    }
}
