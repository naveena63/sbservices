package com.app.sb.sbservices.Cart;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.PrefManager;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;

    private List<CartModel> timeModelList;
    private OnItemClickListener itemClickListener;
    PrefManager prefManager;

    CartAdapter(List<CartModel> timeModels) {
        this.timeModelList = timeModels;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);
        prefManager = new PrefManager(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.subServiceNameTv.setText(String.valueOf("Package : " + timeModelList.get(position).getPackageName()));
        holder.priceTextView.setText(String.valueOf(context.getResources().getString(R.string.price_symbl) + timeModelList.get(position).getPrice()));
//         holder.quantityTextView.setText(String.valueOf("Quantity : " + timeModelList.get(position).getQuantity()));


        Log.e("cart_price", "c_price===>" + timeModelList.get(position).getPrice());
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = timeModelList.get(position).getUserId();
                String cart_id = timeModelList.get(position).getId();
                itemClickListener.OnClick(view, position, user_id, cart_id);
                timeModelList.remove(position);

                ((CartActivity)context).refreshCartAmount(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_delete;
        TextView subServiceNameTv;
        TextView priceTextView;
        TextView quantityTextView;

        ViewHolder(View itemView) {
            super(itemView);
            subServiceNameTv = itemView.findViewById(R.id.pack_name_tv);

            priceTextView = itemView.findViewById(R.id.price_tv);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    public interface OnItemClickListener {

        public void OnClick(View view, int position, String userID, String Cart_id);

    }
}

