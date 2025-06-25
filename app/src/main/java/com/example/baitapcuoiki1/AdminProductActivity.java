package com.example.baitapcuoiki1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.Adapter.ProductAdapter;
import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AdminProductActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;
    Button btnAddProduct;
    List<Product> productList;
    ProductAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        // Ánh xạ view
        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        // Khởi tạo Database và danh sách sản phẩm
        db = new DatabaseHelper(this);
        productList = db.getAllProducts();

        // Cấu hình RecyclerView
        adapter = new ProductAdapter(this, productList);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerProducts.setAdapter(adapter);

        // Xử lý nút Thêm sản phẩm
        btnAddProduct.setOnClickListener(v -> showAddProductDialog());

        // Gán sự kiện nhấn giữ để sửa hoặc xóa sản phẩm
        adapter.setOnItemLongClickListener(product -> showOptionsDialog(product));
    }

    // Hiển thị hộp thoại thêm sản phẩm
    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm sản phẩm mới");

        // Tạo layout nhập liệu
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtImage = view.findViewById(R.id.edtImage);
        EditText edtCategory = view.findViewById(R.id.edtCategory);
        builder.setView(view);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            // Lấy dữ liệu nhập
            String name = edtName.getText().toString().trim();
            int price = Integer.parseInt(edtPrice.getText().toString().trim());
            int image = Integer.parseInt(edtImage.getText().toString().trim());
            int categoryId = Integer.parseInt(edtCategory.getText().toString().trim());

            // Tạo đối tượng sản phẩm
            Product p = new Product(0, name, image, categoryId, price);
            db.insertProduct(p);  // Thêm vào CSDL

            // Cập nhật giao diện
            productList.clear();
            productList.addAll(db.getAllProducts());
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // Hiển thị tùy chọn khi nhấn giữ vào sản phẩm
    private void showOptionsDialog(Product product) {
        String[] options = {"Sửa", "Xóa"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn thao tác")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditProductDialog(product); // Sửa
                    } else {
                        db.deleteProduct(product.getId()); // Xóa
                        productList.remove(product);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    // Hộp thoại sửa thông tin sản phẩm
    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa sản phẩm");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtImage = view.findViewById(R.id.edtImage);
        EditText edtCategory = view.findViewById(R.id.edtCategory);

        // Đổ dữ liệu cũ vào
        edtName.setText(product.getName());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtImage.setText(String.valueOf(product.getImage()));
        edtCategory.setText(String.valueOf(product.getCategoryId()));

        builder.setView(view);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            product.setName(edtName.getText().toString().trim());
            product.setPrice(Integer.parseInt(edtPrice.getText().toString().trim()));
            product.setImage(Integer.parseInt(edtImage.getText().toString().trim()));
            product.setCategoryId(Integer.parseInt(edtCategory.getText().toString().trim()));

            db.updateProduct(product); // Cập nhật CSDL

            productList.clear();
            productList.addAll(db.getAllProducts());
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
