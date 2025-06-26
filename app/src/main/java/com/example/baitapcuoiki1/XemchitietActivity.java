package com.example.baitapcuoiki1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitapcuoiki1.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class XemchitietActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtName, txtPrice;
    Button btngiohang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemchitiet);

        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        btngiohang = findViewById(R.id.btngiohang);

        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            imgProduct.setImageResource(product.getImage());
            txtName.setText(product.getName());

            NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedPrice = format.format(product.getPrice()) + " đ";
            txtPrice.setText("Giá: " + product.getPrice() + " VNĐ");

            btngiohang.setOnClickListener(v -> {
                Toast.makeText(this, "Đã thêm vào giỏ hàng: " + product.getName(), Toast.LENGTH_SHORT).show();
                // TODO: Thêm logic lưu vào SQLite hoặc danh sách tạm ở đây
            });
        }
    }
}
