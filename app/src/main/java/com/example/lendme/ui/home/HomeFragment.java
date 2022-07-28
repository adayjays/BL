package com.example.lendme.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lendme.OnSwipeTouchListener;
import com.example.lendme.R;
import com.example.lendme.ui.borrow.Borrow;
import com.example.lendme.ui.lend.Lend;
import com.parse.ParseUser;

public class HomeFragment extends Fragment implements View.OnClickListener {

    Button lendButton;
    Button borrowButton;
    TextView welcomeText;
    ConstraintLayout constraintLayout;
    

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        borrowButton = view.findViewById(R.id.borrowbtn);
        lendButton = view.findViewById(R.id.lendbtn);
        welcomeText = view.findViewById(R.id.welcome_text);
        constraintLayout = view.findViewById(R.id.container);

        welcomeText.setText("Just another sample");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            welcomeText.setText("Welcome to LendMe, " + currentUser.getUsername() + " !");
        }
        lendButton.setOnClickListener(this);
        borrowButton.setOnClickListener(this);
        container.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
                goToBorrow();
            }
            public void onSwipeRight() {
                goToBorrow();
            }
            public void onSwipeLeft() {
                goToLend();
            }
            public void onSwipeBottom() {
                goToLend();
            }

        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lendbtn:
                goToLend();
                break;
            case R.id.borrowbtn:
                goToBorrow();
                break;
            default:
                break;
        }
    }

    public void goToLend() {
        Lend lend = new Lend();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, lend, "fragment_screen");
        fragmentTransaction.commit();
    }

    public void goToBorrow() {
        Borrow borrow = new Borrow();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, borrow, "fragment_screen");
        fragmentTransaction.commit();
    }
}