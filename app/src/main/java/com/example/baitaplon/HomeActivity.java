package com.example.baitaplon;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.Adapter.CategoryAdapter;
import com.example.baitaplon.Adapter.ProductAdapter;
import com.example.baitaplon.Database.DatabaseHelper;
import com.example.baitaplon.model.Category;
import com.example.baitaplon.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerCategories, recyclerProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private List<Product> allProducts;
    private List<Product> displayedProducts;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerCategories = findViewById(R.id.recycler_categories);
        recyclerProducts = findViewById(R.id.recycler_products);

        dbHelper = new DatabaseHelper(this);

        // Load danh mục và sản phẩm
        List<Category> categoryList = dbHelper.getAllCategories();
        allProducts = dbHelper.getAllProducts();
        displayedProducts = new ArrayList<>(allProducts); // mặc định hiển thị tất cả

        // Thiết lập adapter cho danh sách sản phẩm
        productAdapter = new ProductAdapter(displayedProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerProducts.setAdapter(productAdapter);

        // Thiết lập adapter cho danh mục
        categoryAdapter = new CategoryAdapter(categoryList, categoryId -> {
            // Lọc sản phẩm theo danh mục
            List<Product> filteredList = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getCategoryId() == categoryId) {
                    filteredList.add(p);
                }
            }
            productAdapter.updateList(filteredList);
        });

        recyclerCategories.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerCategories.setAdapter(categoryAdapter);
    }
}
