package com.example.lendme.ui.lend;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomLendAdapter extends BaseAdapter {
    String[] items;
    public CustomLendAdapter(String[] itemsCategories) {
        super();
        this.items = itemsCategories;
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return items[i].hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setText(items[i]);
        return textView;
    }
}