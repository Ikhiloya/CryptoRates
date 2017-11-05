package com.loya.android.currencyconverter.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loya.android.currencyconverter.R;
import com.loya.android.currencyconverter.utils.Helper;

public class ConvertCurrency extends AppCompatActivity {
    //variables
    private EditText cryptoTextField;
    private EditText currencyTextField;
    private Button convertButton;
    private String cryptoName, currencyName;
    private double currencyValue;
    private int cryptoIcon;
    private int currencyIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_currency);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init views
        cryptoTextField = (EditText) findViewById(R.id.cryptoField);
        currencyTextField = (EditText) findViewById(R.id.currencyField);
        convertButton = (Button) findViewById(R.id.convertButton);

        //get extras from intent
        cryptoName = getIntent().getStringExtra("cryptoName");
        currencyName = getIntent().getStringExtra("currencyName");
        currencyValue = getIntent().getDoubleExtra("currencyValue", 0);

        // get drawable icon from the Helper class
        cryptoIcon = Helper.getCryptoIcon(cryptoName);
        currencyIcon = Helper.getCurrencyIcon(currencyName);

        //set drawable icon based on user's selection
        cryptoTextField.setCompoundDrawablesWithIntrinsicBounds(cryptoIcon, 0, 0, 0);
        currencyTextField.setCompoundDrawablesWithIntrinsicBounds(currencyIcon, 0, 0, 0);

        //set hint on the edit text fields
        cryptoTextField.setHint(cryptoName.toUpperCase());
        currencyTextField.setHint(currencyName.toUpperCase());

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert();
            }
        });
    }

    /**
     * method to convert from crypto to currency and vice versa
     */
    private void convert() {
        String cryptoVal = cryptoTextField.getText().toString().trim();
        String currencyVal = currencyTextField.getText().toString().trim();

        if (TextUtils.isEmpty(cryptoVal) && TextUtils.isEmpty(currencyVal)) {
            return;
        } else if (TextUtils.isEmpty(currencyVal)) {
            convertToCurrency(Double.parseDouble(cryptoVal));
        } else {
            convertToCrypto(Double.parseDouble(currencyVal));
        }
    }

    /**
     * method to convert from a crypto currency to other currencies
     *
     * @param cryptoVal the current value of the crypto currency
     */
    private void convertToCurrency(double cryptoVal) {
        double result = cryptoVal * currencyValue;
        currencyTextField.setText(String.valueOf(result));
    }

    /**
     * method to convert from a currency to crypto currency
     *
     * @param currencyVal the current value of the currency
     */
    private void convertToCrypto(double currencyVal) {
        double result = currencyVal / currencyValue;
        cryptoTextField.setText(String.valueOf(result));
    }
}
