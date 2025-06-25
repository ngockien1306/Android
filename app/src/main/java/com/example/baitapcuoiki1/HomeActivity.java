package com.example.baitapcuoiki1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.Adapter.CategoryAdapter;
import com.example.baitapcuoiki1.Adapter.ProductAdapter;
import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.model.Category;
import com.example.baitapcuoiki1.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerCategories, recyclerProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private List<Product> allProducts;
    private List<Product> displayedProducts;

    private DatabaseHelper dbHelper;
    private ImageView btnCart; // Khai báo đúng chỗ
    Button btndamua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerCategories = findViewById(R.id.recycler_categories);
        recyclerProducts = findViewById(R.id.recycler_products);
        btnCart = findViewById(R.id.btnCart);
        btndamua = findViewById(R.id.btndamua);
        dbHelper = new DatabaseHelper(this);

        List<Category> categoryList = dbHelper.getAllCategories();


        allProducts = dbHelper.getAllProducts();

        // Khởi tạo danh sách sản phẩm hiển thị mặc định (tất cả)
        displayedProducts = new ArrayList<>(allProducts);


        productAdapter = new ProductAdapter(this,displayedProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerProducts.setAdapter(productAdapter);

        // Cài đặt RecyclerView danh mục + lọc sản phẩm theo danh mục
        categoryAdapter = new CategoryAdapter(categoryList, categoryId -> {
            List<Product> filteredList = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getCategoryId() == categoryId) {
                    filteredList.add(p);
                }
            }
            productAdapter.updateList(filteredList);
        });

        recyclerCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategories.setAdapter(categoryAdapter);


        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            intent.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(intent);
        });
        btndamua.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, OrderActivity.class);
            startActivity(intent);
        });
    }
}
