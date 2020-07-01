package com.e.thirstycrow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.thirstycrow.Model.Order;
import com.e.thirstycrow.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ArrayList<Order> arrayList;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
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
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = view.findViewById(R.id.orderStatusRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(SaveSharedPreference.getUserName(getContext())).child("orders");
        Query query = databaseReference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(),"IN",Toast.LENGTH_SHORT).show();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Order order = dataSnapshot1.getValue(Order.class);
                            arrayList.add(order);
        //Toast.makeText(getContext(),"36",Toast.LENGTH_SHORT).show();
                    }
                    if (arrayList.isEmpty()){
                        Toast.makeText(getContext(),"NO ORDER FOUND",Toast.LENGTH_LONG).show();
                    }
                    else {
                        //Toast.makeText(getContext(),"IN2",Toast.LENGTH_SHORT).show();
                        Collections.reverse(arrayList);
                        orderAdapter = new OrderAdapter(getContext(), arrayList);
                        //Toast.makeText(getContext(),"IN3",Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(orderAdapter);
                    }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}