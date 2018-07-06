package com.example.woo.quanlytraicay.fruitmanager;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.abstracts.FBDatabase;
import com.example.woo.quanlytraicay.model.User;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {
    private TextView tvAcName, tvAcPhone, tvAcAddress, tvAcEmail;
    private CardView btnAcUpdate;

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

    //Set thông tin Account
    private void loadInfoUser() {
        mData.child("USER").addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = mAuth.getCurrentUser().getEmail().toString();
                User user = dataSnapshot.getValue(User.class);
                if (email.equals(user.getEmail())){
                    tvAcName.setText(user.getName());
                    tvAcPhone.setText(user.getPhone());
                    tvAcAddress.setText(user.getAddress());
                    tvAcEmail.setText(user.getEmail());
                }
            }
        });
    }

    //Ánh xạ
    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_Account);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth   = FirebaseAuth.getInstance();
        mData   = FirebaseDatabase.getInstance().getReference();

        tvAcName       = findViewById(R.id.tvAcName);
        tvAcPhone      = findViewById(R.id.tvAcPhone);
        tvAcAddress    = findViewById(R.id.tvAcAddress);
        tvAcEmail      = findViewById(R.id.tvAcEmail);

        btnAcUpdate    = findViewById(R.id.btnAcUpdate);


    }

    private void addEvents() {

        //Cập nhật thông tin User
        btnAcUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyEdit();
            }
        });
    }

    //Xử lý cập nhật User
    private void xuLyEdit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_account, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        //Ánh xạ
        final EditText edtEdName, edtEdPhone, edtEdAddress;
        edtEdName     = dialogView.findViewById(R.id.edtEdName);
        edtEdPhone    = dialogView.findViewById(R.id.edtEdPhone);
        edtEdAddress  = dialogView.findViewById(R.id.edtEdAddress);

        //Set dữ liệu
        edtEdName.setText(tvAcName.getText().toString());
        edtEdPhone.setText(tvAcPhone.getText().toString());
        edtEdAddress.setText(tvAcAddress.getText().toString());

        builder.setPositiveButton("CẬP NHẬT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mName = edtEdName.getText().toString();
                String mPhone = edtEdPhone.getText().toString();
                String mAddress  = edtEdAddress.getText().toString();

                if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mAddress)){
                    Toast.makeText(AccountActivity.this, R.string.toast_check_info, Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USER").child(mAuth.getCurrentUser().getUid().toString());
                    User user = new User(mName, mPhone, mAddress, mAuth.getCurrentUser().getEmail());
                    databaseReference.setValue(user);
                    loadInfoUser();
                    MainActivity.tvHiUser.setText(mName);
                    dialog.dismiss();
                    Toast.makeText(AccountActivity.this, R.string.toast_update_info, Toast.LENGTH_SHORT).show();
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
