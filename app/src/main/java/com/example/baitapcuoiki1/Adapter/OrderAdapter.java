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
import com.example.baitapcuoiki1.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Adapter hiển thị danh sách đơn hàng đã đặt
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnDeleteClickListener listener; // Callback xóa

    // Interface xử lý sự kiện xóa
    public interface OnDeleteClickListener {
        void onDeleteClick(Order order);
    }

    // Constructor truyền dữ liệu và listener
    public OrderAdapter(List<Order> orders, OnDeleteClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set tên sản phẩm
        holder.tvName.setText(order.getName());

        // Định dạng giá tiền
        String formattedPrice = NumberFormat.getInstance(new Locale("vi", "VN"))
                .format(order.getPrice()) + " đ";
        holder.tvPrice.setText("Giá: " + formattedPrice);

        // Định dạng thời gian đặt hàng
        try {
            long timeMillis = Long.parseLong(order.getOrderTime());
            String formattedTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(timeMillis));
            holder.tvTime.setText("Ngày đặt: " + formattedTime);
        } catch (Exception e) {
            holder.tvTime.setText("Ngày đặt: Không rõ");
        }


        holder.imgProduct.setImageResource(order.getImage());


        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // ViewHolder chứa các thành phần UI
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvTime;
        ImageView imgProduct;
        Button btnDelete;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvOrderName);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvTime = itemView.findViewById(R.id.tvOrderTime);
            imgProduct = itemView.findViewById(R.id.imgOrderProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteOrder);
        }
    }
}
