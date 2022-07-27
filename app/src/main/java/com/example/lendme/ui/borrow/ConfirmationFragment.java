package com.example.lendme.ui.borrow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.lendme.MainActivity;
import com.example.lendme.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Boolean isAvailable = false;
    TextView thankYouMsg, youMsg, notfMsg;
    Button homeBtn,confirm;
    String value_key = "1";

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ConfirmationFragment newInstance(String param1, String param2) {
        ConfirmationFragment fragment = new ConfirmationFragment();
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
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        thankYouMsg = view.findViewById(R.id.thank_you_msg);
        youMsg = view.findViewById(R.id.you_msg);
        notfMsg = view.findViewById(R.id.notf_msg);
        homeBtn = view.findViewById(R.id.button_home);
        confirm = view.findViewById(R.id.confirm_btn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(value_key);
                Toast t = Toast.makeText(getActivity(), "Borrow Confirmed !", Toast.LENGTH_SHORT);
                t.show();
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        });
        Bundle extras = this.getArguments();
//        Log.e(extras.getString("key"));
        if (extras != null) {
            String value = extras.getString("key");
            value_key = value;
            isAvailable = true;
            getData(value);
            //The key argument here must match that used in the other activity
        }
        return view;
    }
    public void getData(String key){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
// Query parameters based on the item name
        query.whereEqualTo("objectId", key);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    isAvailable = true;
                    String title = player.getString("title");
                    String username = "Sara";
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        username = currentUser.getUsername();
                    }
                    String new_thank_you_msg  = "Thank you for using LendMe,"+ username +"! ";
                    Date today = new Date();
                    SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, 1);  // number of days to add
                    String tomorrow = (String)(formattedDate.format(c.getTime()));

                    String new_you_msg = "You and the lender will meet at the agreed location on "+tomorrow;
                    c.add(Calendar.DATE,6);
                    String new_notf_msg = "You must return '"+ title +"' by "+ (String)formattedDate.format(c.getTime()) +" to avoid late fees.";
                    thankYouMsg.setText(new_thank_you_msg);
                    youMsg.setText(new_you_msg);
                    notfMsg.setText(new_notf_msg);

                } else {
                    // Something is wrong
                }
            }
        });
    }
    private void updateItem(String key){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
// Retrieve the object by id
        query.getInBackground(key, new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new dat
                    player.put("is_borrowable", 0);
                    player.saveInBackground();
                } else {
                    // Failed
                }
            }
        });
    }

}