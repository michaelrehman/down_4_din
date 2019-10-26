package com.example.down4din;

import android.content.Context;
import android.content.Intent;
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
    private Context context;

    public EntryAdapter(Context context, int textViewResourceId, ArrayList<Map<String, Object>> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        entries = objects;
    }

    @Override
    public int getCount() { return super.getCount(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        v = inflater.inflate(R.layout.list_item, null);
        final Map<String, Object> entry = entries.get(position);

        TextView main = v.findViewById(R.id.whoAndWhat);
        TextView address = v.findViewById(R.id.address);
        Button maps = v.findViewById(R.id.mapsBtn);
        Button msg = v.findViewById(R.id.msgBtn);

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MapActivity.class);
                i.putExtra("address", entry.get("address").toString());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        main.setText(String.format("%s is %s", entry.get("name"), entry.get("doing")));
        address.setText(entry.get("address").toString());
        return v;
    }
}
