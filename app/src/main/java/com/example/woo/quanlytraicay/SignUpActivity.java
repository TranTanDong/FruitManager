package com.example.woo.quanlytraicay;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private AutoCompleteTextView edt_SU_Email;
    private EditText edt_SU_Pass;
    private Button btn_SU_sugnUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void addControls() {

        edt_SU_Email    = findViewById(R.id.edt_SU_Email);
        edt_SU_Pass     = findViewById(R.id.edt_SU_Pass);
        btn_SU_sugnUp   = findViewById(R.id.btn_SU_signUp);
    }

    private void addEvents() {
        btn_SU_sugnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sign_Up();
            }
        });
    }

    private void Sign_Up() {
        String email = edt_SU_Email.getText().toString();
        String password = edt_SU_Pass.getText().toString();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(SignUpActivity.this, "Bạn chưa nhập email!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(SignUpActivity.this, "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }
}
