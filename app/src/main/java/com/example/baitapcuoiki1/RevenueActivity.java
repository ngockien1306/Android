package com.example.baitapcuoiki1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.model.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RevenueActivity extends AppCompatActivity {

    TextView txtTotalOrders, txtTotalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        // Ánh xạ các view từ layout
        txtTotalOrders = findViewById(R.id.txtTotalOrders);
        txtTotalRevenue = findViewById(R.id.txtTotalRevenue);

        // Khởi tạo database
        DatabaseHelper db = new DatabaseHelper(this);

        // Lấy danh sách tất cả đơn hàng
        List<Order> orders = db.getAllOrders();

        // Tổng số đơn
        int totalOrders = orders.size();

        // Tính tổng doanh thu từ tất cả đơn hàng
        int totalRevenue = 0;
        for (Order order : orders) {
            totalRevenue += order.getPrice();
        }

        // Format tiền theo chuẩn VNĐ
        String formattedRevenue = NumberFormat.getInstance(new Locale("vi", "VN")).format(totalRevenue) + " đ";

        // Hiển thị lên giao diện
        txtTotalOrders.setText("Tổng đơn: " + totalOrders);
        txtTotalRevenue.setText("Doanh thu: " + formattedRevenue);
    }
}
