package com.sjsu.cmpe277.weather.DataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sjsu.cmpe277.weather.R;

import java.util.ArrayList;

/**
 * Created by Ran on 11/3/17.
 */

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<NewItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<NewItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_layout, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.cityName);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.temp);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headlineView.setText(listData.get(position).getCity());
        holder.reporterNameView.setText(listData.get(position).getTemp());
        holder.reportedDateView.setText(listData.get(position).getDate());
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }
}
