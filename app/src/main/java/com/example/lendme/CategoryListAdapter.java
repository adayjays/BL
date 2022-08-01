package com.example.lendme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lendme.models.Category;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private List<Category> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private int resourceLayout;
    public CategoryListAdapter(List<Category> list, Context context,int resource) {
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

        Category p = getItem(position);

        if (p != null) {
            TextView category =  v.findViewById(R.id.category_item);

            if (category != null) {
                category.setText(p.getCategory());
            }
        }

        return v;
    }

}

