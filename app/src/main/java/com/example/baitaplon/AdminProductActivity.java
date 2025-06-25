package com.example.baitaplon;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.Adapter.ProductAdminAdapter;
import com.example.baitaplon.Database.DatabaseHelper;
import com.example.baitaplon.model.Product;

import java.util.List;

public class AdminProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdminAdapter adapter;
    List<Product> productList;
    DatabaseHelper db;
    Button btnAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        recyclerView = findViewById(R.id.recyclerAdminProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnAddProduct = findViewById(R.id.btnAddProduct);

        db = new DatabaseHelper(this);
        productList = db.getAllProducts();

        adapter = new ProductAdminAdapter(productList, new ProductAdminAdapter.OnProductActionListener() {
            @Override
            public void onEdit(Product product) {
                showEditDialog(product);
            }

            @Override
            public void onDelete(Product product) {
                db.deleteProduct(product.getId());
                Toast.makeText(AdminProductActivity.this, "Đã xoá sản phẩm", Toast.LENGTH_SHORT).show();
                reloadList();
            }
        });

        recyclerView.setAdapter(adapter);

        btnAddProduct.setOnClickListener(v -> showAddProductDialog());
    }

    private void reloadList() {
        productList.clear();
        productList.addAll(db.getAllProducts());
        adapter.notifyDataSetChanged();
    }

    private void showEditDialog(Product product) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null);
        EditText edtName = view.findViewById(R.id.edtProductName);
        EditText edtPrice = view.findViewById(R.id.edtProductPrice);

        edtName.setText(product.getName());
        edtPrice.setText(String.valueOf(product.getPrice()));

        new AlertDialog.Builder(this)
                .setTitle("Sửa sản phẩm")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = edtName.getText().toString();
                    int price = Integer.parseInt(edtPrice.getText().toString());
                    db.updateProduct(product.getId(), name, price);
                    Toast.makeText(this, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                    reloadList();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


}
