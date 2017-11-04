package com.loya.android.currencyconverter.utils;


import android.content.Context;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.loya.android.currencyconverter.R;
import com.loya.android.currencyconverter.activites.MainActivity;
import com.loya.android.currencyconverter.adapter.CurrencyAdapter;

/**
 * Created by Ikhiloya on 11/1/2017.
 */

public class Toolbar_ActionMode_Callback implements android.support.v7.view.ActionMode.Callback {
    private  Context context;
    private CurrencyAdapter currencyAdapter;

    public Toolbar_ActionMode_Callback(Context context, CurrencyAdapter currencyAdapter) {
        this.context = context;
        this.currencyAdapter = currencyAdapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_item, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels

            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                MainActivity mainActivity =  (MainActivity)context;
                mainActivity.deleteRows();//delete selected rows
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        currencyAdapter.removeSelection();  // remove selection
        MainActivity mainActivity =  (MainActivity)context;
            mainActivity.setNullToActionMode();//Set action mode null
    }
}






