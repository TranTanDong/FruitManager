package com.example.woo.quanlytraicay.FruitManager;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        loadInfoUser();
        addEvents();
    }

    private void loadInfoUser() {
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

    private void addControls() {
        mAuth   = FirebaseAuth.getInstance();
        mData   = FirebaseDatabase.getInstance().getReference();

        tv_acName       = findViewById(R.id.tv_acName);
        tv_acPhone      = findViewById(R.id.tv_acPhone);
        tv_acAddress    = findViewById(R.id.tv_acAddress);
        tv_acEmail      = findViewById(R.id.tv_acEmail);

        btn_acUpdate    = findViewById(R.id.btn_acUpdate);


    }

    private void addEvents() {

        //Cập nhật thông tin User
        btn_acUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyEdit();
            }
        });
    }

    private void xuLyEdit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_account, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText edt_ED_Name, edt_ED_Phone, edt_ED_Address;
        edt_ED_Name     = dialogView.findViewById(R.id.edt_ED_Name);
        edt_ED_Phone    = dialogView.findViewById(R.id.edt_ED_Phone);
        edt_ED_Address  = dialogView.findViewById(R.id.edt_ED_Address);

        //Set dữ liệu
        edt_ED_Name.setText(tv_acName.getText().toString());
        edt_ED_Phone.setText(tv_acPhone.getText().toString());
        edt_ED_Address.setText(tv_acAddress.getText().toString());

        builder.setPositiveButton("CẬP NHẬT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ten = edt_ED_Name.getText().toString();
                String sdt = edt_ED_Phone.getText().toString();
                String dc  = edt_ED_Address.getText().toString();

                if (TextUtils.isEmpty(ten) || TextUtils.isEmpty(sdt) || TextUtils.isEmpty(dc)){
                    Toast.makeText(AccountActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USER").child(mAuth.getCurrentUser().getUid().toString());
                    User user = new User(ten, sdt, dc, mAuth.getCurrentUser().getEmail());
                    databaseReference.setValue(user);
                    loadInfoUser();
                    dialog.dismiss();
                    Toast.makeText(AccountActivity.this, "Đã cập nhật thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
