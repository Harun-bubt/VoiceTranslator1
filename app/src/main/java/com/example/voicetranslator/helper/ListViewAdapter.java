package com.example.voicetranslator.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.voicetranslator.R;

public class ListViewAdapter extends BaseAdapter {

    String[] arr;
    Context con;

    public ListViewAdapter(Context con, String[] ar) {
        this.con = con;
        this.arr = ar;
    }

    public int getCount() {
        return this.arr.length;
    }

    public Object getItem(int i) {
        return this.arr[i];
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = ((LayoutInflater) this.con.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item, viewGroup, false);
        ((TextView) v.findViewById(R.id.tvitem)).setText(this.arr[i]);
        return v;
    }
}