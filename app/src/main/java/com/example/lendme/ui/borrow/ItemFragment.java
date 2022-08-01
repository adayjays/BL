package com.example.lendme.ui.borrow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lendme.R;
import com.example.lendme.ui.messages.ChatFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView descTxt, titleTxt, otherTxt;
    ImageView imageView3;
    Button borrowBtn,chat;
    boolean isAvailable = false;
    String objectId = "1";
    String ownerId = "1";

    public ItemFragment() {
        // Required empty public constructor
    }

    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        descTxt = view.findViewById(R.id.desc_txt);
        imageView3 = view.findViewById(R.id.imageView3);
        titleTxt = view.findViewById(R.id.title_txt);
        borrowBtn = view.findViewById(R.id.borrow_btn);
        otherTxt = view.findViewById(R.id.other_txt);
        chat = view.findViewById(R.id.chat_btn);
        Bundle b = this.getArguments();
        if (b != null) {
            String value = b.getString("key");
            objectId = value;
//            Toast.makeText(getContext(),"Text! "+value,Toast.LENGTH_SHORT).show();
            isAvailable = true;
            getData(value);
            //The key argument here must match that used in the other activity
        }
        if (isAvailable){
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", objectId);
                    ConfirmationFragment fragment = new ConfirmationFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment.setArguments(bundle);

                    ft.replace(R.id.nav_host_fragment_activity_main,fragment,"new fragment");
                            ft.commit();

                }
            });
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String extra = ownerId +","+ objectId;
                    Bundle bundle = new Bundle();
                    bundle.putString("key",extra);

                    ChatFragment chatFragment = new ChatFragment();
                    chatFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, chatFragment, "chatFragment");
                    fragmentTransaction.commit();
                }
            });
        }

        return view;
    }

    public void getData(String key){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
// Query parameters based on the item name
        query.whereEqualTo("objectId", key);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject item, ParseException e) {
                if (e == null) {
                    isAvailable = true;
                    String desc = item.getString("description");
                    String title = item.getString("title");
                    String img =  item.getString("image_url");
                    ownerId = item.getString("posted_by");
                    String newOtherTxt = "$"+item.getString("price")+" per day.";
                    descTxt.setText(desc);
                    titleTxt.setText(title);
                    if (img != null){
                        imageView3.setImageBitmap(getBitmapFromURL(img));
                    }else{
                        imageView3.setImageResource(R.drawable.nopictures);
                    }
                    otherTxt.setText(newOtherTxt);
                } else {
                    // Something is wrong
                }
            }
        });
    }
    public static Bitmap getBitmapFromURL(String src) {
        if(URLUtil.isHttpUrl(src) || URLUtil.isHttpsUrl(src)) {
            try {
//                Log.e("src", src);
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                Log.e("Bitmap", "returned");
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
//                Log.e("Exception", e.getMessage());
                return null;
            }
        }else {
            return null;
        }
    }
}