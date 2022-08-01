package com.example.lendme.ui.borrow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.lendme.CategoryListAdapter;
import com.example.lendme.R;
import com.example.lendme.models.Category;

import java.util.ArrayList;
import java.util.List;

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
//        getActivity().setTitle("Borrow");

        List<Category> itemCategoryList = new ArrayList<>();
        for (int i = 0; i < itemsCategories.length; i++) {
            Category category = new Category(itemsCategories[i]);
            itemCategoryList.add(category);
        }

        CategoryListAdapter listAdapter = new CategoryListAdapter(itemCategoryList,getContext(),R.layout.category_list_row);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("key", itemCategoryList.get(i).getCategory());
                ItemsFragment fragment = new ItemsFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,fragment,"new fragment").commit();
            }
        });

        return  view;
    }
}