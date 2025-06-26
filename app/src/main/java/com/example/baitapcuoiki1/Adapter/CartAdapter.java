package com.example.baitapcuoiki1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapcuoiki1.R;
import com.example.baitapcuoiki1.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnRemoveClickListener {
        void onRemove(Product product);
    }

    private List<Product> cartItems;
    private OnRemoveClickListener removeClickListener;

    public CartAdapter(List<Product> cartItems, OnRemoveClickListener removeClickListener) {
        this.cartItems = cartItems;
        this.removeClickListener = removeClickListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.tvName.setText(product.getName());

        // Format giá tiền
        String formattedPrice = NumberFormat.getInstance(new Locale("vi", "VN"))
                .format(product.getPrice()) + " đ";
        holder.tvPrice.setText("Giá: " + formattedPrice);

        holder.imgProduct.setImageResource(product.getImage());

        holder.btnRemove.setOnClickListener(v -> {
            if (removeClickListener != null) {
                removeClickListener.onRemove(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct;
        Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
