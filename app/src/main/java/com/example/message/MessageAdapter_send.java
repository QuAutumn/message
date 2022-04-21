package com.example.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter_send extends ArrayAdapter<Message>{
    private int resourceId;

    public MessageAdapter_send(Context context, int textViewResourceId, List<Message> objects) {
        super(context, textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Message message = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        //TextView text_receive = (TextView) view.findViewById(R.id.text_receive);
        TextView text_send = (TextView) view.findViewById(R.id.text_send);
        //text_receive.setText(message.getReceive_mess());
        text_send.setText(message.getSend_mess());
        return view;
    }
}
