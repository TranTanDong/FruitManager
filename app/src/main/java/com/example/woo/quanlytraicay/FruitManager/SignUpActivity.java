package com.example.woo.quanlytraicay.FruitManager;

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
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.Model.User;
import com.example.woo.quanlytraicay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText edt_SU_Name, edt_SU_Phone, edt_SU_Address, edt_SU_Pass, edt_SU_RePass;
    private AutoCompleteTextView edt_SU_Email;
    private CardView btn_SU_SignUp;
    private TextView tv_SU_Login;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addControls();
        addEvents();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addControls() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        edt_SU_Name     = findViewById(R.id.edt_SU_Name);
        edt_SU_Phone    = findViewById(R.id.edt_SU_Phone);
        edt_SU_Address  = findViewById(R.id.edt_SU_Address);
        edt_SU_Email    = findViewById(R.id.edt_SU_Email);
        edt_SU_Pass     = findViewById(R.id.edt_SU_Pass);
        edt_SU_RePass   = findViewById(R.id.edt_SU_RePass);

        btn_SU_SignUp   = findViewById(R.id.btn_SU_SignUp);

        tv_SU_Login     = findViewById(R.id.tv_SU_Login);
        tv_SU_Login.setPaintFlags(tv_SU_Login.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    private void addEvents() {
        //Về trang đăng nhập
        tv_SU_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        //Xử lý đăng ký
        btn_SU_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetWorkConnected()){
                    showDialog();
                }else {
                    String name     = edt_SU_Name.getText().toString();
                    String phone    = edt_SU_Phone.getText().toString();
                    String address  = edt_SU_Address.getText().toString();
                    String mail     = edt_SU_Email.getText().toString();
                    String pass     = edt_SU_Pass.getText().toString();
                    String repass   = edt_SU_RePass.getText().toString();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)){
                        Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    }else if (!pass.equals(repass)){
                        Toast.makeText(SignUpActivity.this, "Mật khẩu nhập lại không đúng!", Toast.LENGTH_SHORT).show();
                        edt_SU_Pass.setText(null);
                        edt_SU_RePass.setText(null);
                    }else {
                        signUp(mail, pass, name, phone, address);
                    }
                }
            }
        });
    }

    private void signUp(final String email, String password, final String name, final String phone, final String address) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    String UID = user.getUid();
                    Log.i("UID111", UID);
                    User user1 = new User(name, phone, address, email);
                    mDatabase.child("USER").child(UID).setValue(user1);
                    String result = "Đăng ký thành công!";
                    showDialogResult(result);
                }
                else {
                    String result = "Email đã tồn tại. Vui lòng nhập email khác!";
                    showDialogResult(result);
                }
            }
        });
    }

    private void showDialogResult(final String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả đăng ký");
        builder.setMessage(result);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(result.equals("Đăng ký thành công!")) {
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi mạng");
        builder.setMessage("Không có mạng. Vui lòng kiểm tra lại!");
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
