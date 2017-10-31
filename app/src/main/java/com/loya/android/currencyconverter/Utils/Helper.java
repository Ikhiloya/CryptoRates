package com.loya.android.currencyconverter.Utils;

import com.loya.android.currencyconverter.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ikhiloya on 10/30/2017.
 * A Helper class to dynamically change the drawable icon
 */

public class Helper {

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
        if (currencyName.equals("eur")) {
            return R.mipmap.ic_launcher;
        } else if (currencyName.equals("usd")) {
            return R.mipmap.ic_launcher_round;
        } else {
            return R.mipmap.ic_launcher;
        }
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
     *
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

}
