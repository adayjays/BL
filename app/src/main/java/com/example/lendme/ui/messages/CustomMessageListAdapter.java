package com.example.lendme.ui.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lendme.R;
import com.example.lendme.models.Message;

import java.util.List;

public class CustomMessageListAdapter extends ArrayAdapter<Message> {
    private List<Message> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private int resourceLayout;

    public CustomMessageListAdapter(List<Message> list, Context context, int resource) {
//        super();
        super(context, resource, list);
        this.resourceLayout = resource;
        this.listData = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resourceLayout, null);
        }

        Message p = getItem(position);

        if (p != null) {
//            ImageView imageView =  v.findViewById(R.id.image_view);
            TextView sender = v.findViewById(R.id.sender);
            TextView time = v.findViewById(R.id.time);

            if (sender != null) {
                sender.setText(p.getSenderName());
            }

            if (time != null) {
                time.setText(p.getSendTime());
            }

        }

        return v;
    }
}
