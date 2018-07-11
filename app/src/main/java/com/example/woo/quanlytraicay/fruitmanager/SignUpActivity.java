package com.example.woo.quanlytraicay.fruitmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.abstracts.DrawableClickListener;
import com.example.woo.quanlytraicay.abstracts.TextChangeListener;
import com.example.woo.quanlytraicay.model.User;
import com.example.woo.quanlytraicay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtSUName, edtSUPhone, edtSUAddress, edtSUPass, edtSURePass;
    private AutoCompleteTextView edtSUEmail;
    private CardView btnSUSignUp;
    private TextView tvSULogin;
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
    //Ánh xạ
    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_SU);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        edtSUName = findViewById(R.id.edt_SU_Name);
        edtSUPhone = findViewById(R.id.edt_SU_Phone);
        edtSUAddress = findViewById(R.id.edt_SU_Address);
        edtSUEmail = findViewById(R.id.edt_SU_Email);
        edtSUPass = findViewById(R.id.edt_SU_Pass);
        edtSURePass = findViewById(R.id.edt_SU_RePass);

        btnSUSignUp = findViewById(R.id.btn_SU_SignUp);

        tvSULogin = findViewById(R.id.tv_SU_Login);
        tvSULogin.setPaintFlags(tvSULogin.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        delEditText(edtSUName, R.drawable.ic_person, R.drawable.ic_del);
        delEditText(edtSUPhone, R.drawable.ic_phone, R.drawable.ic_del);
        delEditText(edtSUAddress, R.drawable.ic_address, R.drawable.ic_del);
        delEditText(edtSUEmail, R.drawable.ic_mail, R.drawable.ic_del);
        showPassword(edtSUPass, R.drawable.ic_lock, R.drawable.ic_show, R.drawable.ic_show_off);
        showPassword(edtSURePass, R.drawable.ic_repass, R.drawable.ic_show, R.drawable.ic_show_off);
    }

    /**
     * Xóa toàn bộ string trong EditText
     * @param editText tham sô truyền vào EditText
     * @param leftDrawable tham số truyền vào hình
     * @param rightDrawable tham số truyền vào hình xóa Text
     */
    private void delEditText(final EditText editText, final int leftDrawable, final int rightDrawable){
        editText.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(editText) {
            @Override
            public boolean onDrawableClick() {
                editText.setText("");
                return true;
            }
        });

        editText.addTextChangedListener(new TextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    editText.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
                }else {
                    editText.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawable, 0);
                }
                super.onTextChanged(s, start, before, count);
            }
        });

        editText.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
    }
    /**
     * Hiển thị hoặc ẩn Password
     * @param editText thông sô truyền vào EditText (Password)
     * @param leftDrawable tham số truyền vào hình bên trái
     * @param rightDrawable tham số truyền vào hình hiển thị Password
     * @param rightDrawableOff tham số truyền vào hình ẩn Password
     */
    private void showPassword(final EditText editText, final int leftDrawable, final int rightDrawable, final int rightDrawableOff){
        editText.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(editText) {
            @Override
            public boolean onDrawableClick() {
                if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    editText.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawableOff, 0);
                }else {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editText.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawable, 0);
                }
                return true;
            }
        });
    }

    //Các sự kiện click
    private void addEvents() {
        //Về trang đăng nhập
        tvSULogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        //Xử lý đăng ký
        btnSUSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetWorkConnected()){
                    showDialog();
                }else {
                    String name     = edtSUName.getText().toString();
                    String phone    = edtSUPhone.getText().toString();
                    String address  = edtSUAddress.getText().toString();
                    String mail     = edtSUEmail.getText().toString();
                    String pass     = edtSUPass.getText().toString();
                    String repass   = edtSURePass.getText().toString();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)){
                        Toast.makeText(SignUpActivity.this, R.string.toast_check_info, Toast.LENGTH_SHORT).show();
                    }else if (!pass.equals(repass)){
                        Toast.makeText(SignUpActivity.this, R.string.toast_repasswork, Toast.LENGTH_SHORT).show();
                        edtSUPass.setText(null);
                        edtSURePass.setText(null);
                    }else {
                        signUp(mail, pass, name, phone, address);
                    }
                }
            }
        });
    }

    /**
     * Đăng ký tài khoản mới
     * @param email tham số truyền vào Email
     * @param password tham số truyền vào Mật khẩu
     * @param name tham số truyền vào tên
     * @param phone tham số truyền vào số điện thoại
     * @param address tham số truyền vào địa chỉ
     */
    private void signUp(final String email, String password, final String name, final String phone, final String address) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    String UID = user.getUid();
                    User user1 = new User(name, phone, address, email);
                    mDatabase.child("USER").child(UID).setValue(user1);
                    String result = "Đăng ký thành công!";
                    showDialogResult(result, email);
                }
                else {
                    String result = "Email đã tồn tại. Vui lòng nhập email khác!";
                    showDialogResult(result, email);
                }
            }
        });
    }

    /**
     * Hiển thị kết quả đăng ký nếu thành công thì gửi Email sang LoginActivity
     * @param result tham số truyền vào kết quả
     * @param email tham số truyền vào Email
     */
    private void showDialogResult(final String result, final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.result_sign_up);
        builder.setMessage(result);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(result.equals(R.string.toast_sign_up_success)) {
                    Intent intent = getIntent();
                    intent.putExtra("EMAIL", email);
                    setResult(LoginActivity.RESULT_CODE_LOGIN_SIGNUP, intent);
                    finish();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
