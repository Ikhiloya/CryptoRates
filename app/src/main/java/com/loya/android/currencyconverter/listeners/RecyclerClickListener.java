package com.loya.android.currencyconverter.listeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ikhiloya on 11/1/2017.
 * Interface for Recycler Long press listener
 */

public interface RecyclerClickListener {
    void onClick(View view, RecyclerView.ViewHolder viewHolder);

    void onLongClick(View view, RecyclerView.ViewHolder viewHolder);
}
