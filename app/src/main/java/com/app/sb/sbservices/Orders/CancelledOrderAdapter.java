package com.app.sb.sbservices.Orders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sb.sbservices.R;

import java.util.List;

public class CancelledOrderAdapter   extends RecyclerView.Adapter<CancelledOrderAdapter.VH> {
    Context context;
    List<PendingOrderModel> pendingOrderModelList;

    public CancelledOrderAdapter(Context context, List<PendingOrderModel> pendingOrderModelList)
    {
        this.context = context;
        this.pendingOrderModelList = pendingOrderModelList;
    }

    @NonNull
    @Override
    public CancelledOrderAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.cancel_order_custom_layout,
                        viewGroup,
                        false);


        return new CancelledOrderAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelledOrderAdapter.VH holder, int position) {
        Log.v("DDDD","");
        holder.textOrderId.setText("Order id :   "+pendingOrderModelList.get(position).getOrderId());
        holder.textPendindDate.setText("pending date :   "+pendingOrderModelList.get(position).getServiceDate());
        holder.textOrderAmount.setText("Order Amount :   "+pendingOrderModelList.get(position).getPrice());


    }
    @Override
    public int getItemCount() {
        return pendingOrderModelList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView textPendindDate,textOrderId,textOrderAmount;

        public VH( View itemView) {
            super(itemView);
            textPendindDate=itemView.findViewById(R.id.textview_pendingDate);
            textOrderId=itemView.findViewById(R.id.textView_orderId);
            textOrderAmount=itemView.findViewById(R.id.textView_orderAmount);


        }
    }

}

