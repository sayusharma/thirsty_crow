package com.e.thirstycrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.e.thirstycrow.Common.Common;
import com.e.thirstycrow.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private EditText first,last,email;
    private Button btnSaveDetails;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference table_user;
    private String userPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        first = findViewById(R.id.firstNameEditText);
        last = findViewById(R.id.lastNameEditText);
        email = findViewById(R.id.emailEditText);
        btnSaveDetails = findViewById(R.id.btnSaveDetails);
        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("users");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        editor.putBoolean("with",false);
        editor.putBoolean("without",false);
        editor.apply();
        userPhone = SaveSharedPreference.getUserName(ProfileActivity.this);
        btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = new User(first.getText().toString(),last.getText().toString(),email.getText().toString());
                        table_user.child(userPhone).setValue(user);
                        Common.currentUser = user;
                        Intent intent = new Intent(ProfileActivity.this,DashDrawerActivity.class);
                        intent.putExtra("name",first.getText().toString());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}