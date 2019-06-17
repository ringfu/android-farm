package com.mycompany.farm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycompany.farm.R;
import com.mycompany.farm.bean.User;

import java.util.List;

public class MyListViewApader extends BaseAdapter {

    private List<User> mlist;
    private Context context;

    public MyListViewApader(List<User> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
            holder.mListViewText = convertView.findViewById(R.id.mListViewText);
            holder.mYueDouXianShi = convertView.findViewById(R.id.mYueDouXianShi);
            holder.mYueDouLin = convertView.findViewById(R.id.mYueDouLin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = mlist.get(position).getName();
        holder.mListViewText.setText(name);


        //true和false用来解决复用问题的，如果当前标记为选中状态那么item字体颜色变红，红色竖条显示，item背景色变白
        if (mlist.get(position).isSelect()) {
            holder.mListViewText.setTextColor(Color.RED);
            holder.mYueDouXianShi.setVisibility(View.VISIBLE);
            holder.mYueDouLin.setBackgroundColor(Color.WHITE);
        }
        //如果不是选中状态，item字体颜色变黑，红色竖条隐藏，item背景还变灰
        else {
            holder.mListViewText.setTextColor(Color.BLACK);
            holder.mYueDouXianShi.setVisibility(View.GONE);
            holder.mYueDouLin.setBackgroundColor(Color.parseColor("#EBEBEB"));
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView mListViewText;
        public TextView mYueDouXianShi;
        public LinearLayout mYueDouLin;
    }

}

