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
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.R;
import com.example.woo.quanlytraicay.abstracts.DrawableClickListener;
import com.example.woo.quanlytraicay.abstracts.TextChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView edtLoginEmail;
    private EditText edtLoginPass;
    private CardView btnLogin;
    private TextView tvLoginSignUp;
    private ProgressDialog progressDialog;

    public static final int REQUEST_CODE_LOGIN_SIGNUP = 1;
    public static final int RESULT_CODE_LOGIN_SIGNUP = 2;
    private boolean mFlagUpdate = true;

    private FirebaseAuth mAuth;
    private DatabaseReference mData;

    private String email, mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();

    }

    //Kiểm tra tình trạng đăng nhập
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser, mFlagUpdate);
    }

    /**
     * Cập nhật User nếu đã có đăng nhập lần trước thì vào thẳng Trang chủ
     * @param currentUser tham số truyền vào thể hiện thông tin User
     * @param mFlag biến kiểm tra
     */
    private void updateUI(FirebaseUser currentUser, boolean mFlag) {
        if (currentUser != null && mFlag == true) {
            Intent lIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(lIntent);
            finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * Nhận dữ liệu từ SignUpActivity khi đăng ký thành công
         * Set giá trị cho Email
         */
        if (requestCode == REQUEST_CODE_LOGIN_SIGNUP && resultCode == RESULT_CODE_LOGIN_SIGNUP && data != null){
            String email = data.getStringExtra("EMAIL");
            mFlagUpdate = false;
            edtLoginEmail.setText(email);
            Toast.makeText(this, R.string.toast_request_login, Toast.LENGTH_SHORT).show();
        }
    }


    //Ánh xạ
    private void addControls() {
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        edtLoginEmail = findViewById(R.id.edt_LI_Email);
        edtLoginPass = findViewById(R.id.edt_LI_Pass);
        btnLogin = findViewById(R.id.btn_LI_Login);
        tvLoginSignUp = findViewById(R.id.tv_LI_SignUp);
        progressDialog = new ProgressDialog(this);

        tvLoginSignUp.setPaintFlags(tvLoginSignUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        delEditText(edtLoginEmail, R.drawable.ic_mail, R.drawable.ic_del);
        showPassword(edtLoginPass, R.drawable.ic_lock, R.drawable.ic_show, R.drawable.ic_show_off);

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


    private void addEvents() {
        //Link chuyển sang Activity đăng ký
       tvLoginSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, SignUpActivity.class), REQUEST_CODE_LOGIN_SIGNUP);
            }
        });

        //Nút đăng nhập
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangNhap();
            }
        });
    }

    //Xử lý đăng nhập
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
                                updateUI(user, true);
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

    //Hiển thị thông báo kiểm tra mạng
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

    //Kiểm tra mạng
    private boolean isNetWorkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}

