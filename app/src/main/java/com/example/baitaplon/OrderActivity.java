package com.example.baitaplon;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.Adapter.OrderAdapter;
import com.example.baitaplon.Database.DatabaseHelper;
import com.example.baitaplon.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {
    RecyclerView recyclerOrders;
    OrderAdapter adapter;
    List<Order> orders;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        orders = db.getAllOrders();

        adapter = new OrderAdapter(orders, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onEdit(Order order) {
                showEditDialog(order);
            }

            @Override
            public void onDelete(Order order) {
                db.deleteOrder(order.getId());
                Toast.makeText(OrderActivity.this, "Đã xóa đơn", Toast.LENGTH_SHORT).show();
                reloadOrders();
            }
        });

        recyclerOrders.setAdapter(adapter);
    }

    void reloadOrders() {
        orders.clear();
        orders.addAll(db.getAllOrders());
        adapter.notifyDataSetChanged();
    }

    void showEditDialog(Order order) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_order, null);
        EditText edtName = view.findViewById(R.id.edtProductName);
        EditText edtQuantity = view.findViewById(R.id.edtQuantity);
        EditText edtPrice = view.findViewById(R.id.edtPrice);

        edtName.setText(order.getProductName());
        edtQuantity.setText(String.valueOf(order.getQuantity()));
        edtPrice.setText(String.valueOf(order.getPrice()));

        new AlertDialog.Builder(this)
                .setTitle("Sửa đơn hàng")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = edtName.getText().toString();
                    int quantity = Integer.parseInt(edtQuantity.getText().toString());
                    int price = Integer.parseInt(edtPrice.getText().toString());
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    db.updateOrder(order.getId(), name, quantity, price, date);
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    reloadOrders();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
