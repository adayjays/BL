package com.example.lendme.ui.borrow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lendme.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConfirmationFragment extends Fragment {

    Boolean isAvailable = false;
    TextView thankYouMsg, agreementMsg, warningMsg;
    Button homeButton, confirm;
    String value_key = "1";

    public ConfirmationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        thankYouMsg = view.findViewById(R.id.thank_you_msg);
        agreementMsg = view.findViewById(R.id.you_msg);
        warningMsg = view.findViewById(R.id.notf_msg);
        homeButton = view.findViewById(R.id.button_home);
        confirm = view.findViewById(R.id.confirm_btn);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(ConfirmationActivity.this, MainActivity.class);
//                startActivity(in);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(value_key);
//                Toast t = Toast.makeText(ConfirmationActivity.this, "Borrow Confirmed !", Toast.LENGTH_SHORT);
//                t.show();
//                Intent in = new Intent(ConfirmationActivity.this, MainActivity.class);
//                startActivity(in);
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

    public void getData(String key) {
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
                    String newThankYouMsg  = "Thank you for using LendMe, " + username + "! ";
                    Date today = new Date();
                    SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 1);  // number of days to add
                    String tomorrow = (String)(formattedDate.format(calendar.getTime()));

                    String newAgreementMsg = "You and the lender will meet at the agreed location on " + tomorrow;
                    calendar.add(Calendar.DATE,6);
                    String newWarningMsg = "You must return '" + title + "' by " + (String)formattedDate.format(calendar.getTime()) + " to avoid late fees.";
                    thankYouMsg.setText(newThankYouMsg);
                    agreementMsg.setText(newAgreementMsg);
                    warningMsg.setText(newWarningMsg);
                } else {
                    // Something is wrong
                }
            }
        });
    }

    private void updateItem(String key) {
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