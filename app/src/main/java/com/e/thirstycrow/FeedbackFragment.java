package com.e.thirstycrow;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.e.thirstycrow.Common.Common;
import com.e.thirstycrow.Model.Feedback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {
    private EditText subject,description;
    private Button btnSubmitCom;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAnalytics firebaseAnalytics;
    private DatabaseReference databaseReference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedbackFragment() {
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
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);
        subject = view.findViewById(R.id.editTextSubject);
        btnSubmitCom = view.findViewById(R.id.btnSubmitComplaint);
        description = view.findViewById(R.id.editTextDescription);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("complaints").child("pending");
        btnSubmitCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(subject.getText())){
                    Toast.makeText(getContext(),"SUBJECT CANNOT BE EMPTY",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (TextUtils.isEmpty(description.getText())){
                        Toast.makeText(getContext(),"DESCRIPTION CANNOT BE EMPTY",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String id = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        Feedback feedback = new Feedback(subject.getText().toString(), SaveSharedPreference.getUserName(getContext()),Common.currentUser.getFirst(),description.getText().toString(),id);
                        databaseReference.child(id).setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                description.setText("");
                                subject.setText("");
                                Bundle bundle = new Bundle();
                                bundle.putString("User",SaveSharedPreference.getUserName(getContext()));
                                bundle.putString("DateTime",Calendar.getInstance().getTime().toString());
                                bundle.putString("Subject",subject.getText().toString());
                                bundle.putString("Description",description.getText().toString());
                                firebaseAnalytics.logEvent("FeedbackComplaint",bundle);
                                Toast.makeText(getContext(),"COMPLAINT REGISTERED SUCCESSFULLY",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
        return view;
    }
}