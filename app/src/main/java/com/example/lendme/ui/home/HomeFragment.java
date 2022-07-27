package com.example.lendme.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lendme.R;
import com.example.lendme.ui.borrow.Borrow;
import com.example.lendme.ui.lend.Lend;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button lendBtn;
    Button borrowBtn;
    TextView welcomeText;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        borrowBtn = view.findViewById(R.id.borrowbtn);
        lendBtn = view.findViewById(R.id.lendbtn);
        welcomeText = view.findViewById(R.id.welcome_text);

        welcomeText.setText("Just another sample");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            welcomeText.setText("Welcome to LendMe, " + currentUser.getUsername() + " !");

        }
        lendBtn.setOnClickListener(this);
        borrowBtn.setOnClickListener(this);
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
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.nav_host_fragment_activity_main, Lend.class, null)
//                .addToBackStack(null)
//                .commit();

//        FragmentManager fm = getActivity().getSupportFragmentManager();
////        fm.beginTransaction().hide(HomeFragment.this);
//        fm.beginTransaction().replace(R.id.nav_host_fragment_activity_main,  lend,null).commit();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment_activity_main, lend, "fragment_screen");
        ft.commit();
    }

    public void goToBorrow() {
        Borrow borrow = new Borrow();
//        FragmentManager fm = getActivity().getSupportFragmentManager();
////        fm.beginTransaction().hide(HomeFragment.this);
//        fm.beginTransaction().replace(R.id.nav_host_fragment_activity_main,  borrow,null).commit();
////                .replace(R.id.nav_host_fragment_activity_main, Borrow.class, null)
////                .addToBackStack(null)
////                .commit();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment_activity_main, borrow, "fragment_screen");
        ft.commit();

    }
}