package com.example.baitapcuoiki1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.Adapter.UserAdapter;
import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.model.User;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    RecyclerView recyclerAccounts;
    UserAdapter adapter;
    List<User> userList;
    DatabaseHelper db;

    // Thêm 3 nút quản lý
    Button btnOrderManagement, btnProductManagement, btnRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Ánh xạ các view
        recyclerAccounts = findViewById(R.id.recyclerAccounts);
        recyclerAccounts.setLayoutManager(new LinearLayoutManager(this));

        btnOrderManagement = findViewById(R.id.btnOrderManagement);
        btnProductManagement = findViewById(R.id.btnProductManagement);
        btnRevenue = findViewById(R.id.btnRevenue);

        db = new DatabaseHelper(this);
        loadUsers();

        // ===== Xử lý sự kiện các nút quản lý =====
        btnOrderManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AdminOrderActivity.class);
            startActivity(intent);
        });

        btnProductManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AdminProductActivity.class);
            startActivity(intent);
        });

        btnRevenue.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, RevenueActivity.class);
            startActivity(intent);
        });
    }

    // Load danh sách tài khoản
    private void loadUsers() {
        userList = db.getAllUsers();
        adapter = new UserAdapter(userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onDelete(User user) {
                db.deleteUser(user.getEmail());
                Toast.makeText(AdminActivity.this, "Đã xóa: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                loadUsers(); // Reload sau khi xóa
            }
        });
        recyclerAccounts.setAdapter(adapter);
    }
}
