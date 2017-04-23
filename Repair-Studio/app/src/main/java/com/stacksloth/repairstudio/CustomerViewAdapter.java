package com.stacksloth.repairstudio;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Daniel Slapelis on 4/23/2017.
 */

public class CustomerViewAdapter extends BaseAdapter {

    private final ArrayList mData;

    public CustomerViewAdapter(Map<Integer, Customer> customerMap)
    {

        mData = new ArrayList();
        mData.addAll(customerMap.values());

    }

    @Override
    public int getCount() {
        Log.d("ADAPTER", "mData is " + mData.size() + "length");
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d("VOLLEY", "getItem Adapter");
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("VOLLEY", "Should be populating listview");
        final View result;

        if(convertView == null)
        {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_item, parent, false);
        }
        else
        {
            result = convertView;
        }

        TextView nameView = (TextView) result.findViewById(R.id.customerName);
        Customer temp = (Customer) mData.get(position);
        Log.d("Adapter", "Hashmap " + temp.getmName());
        nameView.setText(temp.getmName());

        return result;
    }
}
