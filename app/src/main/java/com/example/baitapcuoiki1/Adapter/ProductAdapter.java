package com.example.baitapcuoiki1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.Database.DatabaseHelper;
import com.example.baitapcuoiki1.R;
import com.example.baitapcuoiki1.XemchitietActivity;
import com.example.baitapcuoiki1.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    // Giao diện callback khi nhấn giữ để sửa/xóa
    public interface OnItemLongClickListener {
        void onItemLongClick(Product product);
    }

    private OnItemLongClickListener longClickListener;

    // Hàm gán listener từ ngoài (AdminProductActivity)
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    // Constructor truyền context và danh sách sản phẩm
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    // Cập nhật lại danh sách sản phẩm
    public void updateList(List<Product> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.imgProduct.setImageResource(product.getImage());

        // Format giá theo VNĐ
        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        String priceFormatted = format.format(product.getPrice()) + " đ";
        holder.tvProductPrice.setText("Giá: " + priceFormatted);

        // Mở trang chi tiết sản phẩm (nếu là người dùng)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, XemchitietActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

        // Thêm sản phẩm vào giỏ
        holder.btnAddToCart.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            dbHelper.addToCart(product);
            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Bắt sự kiện nhấn giữ nếu có listener
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(product);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice;
        Button btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
