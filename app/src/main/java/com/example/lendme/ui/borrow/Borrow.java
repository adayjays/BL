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
import com.example.lendme.ui.lend.PostItemFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Borrow#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Borrow extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology", "Household Items", "clothing/Jewelry", "Miscellaneous"};
    private ListView listView;

    public Borrow() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Borrow newInstance(String param1, String param2) {
        Borrow fragment = new Borrow();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);
        listView = view.findViewById(R.id.my_list_view);

        CustomBorrowAdapter listAdapter = new CustomBorrowAdapter(itemsCategories);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                itemsCategories);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("key",itemsCategories[i]);
                PostItemFragment fragment = new PostItemFragment();
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