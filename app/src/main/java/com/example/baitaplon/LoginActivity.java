package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitaplon.Database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    EditText edtemail, edtpass;
    Button btndangki, btndangnhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtpass = findViewById(R.id.edtpass);
        edtemail = findViewById(R.id.edtemail);
        btndangnhap = findViewById(R.id.btndangnhap);
        btndangki = findViewById(R.id.btndangki);

        DatabaseHelper db = new DatabaseHelper(this);

        btndangnhap.setOnClickListener(v -> {
            String email = edtemail.getText().toString().trim();
            String pass = edtpass.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkLogin(email, pass)) {
                int role = db.getUserRole(email); // Lấy quyền người dùng

                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                Intent intent;
                if (role == 1) {
                    // Nếu là admin, mở AdminActivity
                    intent = new Intent(LoginActivity.this, AdminActivity.class);
                } else {
                    // Nếu là người dùng thường, mở HomeActivity
                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                }

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        btndangki.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DangkiActivity.class);
            startActivity(intent);
        });
    }
}
