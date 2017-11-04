package com.loya.android.currencyconverter.data;

import android.provider.BaseColumns;

/**
 * Created by Ikhiloya on 10/10/2017.
 */

public final class CurrencyContract {

    public CurrencyContract() {

    }

    public static final class CurrencyEntry implements BaseColumns {



        //constants for table 1
        public static final String TABLE1_NAME = "btcconvert";
        public static final String BTC_ID = BaseColumns._ID;
        public static final String COLUMN_BTCTOAUD = "btctoaud";
        public static final String COLUMN_BTCTOCAD = "btctocad";
        public static final String COLUMN_BTCTOCHF = "btctochf";
        public static final String COLUMN_BTCTOCNY = "btctocny";
        public static final String COLUMN_BTCTODKK = "btctodkk";
        public static final String COLUMN_BTCTOEUR = "btctoeur";
        public static final String COLUMN_BTCTOGBP = "btctogbp";
        public static final String COLUMN_BTCTOINR = "btctoinr";
        public static final String COLUMN_BTCTOJPY = "btctojpy";
        public static final String COLUMN_BTCTOKRW = "btctokrw";
        public static final String COLUMN_BTCTOMXN = "btctomxn";
        public static final String COLUMN_BTCTONGN = "btctongn";
        public static final String COLUMN_BTCTONOK = "btctonok";
        public static final String COLUMN_BTCTONZD = "btctonzd";
        public static final String COLUMN_BTCTORUB = "btctorub";
        public static final String COLUMN_BTCTOSAR = "btctosar";
        public static final String COLUMN_BTCTOSEK = "btctosek";
        public static final String COLUMN_BTCTOSGD = "btctosgd";
        public static final String COLUMN_BTCTOTRY = "btctotry";
        public static final String COLUMN_BTCTOUSD = "btctousd";
        public static final String COLUMN_BTCTIMESTAMP = "btctimestamp";


        //constants for table 2
        public static final String TABLE2_NAME = "ethconvert";
        public static final String ETH_ID = BaseColumns._ID;

        public static final String COLUMN_ETHTOAUD = "ethtoaud";
        public static final String COLUMN_ETHTOCAD = "ethtocad";
        public static final String COLUMN_ETHTOCHF = "ethtochf";
        public static final String COLUMN_ETHTOCNY = "ethtocny";
        public static final String COLUMN_ETHTODKK = "ethtodkk";
        public static final String COLUMN_ETHTOEUR = "ethtoeur";
        public static final String COLUMN_ETHTOGBP = "ethtogbp";
        public static final String COLUMN_ETHTOINR = "ethtoinr";
        public static final String COLUMN_ETHTOJPY = "ethtojpy";
        public static final String COLUMN_ETHTOKRW = "ethtokrw";
        public static final String COLUMN_ETHTOMXN = "ethtomxn";
        public static final String COLUMN_ETHTONGN = "ethtongn";
        public static final String COLUMN_ETHTONOK = "ethtonok";
        public static final String COLUMN_ETHTONZD = "ethtonzd";
        public static final String COLUMN_ETHTORUB = "ethtorub";
        public static final String COLUMN_ETHTOSAR = "ethtosar";
        public static final String COLUMN_ETHTOSEK = "ethtosek";
        public static final String COLUMN_ETHTOSGD = "ethtosgd";
        public static final String COLUMN_ETHTOTRY = "ethtotry";
        public static final String COLUMN_ETHTOUSD = "ethtousd";

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
