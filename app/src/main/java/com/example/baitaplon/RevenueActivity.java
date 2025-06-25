package com.example.baitaplon;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitaplon.Database.DatabaseHelper;
import com.example.baitaplon.model.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RevenueActivity extends AppCompatActivity {

    TextView txtTotalRevenue;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_revenue);

        txtTotalRevenue = findViewById(R.id.txtTotalRevenue);  // ✅ sửa lại: findViewById
        db = new DatabaseHelper(this);

        calculateRevenue();
    }

    private void calculateRevenue() {
        List<Order> orders = db.getAllOrders();
        int total = 0;
        for (Order o : orders) {
            total += o.getPrice() * o.getQuantity();
        }

        // ✅ Format tiền theo VNĐ
        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formatted = format.format(total) + " đ";

        txtTotalRevenue.setText("Doanh thu: " + formatted);
    }
}
