package com.app.is4401.sociallysafe.Admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.ThirdFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_Main extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    // code for navigation and fragment https://www.youtube.com/watch?v=tPV8xA7m-iw&t=838s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new AdminHomePage()).commit();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.firstFragment:
                            selectedFragment = new AdminHomePage();
                            break;
                        case R.id.secondFragment:
                            selectedFragment = new AdminManageQueue();
                            break;
                        case R.id.thirdFragment:
                            selectedFragment = new ThirdFragment();
                            break;


                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
                    return true;
                }
            };
}