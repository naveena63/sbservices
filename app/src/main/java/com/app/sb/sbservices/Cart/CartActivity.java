package com.app.sb.sbservices.Cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.BottomNavActivity;
import com.app.sb.sbservices.Location.LocationActivity;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;
import com.app.sb.sbservices.Utils.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    public static String TAG = "CartActivity";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_CART_ID = "cart_id";
    String totalPrice,rewardpoints;

    Integer couponAmount;
    RecyclerView recyclerView;
    CartAdapter adapter;
    ViewGroup viewGroup;
    List<CartModel> timeModels;
    private ApiCallingFlow apiCallingFlow;
    private PrefManager prefManager;
    TextView noCartProductsTextView, rewardPoints;
    Button continuePaymentTextView, addService, couponCode;
    EditText editTextCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.cart_page));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        couponCode = findViewById(R.id.coupon_code);
        noCartProductsTextView = findViewById(R.id.no_cart_available);
        continuePaymentTextView = findViewById(R.id.continue_payment_text);
        addService = findViewById(R.id.add_service_text);
        timeModels = new ArrayList<>();
        rewardPoints = (TextView) findViewById(R.id.reward_points);
        prefManager = new PrefManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        CheckBox checkBox = findViewById(R.id.check_box);

        continuePaymentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContinue();
            }
        });
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickAddService();
            }
        });
        couponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Enter Coupon");
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                View viewInflated = LayoutInflater.from(CartActivity.this).inflate(R.layout.dialog, (ViewGroup) viewGroup, false);
                Button couponButn = (Button) viewInflated.findViewById(R.id.btnDilougue);
                editTextCoupon = viewInflated.findViewById(R.id.enter_coupon);
                couponButn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String couponCode = editTextCoupon.getText().toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.COUPON_CODE, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.i("copuonResponse", "couponResponse" + response);

                                    String status = jsonObject.getString("status");

                                    if (status.equals("1")) {
                                        String couponTotal = jsonObject.getString("total");
                                        String couponCode = jsonObject.getString("coupon_code");

                                        prefManager.storeValue(AppConstants.CODE_COUPON, couponCode);
                                        prefManager.setCouponCode("" + couponCode);
                                        Log.e("coupon_code", "couponcode" + couponCode);

                                        couponAmount = jsonObject.getInt("coupon_amount");
                                        prefManager.storeValue(AppConstants.COUPON_AMOUNT, couponAmount);
                                        prefManager.setCouponAmount("" + couponAmount);

                                        editTextCoupon.setText(couponCode);
                                        continuePaymentTextView.setText(couponTotal);
                                        Toast.makeText(CartActivity.this, "coupon Applied Succesfully", Toast.LENGTH_SHORT).show();
                                    } else if (status.equals("fail")) {
                                        Toast.makeText(CartActivity.this, "coupon not valid or coupon already used", Toast.LENGTH_SHORT).show();
                                    }
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
                                map.put("user_id", prefManager.getUserId());
                                map.put("coupon_code", couponCode);
                                map.put("total_amount", prefManager.getTotalPrice());
                                Log.e("yoal", "dhnswk" + prefManager.getTotalPrice());
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                        requestQueue.add(stringRequest);

                    }

                });
                builder.setView(viewInflated);
                builder.show();
            }

        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (buttonView.isChecked()) {
                        deductRewardPoints();

                    } else {
                        revainRewardpoints();

                    }
                }
            }
        });

        requestServiceApi();
        getRewardPoints();

    }



    private void onClickContinue() {
        if (totalPrice != null) {
            Intent intent = new Intent(CartActivity.this, LocationActivity.class);
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            prefManager.setPrice("" + totalPrice);
        }

    }

    private void requestServiceApi() {
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        apiCallingFlow = new ApiCallingFlow(this, parentLayout, true) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            timeModels(AppConstants.CART_ITEMS);
        }
    }

    protected void onCLickAddService() {
        Intent intent = new Intent(CartActivity.this, BottomNavActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    private void timeModels(final String s) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        apiCallingFlow.onSuccessResponse();

                        String status;
                        JSONObject jsonMainObject;
                        try {
                            jsonMainObject = new JSONObject(s);
                            status = jsonMainObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray listArray = jsonMainObject.getJSONArray("cartitems");
                                JSONObject memberOject;

                                if (timeModels != null) {
                                    timeModels.clear();
                                }

                                for (int i = 0; i < listArray.length(); i++) {
                                    memberOject = listArray.getJSONObject(i);
                                    CartModel model = new CartModel();
                                    model.setId(memberOject.getString("id"));
                                    model.setServiceId(memberOject.getString("service_id"));
                                    model.setPackageId(memberOject.getString("package_id"));
                                    model.setPackageName(memberOject.getString("package_name"));
                                    model.setPrice(memberOject.getString("price"));
                                    model.setQuantity(memberOject.getString("qty"));
                                    model.setTimeSlot(memberOject.getString("time_slot"));
                                    model.setUserId(memberOject.getString("user_id"));
                                    model.setDate(memberOject.getString("date"));
                                    model.setServiceDate(memberOject.getString("service_date"));
                                    timeModels.add(model);

                                }
                                if (timeModels.size() > 0) {
                                    adapter = new CartAdapter(timeModels);
                                    recyclerView.setAdapter(adapter);
                                    noCartProductsTextView.setVisibility(View.GONE);
                                    adapter.setItemClickListener(new CartAdapter.OnItemClickListener() {
                                        @Override
                                        public void OnClick(View view, int position, String userID, String Cart_id) {
                                            deleteItems(userID, Cart_id);

                                        }
                                    });
                                }
                            } else {
                                noCartProductsTextView.setText(jsonMainObject.getString("msg"));
                                noCartProductsTextView.setVisibility(View.VISIBLE);
                            }
                            getCartTotalAmount();
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        apiCallingFlow.onErrorResponse();
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(CartActivity.this, "Bad internet connection please try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("user_id", prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                Log.e("user_id", "user_id===>" + prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private void getCartTotalAmount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.CART_TOTAL_AMOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        apiCallingFlow.onSuccessResponse();
                        Log.e(TAG, "Response " + s);
                        String status;
                        JSONObject jsonMainObject;
                        try {

                            jsonMainObject = new JSONObject(s);
                            status = jsonMainObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                totalPrice = jsonMainObject.getString("cart_total");
                                continuePaymentTextView.append(String.valueOf("\n" + getResources().getString(R.string.price_symbl) + jsonMainObject.getString("cart_total")));
                                prefManager.storeValue(AppConstants.TOTAL_PRICE, totalPrice);
                                prefManager.setTotalPrice("" + totalPrice);
                                Log.e("prefTotal", "totalPrice" + prefManager.getTotalPrice());
                            }

                        } catch (Exception e) {
                            System.out.println(TAG + " Exception=======>" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        apiCallingFlow.onErrorResponse();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                params.put("user_id", prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    private void deleteItems(String userID, String cart_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.DELETE_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        apiCallingFlow.onSuccessResponse();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            String msg = object.getString("msg");
                            switch (status) {
                                case "success":
                                    Singleton.getInstance().cartItemsCount = (timeModels.size() - 1);
                                    Toast.makeText(CartActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    // getCartTotalAmount();
                                    break;
                                case "0":
                                    Toast.makeText(CartActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                    break;
                                case "error":
                                    Toast.makeText(CartActivity.this, "Error Occured Please Try Again", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("DELETE CART", "_--------------DELETE Response----------------" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiCallingFlow.onErrorResponse();
                        error.printStackTrace();
                        Toast.makeText(CartActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERID, userID);
                map.put(KEY_CART_ID, cart_id);
                Log.i("cart_id", "cartid" + cart_id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getRewardPoints() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_REWARDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("rewarsResponse", "response" + response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        rewardpoints = jsonObject.getString("reward_points");
                        rewardPoints.setText(rewardpoints);
                        prefManager.storeValue(AppConstants.REWARD_POINTS, rewardpoints);
                        prefManager.setRewardsPoints("" + rewardpoints);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
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
    }


    private void deductRewardPoints() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ADD_REWARD_POINTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("deductreward", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                     String cutRewardsPoints=jsonObject.getString("reward_points");

                     prefManager.storeValue(AppConstants.CutRewardPoints,cutRewardsPoints);
                     prefManager.setCuttingRewardpoints(cutRewardsPoints);

                    String grandTotal = jsonObject.getString("grand_total");
                    continuePaymentTextView.setText(grandTotal);

                    prefManager.storeValue(AppConstants.ANOTHER_PARICE, grandTotal);
                    prefManager.setDeductPrice(grandTotal);

                    String presentRwardPounts = jsonObject.getString("present_reward_points");
                    rewardPoints.setText(presentRwardPounts);

                    prefManager.storeValue(AppConstants.PRESENT_REWARD_POINTS, presentRwardPounts);
                    prefManager.setPresentRewardPoints(presentRwardPounts);
                    Log.i("pr", "pr" + prefManager.getPresentRewardPoints());


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
                Map params = new HashMap<String, String>();
                params.put("user_id", prefManager.getUserId());
                params.put("total", totalPrice);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void revainRewardpoints() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.REMOVE_REWARD_POINTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("revain", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String grandTotal = jsonObject.getString("grand_total");
                    continuePaymentTextView.setText(grandTotal);
                    rewardPoints.setText(prefManager.getRewardsPoints());
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
                Map params = new HashMap<String, String>();
                params.put("user_id", prefManager.getUserId());
                params.put("total", prefManager.getDeductPrice());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void refreshCartAmount(Context context) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        finish();
    }



    TextView textCartItemCount;
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_credits);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_credits: {
                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }


        }
        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() {
        if (textCartItemCount != null) {
            if (Singleton.getInstance().cartItemsCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(Singleton.getInstance().cartItemsCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}