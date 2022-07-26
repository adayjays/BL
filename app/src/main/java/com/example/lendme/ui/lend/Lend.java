package com.example.lendme.ui.lend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.lendme.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Lend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Lend extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Lend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Lend.
     */
    // TODO: Rename and change types and number of parameters
    public static Lend newInstance(String param1, String param2) {
        Lend fragment = new Lend();
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
        View view = inflater.inflate(R.layout.fragment_lend, container, false);
        String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology", "Household Items", "Clothing and Jewelry", "Miscellaneous"};
        getActivity().setTitle("Lend");

        listView = view.findViewById(R.id.my_list_view2);
        CustomLendAdapter listAdapter = new CustomLendAdapter(itemsCategories);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("key",itemsCategories[i]);
                PostItemFragment fragment = new PostItemFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,fragment,"new fragment").commit();

//                Toast.makeText(getActivity(), "TEST "+itemsCategories[i], Toast.LENGTH_LONG).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}