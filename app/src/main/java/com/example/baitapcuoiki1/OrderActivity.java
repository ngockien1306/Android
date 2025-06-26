package com.example.baitapcuoiki1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.Adapter.OrderAdapter;
import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recyclerOrders;
    OrderAdapter orderAdapter;
    DatabaseHelper dbHelper;
    List<Order> orders = new ArrayList<>();
    Button btnHome;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        btnHome = findViewById(R.id.btnHome);

        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);


        userEmail = getIntent().getStringExtra("email");

        if (userEmail != null && !userEmail.isEmpty()) {
            orders = dbHelper.getOrdersByEmail(userEmail);  // ← LỌC THEO EMAIL
        } else {
            Toast.makeText(this, "Không xác định được người dùng", Toast.LENGTH_SHORT).show();
        }

        // Truyền danh sách và xử lý xóa vào adapter
        orderAdapter = new OrderAdapter(orders, order -> {
            dbHelper.deleteOrderByProductId(order.getProductId());
            orders.remove(order);
            orderAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
        });

        recyclerOrders.setAdapter(orderAdapter);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });
    }
}
