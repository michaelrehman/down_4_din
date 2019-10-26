package com.example.down4din;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class EntryAdapter extends ArrayAdapter {

    private ArrayList<Map<String, Object>> entries;

    public EntryAdapter(Context context, int textViewResourceId, ArrayList<Map<String, Object>> objects) {
        super(context, textViewResourceId, objects);
        entries = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        v = inflater.inflate(R.layout.list_item, null);

        TextView main = v.findViewById(R.id.whoAndWhat);
        TextView address = v.findViewById(R.id.address);
        Button maps = v.findViewById(R.id.mapsBtn);
        Button msg = v.findViewById(R.id.msgBtn);

        Map<String, Object> entry = entries.get(position);
        main.setText(String.format("%s is %s", entry.get("name"), entry.get("doing")));
        address.setText(entry.get("address").toString());
        return v;
    }
}
