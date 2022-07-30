package com.example.lendme.ui.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lendme.R;
import com.example.lendme.models.Chat;

import java.util.List;


public class CustomChatListAdapter extends ArrayAdapter<Chat> {
    private List<Chat> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private int resourceLayout;
    public CustomChatListAdapter(List<Chat> list, Context context, int resource) {
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

        Chat p = getItem(position);

        if (p != null) {
//            ImageView imageView =  v.findViewById(R.id.image_view);
            TextView title =  v.findViewById(R.id.msg_body);
            TextView msgStatus =  v.findViewById(R.id.msg_status);

            if (msgStatus != null) {
                msgStatus.setText(p.getIsRead());
            }

            if (title != null) {
                title.setText(p.getMessage());
            }

        }

        return v;
    }

    static class ViewHolder {
        TextView msgStatus;
        TextView title;
        ImageView image;
    }
}
