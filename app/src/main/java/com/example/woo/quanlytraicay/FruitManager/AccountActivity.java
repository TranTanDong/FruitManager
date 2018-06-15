package com.example.woo.quanlytraicay.FruitManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.example.woo.quanlytraicay.Model.User;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {
    private TextView tv_acName, tv_acPhone, tv_acAddress, tv_acEmail;
    private CardView btn_acUpdate;

    private FirebaseAuth mAuth;
    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        addControls();
        addEvents();
    }

    private void addControls() {
        mAuth   = FirebaseAuth.getInstance();
        mData   = FirebaseDatabase.getInstance().getReference();

        tv_acName       = findViewById(R.id.tv_acName);
        tv_acPhone      = findViewById(R.id.tv_acPhone);
        tv_acAddress    = findViewById(R.id.tv_acAddress);
        tv_acEmail      = findViewById(R.id.tv_acEmail);

        btn_acUpdate    = findViewById(R.id.btn_acUpdate);

        //Set thông tin Account
        mData.child("USER").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = mAuth.getCurrentUser().getEmail().toString();
                User user = dataSnapshot.getValue(User.class);
                if (email.equals(user.getEmail())){
                    tv_acName.setText(user.getName());
                    tv_acPhone.setText(user.getPhone());
                    tv_acAddress.setText(user.getAddress());
                    tv_acEmail.setText(user.getEmail());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addEvents() {

    }
}
