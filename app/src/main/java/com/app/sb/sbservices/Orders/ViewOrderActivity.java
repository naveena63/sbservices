package com.app.sb.sbservices.Orders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.app.sb.sbservices.InvoiceActivity;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewOrderActivity extends AppCompatActivity {
    TextView orderStatus, time, address, number, doornum, serviceName, amount, date,landmrk,name,rewardpoints,couponAmount;
    Button call;
    PrefManager prefManager;
    Button fulldetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        orderStatus = findViewById(R.id.orderStatus);
        time = findViewById(R.id.time);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        address = findViewById(R.id.address);
        rewardpoints = findViewById(R.id.rewarspoints);
        couponAmount = findViewById(R.id.couponAmount);
        fulldetails=findViewById(R.id.full_details);
        doornum = findViewById(R.id.doorNumber);
        serviceName = findViewById(R.id.category_name);
        amount = findViewById(R.id.amount);
        prefManager = new PrefManager(this);
        date = findViewById(R.id.date);
        landmrk = findViewById(R.id.landmark);
        call=findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:9514222226"));
                startActivity(callIntent);
            }
        });
        fulldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOrderActivity.this, InvoiceActivity.class);
                startActivity(intent);
            }
        });
        loadViewData();
    }

    private void loadViewData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.VIEW_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("view", "view" + response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("View orders");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String ORDERSATUS = jsonObject1.getString("order_status");
                            String TIME = jsonObject1.getString("time_slot_name");
                            String PHONENUMBER = jsonObject1.getString("mobile");
                            String DOORNUM = jsonObject1.getString("door_no");
                            String SERVICENAME = jsonObject1.getString("category_name");
                            String AMOUNT = jsonObject1.getString("total_order_amount");
                            String namee = jsonObject1.getString("name");
                            String LANS = jsonObject1.getString("shipping_address");
                            Log.e("amount",""+AMOUNT);
                            String dates = jsonObject1.getString("ser_date");
                            String addresses = jsonObject1.getString("address");
                            String rewardpointsstrng = jsonObject1.getString("reward_points");
                            String couponAmountstrng = jsonObject1.getString("coupon_amount");

                            orderStatus.setText(ORDERSATUS);
                            number.setText(PHONENUMBER);
                            doornum.setText(DOORNUM);
                            serviceName.setText(SERVICENAME);
                            amount.setText("(" + AMOUNT + ")");
                            date.setText(dates+",");
                            landmrk.setText(LANS);
                            time.setText(TIME);
                            address.setText(addresses);
                            name.setText(namee);
                            rewardpoints.setText("Reward Points: "+rewardpointsstrng);
                            couponAmount.setText("CouponCode Amount: "+couponAmountstrng);
                        }
                    }

                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewOrderActivity.this, "something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("order_id", prefManager.getOrderId());

                Log.e("orde", "orde" + prefManager.getOrderId());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
