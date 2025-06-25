package com.example.baitaplon.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.R;
import com.example.baitaplon.model.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onEdit(Order order);
        void onDelete(Order order);
    }

    public OrderAdapter(List<Order> orderList, OnOrderActionListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtName.setText("Sản phẩm: " + order.getProductName());
        holder.txtQuantity.setText("Số lượng: " + order.getQuantity());

        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = format.format(order.getPrice()) + " đ";

        holder.txtPrice.setText("Đơn giá: " + formattedPrice);
        holder.txtDate.setText("Ngày đặt: " + order.getOrderDate());

        // Sự kiện sửa
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(order);
        });

        // Sự kiện xoá
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(order);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQuantity, txtPrice, txtDate;
        Button btnEdit, btnDelete;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtOrderProductName);
            txtQuantity = itemView.findViewById(R.id.txtOrderQuantity);
            txtPrice = itemView.findViewById(R.id.txtOrderPrice);
            txtDate = itemView.findViewById(R.id.txtOrderDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
