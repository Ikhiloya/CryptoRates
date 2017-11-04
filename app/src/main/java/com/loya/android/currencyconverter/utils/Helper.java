package com.loya.android.currencyconverter.utils;

import com.loya.android.currencyconverter.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ikhiloya on 10/30/2017.
 * A Helper class to dynamically change the drawable icon
 */

public class Helper {
    private static int flag;

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

}
