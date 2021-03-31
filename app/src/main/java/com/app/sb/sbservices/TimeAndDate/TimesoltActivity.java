package com.app.sb.sbservices.TimeAndDate;


import android.content.Intent;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.Cart.Increment_Listener;
import com.app.sb.sbservices.Cart.CartActivity;
import com.app.sb.sbservices.Packages.PackageModel;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;
import com.app.sb.sbservices.Utils.Singleton;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TimesoltActivity extends AppCompatActivity implements DatePickerListener, TImeSlotClickListener, Increment_Listener {
    public static String TAG = "TimeSlotActivity";
    RecyclerView recyclerView;

    RelativeLayout relativeLayout;
    RelativeLayout parentLayout;
    private String slotTime;
    private String selectedDate;
    protected HorizontalPicker picker;
    private PrefManager prefManager;
    private PackageModel packagesListModel;
    TextView addtocart;
    private ApiCallingFlow apiCallingFlow;
    private List<TimeSlotModel> timeModels = new ArrayList<>();
    private TimeSlotAdapter adapter;
    Toolbar toolbar;
    String slotStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesolt);
        recyclerView = findViewById(R.id.time_slot_List);
        picker = findViewById(R.id.datePicker);
        parentLayout = findViewById(R.id.relative_layout);
        addtocart = findViewById(R.id.add_cart_btn);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.time_date));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickADDCart();

            }
        });

        prefManager = new PrefManager(this);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            packagesListModel = (PackageModel) intent.getSerializable("selected_package");
        }
        picker.setListener(this)
                .setDays(30)
                .setOffset(0)
                .setDateSelectedColor(getResources().getColor(R.color.colorPrimary))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayButtonTextColor(getResources().getColor(R.color.white))
                .setTodayDateTextColor(getResources().getColor(R.color.white))
                .setTodayDateBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setUnselectedDayTextColor(getResources().getColor(R.color.white))
                .setDayOfWeekTextColor(getResources().getColor(R.color.colorPrimary))
                .showTodayButton(false)
                .init();
        picker.setBackgroundColor(getResources().getColor(R.color.white));
        picker.setDate(new DateTime());
        GridLayoutManager layoutManager = new GridLayoutManager(TimesoltActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        requestTimeSlotServiceApi();


    }

    private void requestTimeSlotServiceApi() {
        apiCallingFlow = new ApiCallingFlow(this, parentLayout, false) {
            @Override
            public void callCurrentApiHere() {
                requestTimeSlotServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            //getTimeSlotData();
            //  getAvailableSlot();
        }

    }


    @Override
    public void onDateSelected(DateTime dateSelected) {
        try {
            // You can use any format to compare by spliting the dateTime
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            String str1 = dateSelected.toString();
            Date dddd = formatter.parse(str1);

            timeModels.clear();
            selectedDate = outputFormat.format(dddd);
            prefManager.storeValue(AppConstants.S_date, selectedDate);
            prefManager.setsDate(selectedDate);
            getAvailableSlot();

            String str2 = new DateTime().toString();
            Date currentDate = outputFormat.parse(str2);

//            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
//            Date date = inputFormat.parse("2018-04-10T04:00:00.000Z");
//            String selectedDate = outputFormat.format(date);

            //  Toast.makeText(this, "ggsg"+selectedDate, Toast.LENGTH_SHORT).show();
            Log.e("dhggdg", "hddhgghd" + formatter);
            Log.e("dhggdg", "hddhgghd" + currentDate);
            Log.e("dhggdg", "hddhgghd" + dddd);

            if (selectedDate.compareTo(selectedDate) < 0) {
                picker.setDate(new DateTime());
                System.out.println("current date is Greater than my selected date");
            } else {
                System.out.println("selected date is Greater than my current date");
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        Log.i("HorizontalPicker", "Fecha seleccionada=" + dateSelected.toString());
        //   selectedDate = dateSelected.toString();

    }

    private void getAvailableSlot() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BLOCK_TIME, response -> {
            apiCallingFlow.onSuccessResponse();
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String status = jsonObject.getString("status");
                    String time_slot_name = jsonObject.getString("time_slot_name");
                    TimeSlotModel model = new TimeSlotModel();
                    model.setId(jsonObject.getString("id"));

                    model.setTimeSlotName(time_slot_name);
                    model.setStatus(status);
                    model.setSelected(false);
                    Log.v("DATE", "" + selectedDate);

                    if (status.equalsIgnoreCase("active")) {
//                                Toast.makeText(TimeSlotActivity.this, "DATE "+selectedDate, Toast.LENGTH_SHORT).show();

                    } else {

                    }

                    timeModels.add(model);

                    //timeStatusObject = jsonArray.getJSONObject(i);

                    Log.e(TAG, "My Status" + status);
                    if (timeModels.size() > 0) {
                        adapter = new TimeSlotAdapter(timeModels,
                                TimesoltActivity.this);
                        recyclerView.setAdapter(adapter);
                    }

                }

                Log.e(TAG, response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> apiCallingFlow.onErrorResponse()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("s_date", selectedDate);

                Log.e("dgdgd", "shshhs" + selectedDate);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClickTimeSlot(int position) {

        for (int i = 0; i < timeModels.size(); i++) {
            if (i == position) {
                timeModels.get(i).setSelected(true);
                slotTime = timeModels.get(i).getId();

                slotStatus = timeModels.get(i).getStatus();
            } else {
                timeModels.get(i).setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();


    }

    void onClickADDCart() {

        if (slotTime != null) {

            if (!slotStatus.equals("blocked")) {
                requestServiceApi();
            } else {
                Toast.makeText(TimesoltActivity.this, "This slot is alredy booked " + "\n" +
                        "Please select other slot", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(TimesoltActivity.this, "Please Select Time slot", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestServiceApi() {
        apiCallingFlow = new ApiCallingFlow(this, parentLayout, true) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            packageAddToCart(AppConstants.INSERT_CART);
        }
    }

    public void packageAddToCart(final String s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, s, s1 -> {
            apiCallingFlow.onSuccessResponse();
            Log.e(TAG, "Response " + s1);
            onSuccessAddtoCart();
        },
                volleyError -> apiCallingFlow.onErrorResponse()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("user_id", prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                params.put("service_id", packagesListModel.getService_id());
                // params.put("sub_service_id", packagesListModel.get());
                params.put("package_id", packagesListModel.getPackageId());
                //   params.put("sub_package_id", packagesListModel.get);
                params.put("package_name", packagesListModel.getPackage_name());
                params.put("timeslot", slotTime);
                params.put("servicedate", selectedDate);
                params.put("price", packagesListModel.getPackage_price());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(TimesoltActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void onSuccessAddtoCart() {
        Intent intent = new Intent(TimesoltActivity.this, CartActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void Increment() {
        packageAddToCart(AppConstants.INSERT_CART);
    }

    TextView textCartItemCount;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_credits);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                break;


            case R.id.action_credits: {
                Intent intent = new Intent(TimesoltActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }



        }
        return super.onOptionsItemSelected(item);

    }
    private void setupBadge() {

        if (textCartItemCount!= null) {
            if (Singleton.getInstance().cartItemsCount == 0) {
                if (textCartItemCount.getVisibility()!= View.GONE) {
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
