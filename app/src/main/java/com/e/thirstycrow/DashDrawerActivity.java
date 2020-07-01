package com.e.thirstycrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.e.thirstycrow.AboutUsFragment;
import com.e.thirstycrow.Common.Common;
import com.e.thirstycrow.FeedbackFragment;
import com.e.thirstycrow.HomeFragment;
import com.e.thirstycrow.Model.User;
import com.e.thirstycrow.OrderFragment;
import com.e.thirstycrow.PrivacyFragment;
import com.e.thirstycrow.R;
import com.e.thirstycrow.TermsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class DashDrawerActivity extends AppCompatActivity {
    SNavigationDrawer sNavigationDrawer;
    private String userPhone;
    Class fragmentClass;
    private AppCompatRadioButton withBtn,withoutBtn;
    public static Fragment fragment;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_drawer);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userPhone = SaveSharedPreference.getUserName(DashDrawerActivity.this);
        table_user = firebaseDatabase.getReference("users");
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(userPhone).getValue(User.class);
                Common.currentUser = user;
                //setUserName(Objects.requireNonNull(user));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sNavigationDrawer = findViewById(R.id.navigationDrawer);

        //Creating a list of menu Items

        List<MenuItem> menuItems = new ArrayList<>();

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and then the second parameter is the image which will be the background of the menu item.

        menuItems.add(new MenuItem("Home",R.drawable.back));
        menuItems.add(new MenuItem("My Orders",R.drawable.back));
        menuItems.add(new MenuItem("About Us",R.drawable.back));
        menuItems.add(new MenuItem("Feedback/Complaints",R.drawable.back));
        menuItems.add(new MenuItem("Privacy Policy",R.drawable.back));
        menuItems.add(new MenuItem("Terms & Conditions",R.drawable.back));
        menuItems.add(new MenuItem("Logout",R.drawable.back));

        //then add them to navigation drawer

        sNavigationDrawer.setMenuItemList(menuItems);
        fragmentClass =  HomeFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }



        //Listener to handle the menu item click. It returns the position of the menu item clicked. Based on that you can switch between the fragments.

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position "+position);

                switch (position){
                    case 0:{
                        fragmentClass = HomeFragment.class;
                        break;
                    }
                    case 1:{
                        fragmentClass = OrderFragment.class;
                        break;
                    }
                    case 2:{
                        fragmentClass = AboutUsFragment.class;
                        break;
                    }
                    case 3:{
                        fragmentClass = FeedbackFragment.class;
                        break;
                    }
                    case 4:{
                        fragmentClass = PrivacyFragment.class;
                        break;
                    }
                    case 5:{
                        fragmentClass = TermsFragment.class;
                        break;
                    }
                    case 6:{
                        fragmentClass = LogoutFragment.class;
                        break;
                    }

                }

                //Listener for drawer events such as opening and closing.
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening(){

                    }

                    @Override
                    public void onDrawerClosing(){
                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State "+newState);
                    }
                });
            }
        });
    }
    public void onOrderNowPressed(View view){
        startActivity(new Intent(DashDrawerActivity.this,OrderActivity.class));
    }
}