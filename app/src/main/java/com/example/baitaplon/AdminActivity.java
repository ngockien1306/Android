package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.Adapter.UserAdapter;
import com.example.baitaplon.Database.DatabaseHelper;
import com.example.baitaplon.model.Order;
import com.example.baitaplon.model.User;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    RecyclerView recyclerAccounts;
    UserAdapter adapter;
    List<User> userList;
    DatabaseHelper db;

    // 3 nút chức năng
    Button btnOrderManagement, btnProductManagement, btnRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Khởi tạo DB
        db = new DatabaseHelper(this);

        // Ánh xạ RecyclerView
        recyclerAccounts = findViewById(R.id.recyclerAccounts);
        recyclerAccounts.setLayoutManager(new LinearLayoutManager(this));
        loadUsers();

        // Ánh xạ các nút
        btnOrderManagement = findViewById(R.id.btnOrderManagement);
        btnProductManagement = findViewById(R.id.btnProductManagement);
        btnRevenue = findViewById(R.id.btnRevenue);

        // Bắt sự kiện
        btnOrderManagement.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, OrderActivity.class));

        });

        btnProductManagement.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AdminProductActivity.class));
        });

        btnRevenue.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, RevenueActivity.class));
        });
    }

    // Load danh sách người dùng
    private void loadUsers() {
        userList = db.getAllUsers();
        adapter = new UserAdapter(userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onDelete(User user) {
                db.deleteUser(user.getEmail());
                Toast.makeText(AdminActivity.this, "Đã xóa: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                loadUsers(); // reload
            }
        });
        recyclerAccounts.setAdapter(adapter);
    }
}
