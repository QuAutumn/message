package com.example.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private int resourceId;

    public ContactAdapter(Context context, int textViewResourceId, List<Contact> objects) {
        super(context, textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Contact contact = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView text_name = (TextView) view.findViewById(R.id.text_name);
        TextView text_num = (TextView) view.findViewById(R.id.text_num);
        text_name.setText(contact.getName());
        text_num.setText(contact.getNumber());
        return view;
    }
}
