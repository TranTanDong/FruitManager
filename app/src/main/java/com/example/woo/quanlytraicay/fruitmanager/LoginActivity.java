package com.example.woo.quanlytraicay.fruitmanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView edtLoginEmail;
    private EditText edtLoginPass;
    private CardView btnLogin;
    private TextView tvLoginSignUp;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    private String email, mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
            Intent lIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(lIntent);
            finish();

        }
    }

    private void addControls() {
        mAuth = FirebaseAuth.getInstance();

        edtLoginEmail = findViewById(R.id.edt_LI_Email);
        edtLoginPass = findViewById(R.id.edt_LI_Pass);
        btnLogin = findViewById(R.id.btn_LI_Login);
        tvLoginSignUp = findViewById(R.id.tv_LI_SignUp);
        progressDialog = new ProgressDialog(this);

        tvLoginSignUp.setPaintFlags(tvLoginSignUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

    }

    private void addEvents() {
        //Đăng ký
       tvLoginSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, SignUpActivity.class), 11);

            }
        });

        //Đăng nhập
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangNhap();
            }
        });
    }


    private void xuLyDangNhap() {
        email    = edtLoginEmail.getText().toString();
        mPassword = edtLoginPass.getText().toString();

        if (!isNetWorkConnected()){
            showDialog();
        }else if (TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, R.string.toast_mail_empty, Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(mPassword)){
            Toast.makeText(LoginActivity.this, R.string.toast_password_empty, Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Đang xác thực tài khoản");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.hide();
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.hide();
                                Toast.makeText(LoginActivity.this, R.string.toast_login_failed, Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.network_error);
        builder.setMessage(R.string.check_network);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isNetWorkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
