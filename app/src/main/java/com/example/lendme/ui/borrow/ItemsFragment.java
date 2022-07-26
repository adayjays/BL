package com.example.lendme.ui.borrow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lendme.ItemAdapter;
import com.example.lendme.R;
import com.example.lendme.RecyclerViewClickListener;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;
    private static RecyclerViewClickListener itemListener;

    private RecyclerView recyclerView;
    private TextView categoryType;

    public ItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
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
        View view = inflater.inflate(R.layout.fragment_items, container, false);


                recyclerView = view.findViewById(R.id.recyclerView);
        categoryType = view.findViewById(R.id.category_type);
//        empty_text = findViewById(R.id.empty_text);

        Bundle b = this.getArguments();
        String value = ""; // or other values
        if(b != null) {
            value = b.getString("key");
//            set it as title of the page
            categoryType.setText(value.toUpperCase(Locale.ROOT));

        }
        getItemList(value);

        itemListener = new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
//                Toast.makeText(getContext(),"Position is",Toast.LENGTH_LONG).show();

            }


        };
        // Inflate the layout for this fragment
        return view;
    }

    private void getItemList(String val) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
        //We use this code to fetch data from newest to oldest.
        if (val != "") {
            query.whereEqualTo("category", val);
        }
        query.whereEqualTo("is_borrowable",1);
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {

                initTodoList(objects);
            } else {
                showAlert("Error", e.getMessage());
            }
        });
    }



    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
//                    Intent intent = new Intent(getContext(), ItemsActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);

                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    private void initTodoList(List<ParseObject> list) {
        if (list == null || list.isEmpty()) {
//            empty_text.setVisibility(View.VISIBLE);
            return;
        }
//        empty_text.setVisibility(View.GONE);

        ItemAdapter adapter = new ItemAdapter(list, getContext());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}