package com.app.sb.sbservices.Orders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingOrdersAdapter extends RecyclerView.Adapter<BookingOrdersAdapter.VH> {
    Context context;
    List<BookingOrdersModel> bookingOrdersModelList;
    PrefManager prefManager;

    public BookingOrdersAdapter(Context context, List<BookingOrdersModel> bookingOrdersModelList) {
        this.context = context;
        this.bookingOrdersModelList = bookingOrdersModelList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.booking_orders_layout,
                        viewGroup,
                        false);

        prefManager = new PrefManager(context);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Log.v("DDDD", "");
        holder.textOrderId.setText("Order id :   " + bookingOrdersModelList.get(position).getOrderId());
        holder.textPendindDate.setText("Booking date :   " + bookingOrdersModelList.get(position).getServiceDate());
        holder.textOrderAmount.setText("Order Amount :   " + bookingOrdersModelList.get(position).getPrice());

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewOrderActivity.class);
                context.startActivity(intent);
                prefManager.setOrderId(bookingOrdersModelList.get(position).getOrderId());
            }
        });
        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure  want to cancel?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.CANCEL_ORDER, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                Log.e("cancelOrder", "cancelOrder" + response);
                                                String status = jsonObject.getString("status");
                                                //String msg = jsonObject.getString("msg");
                                                // prefManager.setOrderId(bookingOrdersModelList.get(position).getOrderId());

                                                holder.cancelOrder.setText("cancelled");
                                                holder.cancelOrder.setBackgroundColor(Color.parseColor("#dd0d7b"));
                                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> map = new HashMap<>();
                                            map.put("order_id", prefManager.getOrderId());
                                            Log.e("order_id", "orfde");
                                            return map;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    requestQueue.add(stringRequest);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                   }
        });
    }

    @Override
    public int getItemCount() {
        return bookingOrdersModelList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView textPendindDate, textOrderId, textOrderAmount, textServiceName;
        Button viewButton, cancelOrder;

        public VH(View itemView) {
            super(itemView);
            textPendindDate = itemView.findViewById(R.id.textbookingdate);
            textOrderId = itemView.findViewById(R.id.textView_orderId);
            textOrderAmount = itemView.findViewById(R.id.textView_orderAmount);
            viewButton = itemView.findViewById(R.id.viewButton);
            cancelOrder = itemView.findViewById(R.id.cancelOrder);

        }
    }

}
