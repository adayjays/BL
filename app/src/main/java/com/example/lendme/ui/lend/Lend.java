package com.example.lendme.ui.lend;

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

public class Lend extends Fragment {

    ListView listView;

    public Lend() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lend, container, false);
        String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology", "Household Items", "Clothing and Jewelry", "Miscellaneous"};
        getActivity().setTitle("Lend");
        listView = view.findViewById(R.id.my_list_view2);

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
                PostItemFragment fragment = new PostItemFragment();
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,fragment,"new fragment").commit();
//                Toast.makeText(getActivity(), "TEST "+itemsCategories[i], Toast.LENGTH_LONG).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}