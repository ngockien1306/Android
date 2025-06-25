package com.example.baitaplon; // Khai báo package của dự án

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitaplon.model.Product; // Import model Product đã tạo

public class XemchitietActivity extends AppCompatActivity {

    // Khai báo các view trong layout XML
    ImageView imgProduct;
    TextView txtName, txtPrice;
    Button btngiohang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemchitiet); // Gán layout XML cho Activity

        // Ánh xạ view trong XML vào biến trong code
        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        btngiohang = findViewById(R.id.btngiohang);

        // Nhận đối tượng Product được truyền qua từ Intent (từ HomeActivity hoặc ProductAdapter)
        Product product = (Product) getIntent().getSerializableExtra("product");

        // Nếu sản phẩm tồn tại, hiển thị dữ liệu lên giao diện
        if (product != null) {
            imgProduct.setImageResource(product.getImage()); // Hiển thị hình sản phẩm
            txtName.setText(product.getName()); // Hiển thị tên sản phẩm
            txtPrice.setText("Giá: " + product.getPrice() + "₫"); // Hiển thị giá sản phẩm
        }

        // Sự kiện click nút "Thêm vào giỏ hàng"
        btngiohang.setOnClickListener(v -> {
            // Hiển thị thông báo khi nhấn nút
            Toast.makeText(XemchitietActivity.this, "Đã thêm vào giỏ hàng: " + product.getName(), Toast.LENGTH_SHORT).show();

            // TODO: Bạn có thể thêm logic thêm vào giỏ hàng ở đây
            // Ví dụ: lưu vào SQLite, SharedPreferences, hoặc list tạm thời
        });
    }
}
