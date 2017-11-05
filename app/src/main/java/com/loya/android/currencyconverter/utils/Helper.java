package com.loya.android.currencyconverter.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.loya.android.currencyconverter.R;
import com.loya.android.currencyconverter.data.CurrencyContract;
import com.loya.android.currencyconverter.data.CurrencyDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ikhiloya on 10/30/2017.
 * A Helper class to dynamically change the drawable icon
 */

public class Helper {
    private static int flag;
    private static CurrencyDbHelper mDbHelper;

    /**
     * @param cryptoName the name of the crypto currency selected by the user
     * @return the drawable icon of the crypto currency based on user selection
     */
    public static int getCryptoIcon(String cryptoName) {
        if (cryptoName.equals("btc")) {
            return R.mipmap.btc;
        } else {
            return R.mipmap.eth;
        }
    }

    /**
     * @param currencyName the name of the currency selected by the user
     * @return the drawable icon of the currency based on user selection
     */
    public static int getCurrencyIcon(String currencyName) {
        // AUD,CAD,CHF,CNY,DKK,EUR,GBP,INR,JPY,KRW,MXN,NGN,NOK,NZD,RUB,SAR,SEK,SGD,TRY,USD

        switch (currencyName) {
            case "aud":
                flag = R.mipmap.icon_ic_australia;
                break;
            case "cad":
                flag = R.mipmap.icon_ic_canada;
                break;
            case "chf":
                flag = R.mipmap.icon_ic_switzerland;
                break;
            case "cny":
                flag = R.mipmap.icon_ic_china;
                break;
            case "dkk":
                flag = R.mipmap.icon_ic_denmark;
                break;
            case "eur":
                flag = R.mipmap.icon_ic_euro;
                break;
            case "gbp":
                flag = R.mipmap.icon_ic_gbp;
                break;
            case "inr":
                flag = R.mipmap.icon_ic_india;
                break;
            case "jpy":
                flag = R.mipmap.icon_ic_japan;
                break;
            case "krw":
                flag = R.mipmap.icon_ic_southkorea;
                break;
            case "mxn":
                flag = R.mipmap.icon_ic_mexico;
                break;
            case "ngn":
                flag = R.mipmap.icon_ic_nigeria;
                break;
            case "nok":
                flag = R.mipmap.icon_ic_norway;
                break;
            case "nzd":
                flag = R.mipmap.icon_ic_newzealand;
                break;
            case "rub":
                flag = R.mipmap.icon_ic_russia;
                break;
            case "sar":
                flag = R.mipmap.icon_ic_southafrica;
                break;
            case "sek":
                flag = R.mipmap.icon_ic_sweden;
                break;
            case "sgd":
                flag = R.mipmap.icon_ic_singapore;
                break;
            case "try":
                flag = R.mipmap.icon_ic_turkey;
                break;
            case "usd":
                flag = R.mipmap.icon_ic_usa;
                break;
        }
        return flag;
    }

    /**
     * @param cryptoName the name of the crypto currency selected
     * @return the name of the crypto currency
     */
    public static String getCryptoName(String cryptoName) {
        if (cryptoName.equals("btc")) {
            return "Bitcoin";
        } else {
            return "Ethereum";
        }
    }

    /**
     * @return the current date
     */
    public static String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        //convert the time in milliseconds into a Date object by calling the Date constructor.
        Date dateObject = new Date(currentTime);
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy"); //e.g 10-09-2017
        SimpleDateFormat time = new SimpleDateFormat(" HH:mm a");   //e.g 12:08 PM
        String dateText = date.format(dateObject);
        String timeText = time.format(dateObject);
        String currentDate = dateText + " at " + timeText;
        return currentDate;
    }

    /**
     * Updates the Database with the current Conversion Rates information from the CryptoCompare API.
     *
     * @param jsonObject  data from the query
     * @param context     the current activity
     * @param currentTime the time at which the data was fetched
     */
    public static void updateDatabase(JSONObject jsonObject, Context context, String currentTime) {
        mDbHelper = new CurrencyDbHelper(context);
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

            //insert into tables
            long table1_rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE1_NAME, null, btcContentValues);
            long table2_rowId = db.insert(CurrencyContract.CurrencyEntry.TABLE2_NAME, null, ethContentValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
