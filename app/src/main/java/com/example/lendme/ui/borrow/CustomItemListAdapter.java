package com.example.lendme.ui.borrow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lendme.GetLocation;
import com.example.lendme.R;
import com.example.lendme.models.Item;

import java.util.List;


public class CustomItemListAdapter extends ArrayAdapter<Item> {
    private List<Item> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private int resourceLayout;
    public CustomItemListAdapter(List<Item> list, Context context,int resource) {
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

        Item p = getItem(position);

        if (p != null) {
            ImageView imageView =  v.findViewById(R.id.image_view);
            TextView title =  v.findViewById(R.id.title);
            TextView location =  v.findViewById(R.id.location);

            if (location != null) {
                location.setText(p.getId());
            }

            if (location != null) {
                if (p.getSeller_loc() != null){
                    try {
                        GetLocation getLocation = new GetLocation(context.getApplicationContext());
                        double distance = getLocation.getCurrentLocation().distanceInKilometersTo(p.getSeller_loc());

                        location.setText("located at : "+Math.round (distance * 100.0) / 100.0  + " km away");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (title != null) {
                title.setText(p.getTitle());
            }
            if(imageView !=null){
                if(p.getImage_url() != null){
                    imageView.setImageBitmap(p.getImage_url());
                }
            }
        }

        return v;
    }

    static class ViewHolder {
        TextView title;
        TextView location;
        ImageView image;
    }
}
