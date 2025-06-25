package com.example.baitapcuoiki1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.Adapter.OrderAdapter;
import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.model.Order;

import java.util.List;

public class AdminOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order); // bạn sẽ tạo layout này

        recyclerView = findViewById(R.id.recyclerAdminOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = new DatabaseHelper(this);

        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = db.getAllOrders();
        orderAdapter = new OrderAdapter(orders, order -> {
            db.deleteOrderByProductId(order.getProductId());
            Toast.makeText(this, "Đã hủy đơn hàng: " + order.getName(), Toast.LENGTH_SHORT).show();
            loadOrders(); // refresh danh sách
        });
        recyclerView.setAdapter(orderAdapter);
    }
}
