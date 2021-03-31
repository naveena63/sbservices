package com.app.sb.sbservices.ReferalId;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class EarnCreditsActivity extends AppCompatActivity {
     String rewardPoints;
    private PrefManager prefManager;
    TextView textView;
    RecyclerView recyclerView;
    List<RewardsHistoryModel> rewardsHistoryModelList;
    RewardsHistoryModel rewardsHistoryModel;

    RewardsHistoryAdapter rewardsHistoryAdapter;
    TextView no_packages_available;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_credits);
         textView = (TextView)findViewById(R.id.reward_points);
         recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(EarnCreditsActivity.this));
        no_packages_available =findViewById(R.id.no_packages_available);
        prefManager=new PrefManager(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.earn));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_REWARDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("rewarsResponse", "response" + response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        rewardPoints = jsonObject.getString("reward_points");
                        textView.setText(rewardPoints);
                        prefManager.storeValue(AppConstants.REWARD_POINTS, rewardPoints);
                        prefManager.setTotalPrice("" + rewardPoints);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EarnCreditsActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("error", "response" + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = new HashMap<String, String>();
                params.put("user_id", prefManager.getUserId());
                Log.e("useridreward", "response" + prefManager.getUserId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        StringRequest sr = new StringRequest(Request.Method.POST,
                AppConstants.REWARDS_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("RewardpoiResponce",""+response);
                rewardsHistoryModelList=new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String myResponce = jsonObject.getString("status");

                    if (myResponce.equalsIgnoreCase("1"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("reward_history");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String orederId = jsonObject1.getString("order_id");
                            String date = jsonObject1.getString("date");
                            String usedrewardpoints = jsonObject1.getString("used_reward_points");

                            rewardsHistoryModel = new RewardsHistoryModel();
                            rewardsHistoryModel.setOrder_id(orederId);
                            rewardsHistoryModel.setDate(date);
                            rewardsHistoryModel.setUsed_reward_points(usedrewardpoints);
                            rewardsHistoryModelList.add(rewardsHistoryModel);
                        }
                        if (rewardsHistoryModelList.size() > 0) {
                            rewardsHistoryAdapter = new RewardsHistoryAdapter(EarnCreditsActivity.this, rewardsHistoryModelList);
                            rewardsHistoryAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(rewardsHistoryAdapter);
                            no_packages_available.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
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
                Toast.makeText(EarnCreditsActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                // params.put("user_id","CUST34051");
                params.put("user_id","CUST59327");
                params.put("token","c0304a62dd289bdc7364fb974c2091f6");
                return params;
            }

        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(sr);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
