package com.e.thirstycrow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentError extends AppCompatActivity {
    private Button btnDashboardErr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_error);
        btnDashboardErr = findViewById(R.id.btnErrorDashboard);
        btnDashboardErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentError.this,DashDrawerActivity.class));
                finish();
            }
        });
    }
}