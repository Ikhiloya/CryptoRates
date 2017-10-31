package com.loya.android.currencyconverter.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loya.android.currencyconverter.Models.SpinnerModel;
import com.loya.android.currencyconverter.R;

import java.util.ArrayList;

/**
 * Created by ikhiloya on 10/28/2017.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<SpinnerModel> spinners;
    LayoutInflater inflater;


    public SpinnerAdapter(Context applicationContext, ArrayList<SpinnerModel> spinners) {
        this.context = applicationContext;
        this.spinners = spinners;
        inflater = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return spinners.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_spinner_items, null);

        SpinnerModel spinnerModel = spinners.get(position);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(spinnerModel.getFlags());
        names.setText(spinnerModel.getCodes());
        return view;
    }
}
