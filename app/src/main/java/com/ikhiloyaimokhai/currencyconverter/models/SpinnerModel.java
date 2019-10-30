package com.ikhiloyaimokhai.currencyconverter.models;

/**
 * Created by Ikhiloya on 10/30/2017.
 * <p>
 * A model class to populate the spinners
 */

public class SpinnerModel {
    private String codes;
    private int flags;

    public SpinnerModel(String codes, int flags) {
        this.codes = codes;
        this.flags = flags;
    }

    public String getCodes() {
        return codes;
    }

    public int getFlags() {
        return flags;
    }

}
