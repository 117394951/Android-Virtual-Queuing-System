//package com.app.is4401.sociallysafe.User;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//import com.app.is4401.sociallysafe.R;
//import com.app.is4401.sociallysafe.activity.MainActivity;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//
//public class User_Main extends AppCompatActivity {
//
//
//    private BottomNavigationView bottomNavigationView;
//
//    public User_Main() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_user__main);
//
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
//
//        startActivity(new Intent);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MainActivity()).commit();
//
//    }
//
//
//    private  final BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment selectedFragment = null;
//
//                    switch(item.getItemId()){
//                        case R.id.firstFragment:
//                        selectedFragment = new User_MyQueues();
//                        break;
//                        case R.id.secondFragment:
//                            selectedFragment = new MainActivity();
//                            break;
////                        case R.id.thirdFragment:
////                            selectedFragment = new User_Profile();
////                            break;
//                    }
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
//                    return true;
//                }
//            };
//}