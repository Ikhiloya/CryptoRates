package com.loya.android.currencyconverter.data;

import android.provider.BaseColumns;

/**
 * Created by user on 10/10/2017.
 */

public final class CurrencyContract {

    public CurrencyContract() {

    }

    public static final class CurrencyEntry implements BaseColumns {


        //constants for table 1
        public static final String TABLE1_NAME = "btcconvert";
        public static final String BTC_ID = BaseColumns._ID;
        public static final String COLUMN_BTCTOEUR = "btctoeur";
        public static final String COLUMN_BTCTOUSD = "btctousd";
        public static final String COLUMN_BTCTONGN = "btctongn";
        public static final String COLUMN_BTCTIMESTAMP = "btctimestamp";


        //constants for table 2
        public static final String TABLE2_NAME = "ethconvert";
        public static final String ETH_ID = BaseColumns._ID;

        public static final String COLUMN_ETHTOEUR = "ethtoeur";
        public static final String COLUMN_ETHTOUSD = "ethtousd";
        public static final String COLUMN_ETHTONGN = "ethtongn";
        public static final String COLUMN_ETHTIMESTAMP = "ethtimestamp";


        //USER TABLE

        public static final String TABLE3_NAME = "user";
        public static final String USER_ID = BaseColumns._ID;

        public static final String COLUMN_CRYPTO_NAME = "cyrptoname";
        public static final String COLUMN_CURRENCY_NAME = "currencyname";
        public static final String COLUMN_CURRENCY_VALUE = "currencyvalue";
        public static final String COLUMN_TIMESTAMP = "timestamp";


    }

}
