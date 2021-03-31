package com.app.sb.sbservices;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.app.sb.sbservices.Menu.BlankFragment;
import com.app.sb.sbservices.Orders.OrdersMainFragment;

import com.app.sb.sbservices.Profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(HomeFragment.newInstance("", ""));
//        // Call the function callInstamojo to start payment here
//        loadFragment(new HomeFragment());
//        BottomNavigationView navigation = findViewById(R.id.navigation);
//
//        navigation.setOnNavigationItemSelectedListener(this);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            openFragment(HomeFragment.newInstance("", ""));
                            return true;
                        case R.id.myOrders:
                            openFragment(OrdersMainFragment.newInstance("", ""));
                            return true;
                        case R.id.action_cart:
                            openFragment(EarnCreditsFragment.newInstance("", ""));
                            return true;
                        case R.id.profile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            return true;
                        case R.id.menu:
                            openFragment(BlankFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };
//    private boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }
//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        BottomNavActivity.this.finish();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Fragment fragment = null;
//
//        switch (menuItem.getItemId()) {
//            case R.id.home:
//                fragment = new HomeFragment();
//                break;
//            case R.id.myOrders:
//                fragment=new OrdersMainFragment();
//                break;
//
//            case R.id.action_cart:
//                fragment=new EarnCreditsFragment();
//                break;
//
//            case R.id.profile:
//                fragment=new ProfileFragment();
//                break;
//            case R.id.menu:
//                fragment= new BlankFragment();
//                break;
//
//        }
//        return loadFragment(fragment);
//    }

}
