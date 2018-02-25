package com.example.yang.apkget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yang on 17/11/28.
 */

public class AppInfoAdapter extends BaseAdapter {
    private ArrayList<AppInfo> data = new ArrayList<>();
    private Context context;

    AppInfoAdapter(Context context, ArrayList list){
        this.context = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.imageView);
            holder.name = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(data.get(position).name);
        holder.icon.setImageDrawable(data.get(position).icon);

        return convertView;
    }

    class ViewHolder{
        ImageView icon;
        TextView name;
    }
}
