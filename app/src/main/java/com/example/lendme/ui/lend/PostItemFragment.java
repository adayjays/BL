package com.example.lendme.ui.lend;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lendme.GetLocation;
import com.example.lendme.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostItemFragment extends Fragment implements LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText title;
    EditText imageUrl;
    EditText description;
    EditText availability;
    EditText price;
    Button submit;
    Spinner category;
    private FloatingActionButton openInputPopupDialogButton;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private LocationManager locationManager;
    private Location loc;
    private String cat;
    private String locationLatLong ="";

    private ProgressDialog progressDialog;

    public PostItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostItemFragment newInstance(String param1, String param2) {
        PostItemFragment fragment = new PostItemFragment();
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
        Bundle bundle = this.getArguments();
        View view = inflater.inflate(R.layout.fragment_post_item, container, false);
        String Key  = bundle.getString("key");
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        availability = view.findViewById(R.id.availability);
        submit = view.findViewById(R.id.submit);
        category = view.findViewById(R.id.category);
        price = view.findViewById(R.id.price);
        imageUrl = view.findViewById(R.id.image_url);
        progressDialog = new ProgressDialog(getContext());
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        loc = null;

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(loc);
        }

        cat = "Books";


        String[] items = new String[]{"Books", "Outdoor supplies", "Technology","Household Items","clothing/Jewelry", "Miscellaneous"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);

        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat = category.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTodo();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void saveTodo() {
        ParseObject item = new ParseObject("items");
        GetLocation getLocation = new GetLocation(getContext());
        if (title.getText().toString().length() != 0 && description.getText().toString().length() != 0 &&(URLUtil.isHttpUrl(imageUrl.getText().toString()) || URLUtil.isHttpsUrl(imageUrl.getText().toString()))) {

            progressDialog.show();
            item.put("title", title.getText().toString());
            item.put("description", description.getText().toString());
            item.put("availability", availability.getText().toString());
            item.put("price",price.getText().toString());
            item.put("image_url", imageUrl.getText().toString());
            if (locationLatLong == ""){
                item.put("seller_loc", "no location data found");
            }else {
                String[]locationLatLongArray = locationLatLong.split(",");
                double lat = Double.parseDouble(locationLatLongArray[0]);
                double lng = Double.parseDouble(locationLatLongArray[1]);

                item.put("seller_loc",getLocation.getAddresPlain(lat,lng));
            }
            item.put("category",cat);
            item.put("is_borrowable",1);
            ParseUser currentUser = ParseUser.getCurrentUser();
            item.put("posted_by",currentUser.getObjectId());

            item.saveInBackground(e -> {
                progressDialog.dismiss();
                if (e == null) {
                    //We saved the object and fetching data again
                    showAlert("Success","Item saved Successfully");
                } else {
                    //We have an error.We are showing error message here.
                    showAlert("Error", e.getMessage());
                }
            });
        } else {
            showAlert("Error", "Please enter a title, image_ur should contain http or htpps and description");
        }
    }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        locationLatLong = latitude+","+longitude;

    }
}