package com.loya.android.currencyconverter.Activites;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loya.android.currencyconverter.Adapter.SpinnerAdapter;
import com.loya.android.currencyconverter.Models.SpinnerModel;
import com.loya.android.currencyconverter.R;
import com.loya.android.currencyconverter.data.CurrencyContract;
import com.loya.android.currencyconverter.data.CurrencyDbHelper;

import java.util.ArrayList;

public class CustomCurrency extends AppCompatActivity {

    private Spinner cryptoSpinner;
    private Spinner currencySpinner;
    private Button okButton;


    private CurrencyDbHelper mDbHelper;

    private Button displayUserBtn;


    private ArrayList<SpinnerModel> countryList;
    private ArrayList<SpinnerModel> cryptoList;
    private String mCryptoUnit = null;
    private String mCurrencyUnit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_currency);
        cryptoSpinner = (Spinner) findViewById(R.id.crypto_spinner);
        currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
        okButton = (Button) findViewById(R.id.okButton);

        //test
        displayUserBtn = (Button) findViewById(R.id.userButton);


        mDbHelper = new CurrencyDbHelper(this);

        //init arrayList
        countryList = new ArrayList<>();
        cryptoList = new ArrayList<>();
        //populates @countryList
        countryList.add(new SpinnerModel("EUR", R.mipmap.ic_launcher));
        countryList.add(new SpinnerModel("USD", R.mipmap.ic_launcher));
        countryList.add(new SpinnerModel("NGN", R.mipmap.ic_launcher));

        //populates @cryptoList
        cryptoList.add(new SpinnerModel("BTC", R.mipmap.btc));
        cryptoList.add(new SpinnerModel("ETH", R.mipmap.eth));

        //setup Spinners
        setUpCryptoSpinner();
        setUpCurrencySpinner();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryAndInsert();
                finish();
            }
        });

        //test
        displayUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayUserTable();
            }
        });
    }

    private void queryAndInsert() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //concatenates the user's selection frm the spinner
        String colName = mCryptoUnit + "to" + mCurrencyUnit;
        Toast.makeText(CustomCurrency.this, colName, Toast.LENGTH_LONG).show();

        if (!colName.startsWith("btc")) {

            String[] projection = {
                    CurrencyContract.CurrencyEntry.ETH_ID,
                    colName,
                    CurrencyContract.CurrencyEntry.COLUMN_ETHTIMESTAMP
            };
            // Perform  query on pets table
            Cursor cursor = db.query(
                    CurrencyContract.CurrencyEntry.TABLE2_NAME, //The table to query
                    projection,          //The column to return
                    null,                //The column for the WHERE clause
                    null,                //The values for the WHERE clause
                    null,                //don't group the rows
                    null,                //don't filter the row groups
                    null);               //The sort order


            try {

                TextView displayView = (TextView) findViewById(R.id.displayTv);

                displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
                displayView.append(CurrencyContract.CurrencyEntry.ETH_ID + " - " +
                        CurrencyContract.CurrencyEntry.COLUMN_ETHTOEUR + " - " + CurrencyContract.CurrencyEntry.COLUMN_ETHTOUSD + " - " +
                        CurrencyContract.CurrencyEntry.COLUMN_ETHTONGN + "\n");


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

                    insertCurrency(mCryptoUnit, mCurrencyUnit, cryptoRate, currentTime);

                    // Display the values from each column of the current row in the cursor in the TextView
                    displayView.append(("\n" + currentID + " - " +
                            cryptoRate + " - "));
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

                TextView displayView = (TextView) findViewById(R.id.displayTv);

                displayView.setText("The table contains " + cursor.getCount() + " pets.\n\n");
                displayView.append(CurrencyContract.CurrencyEntry.BTC_ID + " - " +
                        CurrencyContract.CurrencyEntry.COLUMN_BTCTOEUR + " - " + CurrencyContract.CurrencyEntry.COLUMN_BTCTOUSD + " - " +
                        CurrencyContract.CurrencyEntry.COLUMN_BTCTONGN + "\n");


                //dummy test data
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

                    //insert the values into the user's table (Table_3)
                    insertCurrency(mCryptoUnit, mCurrencyUnit, cryptoRate, currentTime);

                    // Display the values from each column of the current row in the cursor in the TextView
                    displayView.append(("\n" + currentID + " - " +
                            cryptoRate + " - "));
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
                //  mCryptoUnit = "BTC";
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
                //  mCryptoUnit = "BTC";
            }
        });
    }


    private void insertCurrency(String cyrptoName, String currencyName, double value, String currentTime) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME, cyrptoName);
        values.put(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME, currencyName);
        values.put(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE, value);
        values.put(CurrencyContract.CurrencyEntry.COLUMN_TIMESTAMP, currentTime);

        //test
        long rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE3_NAME, null, values);
        Toast.makeText(CustomCurrency.this, "inserted in table 3 with Row id: " + rowId, Toast.LENGTH_LONG).show();
    }


    //test method
    private void displayUserTable() {
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

            TextView displayView = (TextView) findViewById(R.id.displayTv);

            displayView.setText("The user table contains " + cursor.getCount() + " currencies.\n\n");
            displayView.append(CurrencyContract.CurrencyEntry.USER_ID + " - " +
                    CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME + " - " + CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME + " - " +
                    CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE + "\n");


            //dummy test data
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.USER_ID);
            int nameColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME);
            int breedColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME);
            int genderColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE);


            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String crypto_name = cursor.getString(nameColumnIndex);
                String currency_name = cursor.getString(breedColumnIndex);
                double currency_val = cursor.getDouble(genderColumnIndex);


                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        crypto_name + " - " + currency_name + " - " + currency_val));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            assert cursor != null;
            cursor.close();
        }

    }


}
