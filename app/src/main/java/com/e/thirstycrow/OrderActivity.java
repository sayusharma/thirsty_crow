package com.e.thirstycrow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e.thirstycrow.Common.Common;
import com.e.thirstycrow.Model.Order;
import com.e.thirstycrow.Model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.e.thirstycrow.R.drawable.ic_baseline_check_box_24;
import static com.e.thirstycrow.R.drawable.ic_baseline_check_box_outline_blank_24;

public class OrderActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = OrderActivity.class.getSimpleName();
    private TextView textQtyWithCan;
    private TextView textQtyWithoutCan;
    private Spinner selectTimeSpinner;
    private RelativeLayout selectDateLay;
    private Context context;
    private TextView selectDateText;
    private FirebaseAnalytics firebaseAnalytics;
    private Button btnPayNow;
    private TextView instantlyText,sameAsPrevAddress;
    private FirebaseDatabase firebaseDatabase;
    private boolean hasAddress=false;
    private DatabaseReference table_user;
    private EditText addressEditText;
    private String del_date,del_time,amount;
    private String prevAddress;
    private String currentAddress;
    private Button sameAsPrevBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        context = this;
        Checkout.preload(OrderActivity.this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference().child("users").child(SaveSharedPreference.getUserName(OrderActivity.this));
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("address").exists()){
                    hasAddress=true;
                    prevAddress = (String) dataSnapshot.child("address").getValue();
                    //Toast.makeText(getApplicationContext(),""+prevAddress,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addressEditText = findViewById(R.id.editTextAddress);
        sameAsPrevBtn = findViewById(R.id.btnSameAsPrev);
        sameAsPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasAddress){
                    addressEditText.setText(prevAddress);
                }
                else {
                    Toast.makeText(getApplicationContext(),"NO PREVIOUS ORDER FOUND",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnPayNow = findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startPayment();
                if (validationQuantity()){
                    if (validationDeliveryDate()){
                        if (validationDeliveryTime()){
                            if (validationAddress()){
                                // validation complete
                                TextView t1 = findViewById(R.id.totalAmountPayable);
                                amount = t1.getText().toString();
                                TextView t2 = findViewById(R.id.selectDateText);
                                del_date = t2.getText().toString();
                                EditText editText = findViewById(R.id.editTextAddress);
                                currentAddress = editText.getText().toString();
                                if (instantlyText.getVisibility() == View.VISIBLE){
                                    del_time = "ASAP";
                                }
                                else{
                                    del_time = selectTimeSpinner.getSelectedItem().toString();
                                }
                                startPayment(amount);
                            }
                            else Toast.makeText(getApplicationContext(),"PLEASE ENTER ADDRESS",Toast.LENGTH_LONG).show();
                        }
                        else Toast.makeText(getApplicationContext(),"PLEASE SELECT DELIVERY TIME",Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(getApplicationContext(),"PLEASE SELECT DELIVERY DATE",Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getApplicationContext(),"PLEASE SELECT QUANTITY",Toast.LENGTH_LONG).show();
            }
        });
        textQtyWithCan = findViewById(R.id.withCanQuantity);
        selectTimeSpinner = findViewById(R.id.selectTimeSpinner);
        selectDateLay = findViewById(R.id.selectDateLayout);
        selectDateText = findViewById(R.id.selectDateText);
        instantlyText = findViewById(R.id.textInstantly);
        selectDateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instantlyText.setVisibility(View.INVISIBLE);
                selectTimeSpinner.setVisibility(View.VISIBLE);
                final Calendar currentDate = Calendar.getInstance();
                final int day = currentDate.get(Calendar.DAY_OF_MONTH);
                final int month = currentDate.get(Calendar.MONTH);
                final int year = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        java.sql.Date date1 = new java.sql.Date(i-1900,i1,i2);
                        selectDateText.setText(date1.toString());
                       if (i1==month && i2==day){
                           instantlyText.setVisibility(View.VISIBLE);
                           selectTimeSpinner.setVisibility(View.INVISIBLE);
                       }
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
                currentDate.add(Calendar.DAY_OF_MONTH,6);
                datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(OrderActivity.this,
                R.array.time, android.R.layout.simple_spinner_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTimeSpinner.setAdapter(adapters);
        textQtyWithoutCan = findViewById(R.id.withoutCanQuantity);

    }
    public boolean validationDeliveryTime(){
        TextView textView = findViewById(R.id.textInstantly);
        if (textView.getVisibility()==View.VISIBLE) return true;
        else{
            Spinner spinner = findViewById(R.id.selectTimeSpinner);
            if (spinner.getSelectedItem().toString().equals("Select Delivery Time")) return false;
            else return true;
        }
    }
    public boolean validationDeliveryDate(){
        TextView textView = findViewById(R.id.selectDateText);
        if (textView.getText().equals("Select Delivery Date")) return false;
        else return true;
    }
    public boolean validationAddress(){
        EditText editText = findViewById(R.id.editTextAddress);
        if (TextUtils.isEmpty(editText.getText())) return false;
        else return true;
    }
    public boolean validationQuantity(){
        int qtyWithoutCan = Integer.parseInt(textQtyWithoutCan.getText().toString());
        int qtyWithCan = Integer.parseInt(textQtyWithCan.getText().toString());
        if(qtyWithCan==0 && qtyWithoutCan==0){
            return false;
        }
        else
            return true;
    }
    public void startPayment(String amount) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_2WyuExm60JwHM5");
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.crow);

        /**
         * Reference to current activitya
         */
        final Activity activity = OrderActivity.this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Thirsty Crow");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Water Ordering App");
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", Integer.parseInt(amount)*100);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
    public void onQuantityChanged(View view){
        int qtyWithoutCan = Integer.parseInt(textQtyWithoutCan.getText().toString());
        int qtyWithCan = Integer.parseInt(textQtyWithCan.getText().toString());
        switch (view.getId()){
            case R.id.withCanMinus: {
                if(qtyWithCan!=0){
                    qtyWithCan=qtyWithCan-1;
                    textQtyWithCan.setText(String.valueOf(qtyWithCan));
                }
                break;
            }
            case R.id.withCanPlus: {
                qtyWithCan=qtyWithCan+1;
                textQtyWithCan.setText(String.valueOf(qtyWithCan));
                break;
            }
            case R.id.withoutCanMinus:{
                if(qtyWithoutCan!=0){
                    qtyWithoutCan=qtyWithoutCan-1;
                    textQtyWithoutCan.setText(String.valueOf(qtyWithoutCan));
                }
                break;
            }
            case R.id.withoutCanPlus:{
                qtyWithoutCan=qtyWithoutCan+1;
                textQtyWithoutCan.setText(String.valueOf(qtyWithoutCan));
                break;
            }
            default:
                Toast.makeText(getApplicationContext(),"INVALID SELECTION",Toast.LENGTH_LONG).show();
        }
        setPrice(qtyWithCan,qtyWithoutCan);
    }
    public void setPrice(int qtyWith,int qtyWithout){
        TextView textPriceWith = findViewById(R.id.priceJarIncluded);
        TextView textPriceWithout = findViewById(R.id.priceJarNotIncluded);
        TextView totalText = findViewById(R.id.totalAmountPayable);
        int priceWith = 200 * qtyWith;
        int priceWithout = 45* qtyWithout;
        int total = priceWith + priceWithout;
        textPriceWith.setText(String.valueOf(priceWith));
        textPriceWithout.setText(String.valueOf(priceWithout));
        totalText.setText(String.valueOf(total));
    }

    @Override
    public void onPaymentSuccess(String s) {
        final String payId = s;
        final Product with = new Product(textQtyWithCan.getText().toString(),String.valueOf(200*Integer.parseInt(textQtyWithCan.getText().toString())));
        final Product without = new Product(textQtyWithoutCan.getText().toString(),String.valueOf(45*Integer.parseInt(textQtyWithoutCan.getText().toString())));
        final DatabaseReference reference = firebaseDatabase.getReference().child("requests");
        final ArrayList<String> orderIDList = new ArrayList<>();
        orderIDList.add(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(orderIDList.get(orderIDList.size()-1)).exists()){
                    orderIDList.add(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(orderIDList.get(orderIDList.size()-1)).exists()){
                    orderIDList.add(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final Order newOrder = new Order(Common.currentUser.getFirst(),SaveSharedPreference.getUserName(this),currentAddress,s,del_date,del_time,with,without,amount,"pending",orderIDList.get(orderIDList.size()-1));
        final DatabaseReference reference1 = firebaseDatabase.getReference().child("users").child(SaveSharedPreference.getUserName(OrderActivity.this));
        reference.child(orderIDList.get(orderIDList.size()-1)).setValue(newOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reference1.child("address").setValue(currentAddress);
                        Bundle bundle = new Bundle();
                        bundle.putString("User",SaveSharedPreference.getUserName(context));
                        bundle.putString("Name",Common.currentUser.getFirst());
                        bundle.putString("DateTime",Calendar.getInstance().getTime().toString());
                        bundle.putString("OrderID",orderIDList.get(orderIDList.size()-1));
                        bundle.putString("PaymentID",payId);
                        bundle.putString("TotalAmount",amount);
                        bundle.putString("Address",currentAddress);
                        bundle.putString("DeliveryDate",del_date);
                        bundle.putString("DeliveryTime",del_time);
                        bundle.putString("QtyWithCan",with.getQty());
                        bundle.putString("QtyWithoutCan",without.getQty());
                        firebaseAnalytics.logEvent("OrderedSuccess",bundle);
                        Intent intent = new Intent(getApplicationContext(),PaymentSuccess.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Bundle bundle = new Bundle();
        bundle.putString("User",SaveSharedPreference.getUserName(context));
        bundle.putString("DateTime",Calendar.getInstance().getTime().toString());
        bundle.putString("ErrorCode",String.valueOf(i));
        bundle.putString("PaymentID",s);
        firebaseAnalytics.logEvent("OrderFailed",bundle);
        startActivity(new Intent(getApplicationContext(),PaymentError.class));
        finish();

    }

}