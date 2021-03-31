package com.app.sb.sbservices.Orders;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CompletedOrderFragment extends Fragment {
    List<CompleteOrderModel> completeOrderModelList;
    CompleteOrderModel completeOrderModel;
    RecyclerView recyclerView;
    CompletedOrderAdapter completeOrderAdapter;
    TextView no_packages_available;
    public CompletedOrderFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_order, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false));

        no_packages_available = view.findViewById(R.id.no_packages_available);

        loadData();

        return view;
    }
    private void loadData(){


        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        StringRequest sr = new StringRequest(Request.Method.POST, AppConstants.COMPLETED_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("Responce",""+response);
                completeOrderModelList=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String myResponce = jsonObject.getString("status");


                    if (myResponce.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("completed_orders");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String orederId = jsonObject1.getString("order_id");
                            String price = jsonObject1.getString("price");
                            String service_date = jsonObject1.getString("service_date");

                            completeOrderModel = new CompleteOrderModel();
                            completeOrderModel.setOrderId(orederId);
                            completeOrderModel.setPrice(price);
                            completeOrderModel.setServiceDate(service_date);
                            completeOrderModelList.add(completeOrderModel);
                        }
                        if (completeOrderModelList.size() > 0) {
                            completeOrderAdapter = new CompletedOrderAdapter(getActivity(), completeOrderModelList);
                            completeOrderAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(completeOrderAdapter);
                            no_packages_available.setVisibility(View.GONE);
                        }
                    } else {
                        no_packages_available.setText("no data found");
                        no_packages_available.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id",new PrefManager(getActivity()).getUserId());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(sr);


    }


}
