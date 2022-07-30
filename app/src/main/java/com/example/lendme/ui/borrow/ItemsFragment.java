package com.example.lendme.ui.borrow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lendme.GetLocation;
import com.example.lendme.R;
import com.example.lendme.RecyclerViewClickListener;
import com.example.lendme.models.Item;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemsFragment extends Fragment {

    private ProgressDialog progressDialog;
    private static RecyclerViewClickListener itemListener;

    private RecyclerView recyclerView;
    private TextView categoryType;
    private LocationManager locationManager;
    private ListView itemList;
    private List<Item> itemsList;

    public ItemsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

//        recyclerView = view.findViewById(R.id.recyclerView);
        categoryType = view.findViewById(R.id.category_type);
        itemList = view.findViewById(R.id.item_list);
//        empty_text = findViewById(R.id.empty_text);

        Bundle bundle = this.getArguments();
        String value = ""; // or other values
        if (bundle != null) {
            value = bundle.getString("key");
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
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                if(itemsList.size()>0){
                    bundle.putString("key",itemsList.get(i).getId() );
                }else{

                    bundle.putString("key","0");
                }

                ItemFragment itemFragment = new ItemFragment();
                itemFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, itemFragment, "itemFragment");
                fragmentTransaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void getItemList(String category) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
        //We use this code to fetch data from newest to oldest.
        if (category != "") {
            query.whereEqualTo("category", category);
        }
        query.whereEqualTo("is_borrowable", 1);
        GetLocation getLocation = new GetLocation(getContext());
        Location location = getLocation.getLocation(getActivity(),locationManager);
        if (location != null) {
            query.whereNear("seller_loc", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
        }
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
            Toast.makeText(getContext(), "No items found", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Item> items = new ArrayList<>();
        for (ParseObject object : list) {
            ParseGeoPoint point = object.getParseGeoPoint("seller_loc");
            // Bitmap bitmap_image = object.getParseFile("image").getData();
            Bitmap bitmap_image = null;
            Item item = new Item(
                object.getObjectId(),object.getString("title"),object.getString("description"),object.getString("availability"),
                object.getString("price"),bitmap_image,point,object.getString("category"),
                object.getString("is_borrowable"),object.getString("posted_by")
            );
            
            items.add(item);
        }
        itemsList = items;
        CustomItemListAdapter adapter = new CustomItemListAdapter(items,getContext(),R.layout.item_list_row);
//        Toast.makeText(getContext(), "items size"+items.size(), Toast.LENGTH_SHORT).show();
        itemList.setAdapter(adapter);
        

        // ItemAdapter adapter = new ItemAdapter(list, getContext());

        // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // recyclerView.setAdapter(adapter);
    }
}