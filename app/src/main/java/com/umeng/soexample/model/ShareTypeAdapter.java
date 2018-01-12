package com.umeng.soexample.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.common.ResContainer;
import com.umeng.soexample.R;


import java.util.ArrayList;

/**
 * Created by wangfei on 16/11/10.
 */
public class ShareTypeAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;

    public ShareTypeAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.app_styleadapter, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.name);
        tv.setText(list.get(position));

        return convertView;
    }
}
