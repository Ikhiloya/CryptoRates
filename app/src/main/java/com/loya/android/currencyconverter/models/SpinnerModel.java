package com.loya.android.currencyconverter.models;

/**
 * Created by Ikhiloya on 10/30/2017.
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

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}
