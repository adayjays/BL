package com.example.lendme.ui.borrow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lendme.R;

public class Borrow extends Fragment {

    private String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology", "Household Items", "Clothing and Jewelry", "Miscellaneous"};
    private ListView listView;

    public Borrow() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);
//        view.getSupportActionBar().setTitle("Hello world App");
        listView = view.findViewById(R.id.my_list_view);
        getActivity().setTitle("Borrow");
        CustomBorrowAdapter listAdapter = new CustomBorrowAdapter(itemsCategories);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("key", itemsCategories[i]);
                ItemsFragment fragment = new ItemsFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,fragment,"new fragment").commit();
            }
        });

        return  view;
    }
}
class CustomBorrowAdapter extends BaseAdapter {
    String[] items;
    public CustomBorrowAdapter(String[] itemsCategories) {
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