package com.example.lendme.ui.lend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lendme.GetLocation;
import com.example.lendme.R;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostItemFragment extends Fragment implements LocationListener,View.OnClickListener {

    private static int RESULT_LOAD_CAMERA_IMAGE = 2;
    private static int RESULT_LOAD_GALLERY_IMAGE = 1;
    private String mCurrentPhotoPath;
    private File cameraImageFile;

    EditText title;
    EditText imageUrl;
    EditText description;
    EditText availability;
    EditText price;
    Button submit;
    Spinner category;
    ImageButton imageButton;
    private Activity activity;
    private LocationManager locationManager;
    private Location location;
    private String cat;
    private String locationLatLong = "";
    private String Document_img1="";
    private Bitmap image_for_product = null;

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
        imageButton = view.findViewById(R.id.imageButton);
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
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void saveTodo() {
        Toast.makeText(activity, "image_for_product ="+(image_for_product != null), Toast.LENGTH_SHORT).show();
        ParseObject item = new ParseObject("items");
        GetLocation getLocation = new GetLocation(getContext());
        boolean requiredFields = title.getText().toString().length() != 0;
        if (requiredFields) {
            progressDialog.show();
            item.put("title", title.getText().toString());
            item.put("description", description.getText().toString());
            item.put("availability", availability.getText().toString());
            item.put("price", price.getText().toString());
            if(image_for_product != null) {
                item.put("image_url", image_for_product);
            }

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
            showAlert("Error", "Please enter a title, image should contain http or htpps and description");
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
    private byte[] readInFile(String path) throws IOException {

        byte[] data = null;
        File file = new File(path);
        InputStream input_stream = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        data = new byte[16384]; // 16K
        int bytes_read;

        while ((bytes_read = input_stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytes_read);
        }

        input_stream.close();
        return buffer.toByteArray();
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder chooseDialog =new AlertDialog.Builder(getContext());
        chooseDialog.setTitle("Pick your choice").setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(options[which].equals("Choose from Gallery")){

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_GALLERY_IMAGE);

                } else {

                    try {

                        File photoFile = createImageFile();
                        Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_MEDIA_SEARCH);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, RESULT_LOAD_GALLERY_IMAGE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        chooseDialog.show();
    }
    private File createImageFile () throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File folder = new File(storageDir.getAbsolutePath() + "/downloads");

        if (!folder.exists()) {
            folder.mkdir();
        }

        cameraImageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                folder      /* directory */
        );

        return cameraImageFile;
    }
    View.OnClickListener chooseImageListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectImage();
        }
    };

    View.OnClickListener uploadListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            byte[] image = null;

            try {
                image = readInFile(mCurrentPhotoPath);
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            // Create the ParseFile
            ParseFile file = new ParseFile("picturePath", image);
            // Upload the image into Parse Cloud
            file.saveInBackground();
            // Create a New Class called "ImageUpload" in Parse
            ParseObject imgupload = new ParseObject("Image");
            // Create a column named "ImageName" and set the string
            imgupload.put("Image", "picturePath");
            // Create a column named "ImageFile" and insert the image
            imgupload.put("ImageFile", file);
            // Create the class and the columns
            imgupload.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {

                    Toast.makeText(activity.getBaseContext(), "Done!", Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    @Override
    public void onClick(View view) {
        if (Document_img1.equals("") || Document_img1.equals(null)) {
            ContextThemeWrapper ctw = new ContextThemeWrapper( getContext(), R.style.Theme_AlertDialog);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
            alertDialogBuilder.setTitle("Image Upload");
            alertDialogBuilder.setMessage("Upload product Image");
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialogBuilder.show();
            return;
        }
        else{

//                SendDetail();
            return;


        }
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Toast.makeText(activity, "onActivityResult "+resultCode +"requestCode"+requestCode , Toast.LENGTH_SHORT).show();

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == RESULT_LOAD_GALLERY_IMAGE && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContext().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == RESULT_LOAD_CAMERA_IMAGE) {
                mCurrentPhotoPath = cameraImageFile.getAbsolutePath();
            }

            File image = new File(mCurrentPhotoPath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
//            imgPhoto.setImageBitmap(bitmap);
        }
    }

}