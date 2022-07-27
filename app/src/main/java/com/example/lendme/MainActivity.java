package com.example.lendme;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lendme.databinding.ActivityMainBinding;
import com.example.lendme.ui.borrow.ItemFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

//        if (extras != null) {
//            String fragment = extras.getString("fragment");
//            goToFragment(fragment);
//            // and get whatever type user account id is
//        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.borrow, R.id.lend_page, R.id.messageFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (extras != null) {
            String fragment = extras.getString("fragment");
            goToFragment(fragment);
            // and get whatever type user account id is
        }


    }

    private void goToFragment(String fragment) {
        String[] fragmentArray = fragment.split(",");
//        toastIt(fragment);
        Fragment fragment1 = null;
        Bundle bundle = new Bundle();
//        toastIt(fragmentArray[0]);
        if (fragmentArray[0].trim().equals("1"))
        {
//            toastIt("should navigate you");
            bundle.putString("key", fragmentArray[1]);
            ItemFragment itemFragment = new ItemFragment();
            itemFragment.setArguments(bundle);


            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, itemFragment)
                    .commit()
            ;

//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.nav_host_fragment_activity_main, itemFragment,"items").commit();
        }
    }
    private void toastIt(String msg){
        Toast.makeText(MainActivity.this,
                msg, Toast.LENGTH_LONG).show();
    }

}