package com.example.baitapcuoiki1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.Adapter.CartAdapter;
import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.model.Order;
import com.example.baitapcuoiki1.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView txtTotal;
    private Button btnThanhToan;

    private DatabaseHelper dbHelper;
    private CartAdapter cartAdapter;
    private List<Product> cartItems;
    private String userEmail; // Biến toàn cục để giữ email người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnThanhToan = findViewById(R.id.btnThanhToan);

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("email"); // ← Lưu đúng key như bên LoginActivity

        cartItems = dbHelper.getCartItems();

        cartAdapter = new CartAdapter(cartItems, product -> {
            dbHelper.removeFromCart(product.getId());
            reloadCart();
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
        });

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(cartAdapter);

        updateTotal();

        btnThanhToan.setOnClickListener(v -> {
            String userEmail = getIntent().getStringExtra("email");

            for (Product product : cartItems) {
                dbHelper.saveOrder(product, userEmail);
            }

            dbHelper.clearCart();

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
            }, 300);
        });
    }

    private void reloadCart() {
        cartItems.clear();
        cartItems.addAll(dbHelper.getCartItems());
        cartAdapter.notifyDataSetChanged();
        updateTotal();
    }

    private void updateTotal() {
        int total = 0;
        for (Product p : cartItems) {
            total += p.getPrice();
        }

        String formattedTotal = NumberFormat.getInstance(new Locale("vi", "VN")).format(total) + " đ";
        txtTotal.setText("Tổng tiền: " + formattedTotal);
    }
}
