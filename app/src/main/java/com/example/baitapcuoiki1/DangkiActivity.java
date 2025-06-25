package com.example.baitapcuoiki1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.R;


public class DangkiActivity extends AppCompatActivity {

    EditText edtemail, edtpass, edtConfirm;
    Button btndangkia;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);

        // Ánh xạ view
        edtemail = findViewById(R.id.edtemail);
        edtpass = findViewById(R.id.edtpass);
        edtConfirm = findViewById(R.id.edtConfirm);
        btndangkia = findViewById(R.id.btndangkia);

        dbHelper = new DatabaseHelper(this);

        btndangkia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtemail.getText().toString().trim();
                String pass = edtpass.getText().toString().trim();
                String confirm = edtConfirm.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(DangkiActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass.equals(confirm)) {
                    Toast.makeText(DangkiActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isRegistered = dbHelper.registerUser(email, pass);
                if (isRegistered) {
                    Toast.makeText(DangkiActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    // Quay về màn hình đăng nhập
                    Intent intent = new Intent(DangkiActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DangkiActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
