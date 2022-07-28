package com.example.lendme.ui.lend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lendme.GetLocation;
import com.example.lendme.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PostItemFragment extends Fragment implements LocationListener {

    private static final int REQUEST_LOCATION = 1;

    EditText title;
    EditText imageUrl;
    EditText description;
    EditText availability;
    EditText price;
    Button submit;
    Spinner category;
    private Activity activity;
    private FloatingActionButton openInputPopupDialogButton;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private LocationManager locationManager;
    private Location location;
    private String cat;
    private String locationLatLong = "";

    private ProgressDialog progressDialog;

    public PostItemFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_post_item, container, false);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        availability = view.findViewById(R.id.availability);
        submit = view.findViewById(R.id.submit);
        category = view.findViewById(R.id.category);
        price = view.findViewById(R.id.price);
        imageUrl = view.findViewById(R.id.image_url);
        progressDialog = new ProgressDialog(getContext());
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location = null;
        GetLocation getLocationInstance = new GetLocation(activity);

        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

        location = getLocationInstance.getLocation(activity,locationManager);
        if (location != null) {
            onLocationChanged(location);
        }

        cat = "Books";

        String[] items = new String[] {"Books", "Outdoor supplies", "Technology", "Household Items", "Clothing and Jewelry", "Miscellaneous"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);

        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat = category.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
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
        boolean requiredFields = title.getText().toString().length() != 0 && 
                                 description.getText().toString().length() != 0 && 
                                 (URLUtil.isHttpUrl(imageUrl.getText().toString()) || URLUtil.isHttpsUrl(imageUrl.getText().toString()));
        if (requiredFields) {
            progressDialog.show();
            item.put("title", title.getText().toString());
            item.put("description", description.getText().toString());
            item.put("availability", availability.getText().toString());
            item.put("price", price.getText().toString());
            item.put("image_url", imageUrl.getText().toString());

            if (locationLatLong == "") {
                item.put("seller_loc", new ParseGeoPoint(56.48, 88.08));
            } else {
                String[] locationLatLongArray = locationLatLong.split(",");
                double lat = Double.parseDouble(locationLatLongArray[0]);
                double lng = Double.parseDouble(locationLatLongArray[1]);

                item.put("seller_loc", new ParseGeoPoint(lat, lng));
            }

            item.put("category", cat);
            item.put("is_borrowable", 1);
            ParseUser currentUser = ParseUser.getCurrentUser();
            item.put("posted_by", currentUser.getObjectId());

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
        locationLatLong = latitude + "," + longitude;
    }
}