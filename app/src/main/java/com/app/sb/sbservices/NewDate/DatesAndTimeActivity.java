package com.app.sb.sbservices.NewDate;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.app.sb.sbservices.Cart.CartActivity;
import com.app.sb.sbservices.Cart.Increment_Listener;
import com.app.sb.sbservices.Packages.PackageModel;

import com.app.sb.sbservices.R;
import com.app.sb.sbservices.TimeAndDate.TImeSlotClickListener;
import com.app.sb.sbservices.TimeAndDate.TimeSlotAdapter;
import com.app.sb.sbservices.TimeAndDate.TimeSlotModel;

import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;
import com.app.sb.sbservices.Utils.Singleton;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatesAndTimeActivity extends AppCompatActivity implements  TImeSlotClickListener, Increment_Listener {
    RecyclerView recyclerView,recyclerView1;
    public static String TAG = "TimeSlotActivity";
    List<ModelClass> modelClassList;
    List<DateModel> dateModelList;
    AdapterClass adapterClass;
    TextView tvMonth;
    String characterMonth = "mmmmmm";
DateTime dateSelected;
    RelativeLayout parentLayout;
    private String slotTime, calendarDate;
    private String selectedDate;

    private PrefManager prefManager;
    private PackageModel packagesListModel;
    TextView addtocart;
    private ApiCallingFlow apiCallingFlow;
    private List<TimeSlotModel> timeModels = new ArrayList<>();
    private TimeSlotAdapter adapter;
    String slotStatus;
    String datetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_and_time);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        datetext= String.valueOf(formattedDate);
        recyclerView = findViewById(R.id.recyclerView);

        tvMonth = findViewById(R.id.textMonth);
        modelClassList = new ArrayList<>();
        dateModelList = new ArrayList<>();
        recyclerView1 = findViewById(R.id.time_slot_List);
        parentLayout = findViewById(R.id.relative_layout);
        addtocart = findViewById(R.id.add_cart_btn);
        prefManager = new PrefManager(this);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            packagesListModel = (PackageModel) intent.getSerializable("selected_package");
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.time_date));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(
                DatesAndTimeActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false));


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickADDCart();

            }
        });


        GridLayoutManager layoutManager = new GridLayoutManager(DatesAndTimeActivity.this, 2);
        recyclerView1.setLayoutManager(layoutManager);

        dateList();
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
             getAvailableSlot();
        }

    }


    public void dateList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sbservices.in/api/get-blocked-booking-dates",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonMainObject = new JSONObject(response);
                            Log.i("respons", "response" + response);
                            JSONArray jsonArray = jsonMainObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                calendarDate = jsonObject.getString("calendar_date");
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                String day = jsonObject.getString("day");

                                Log.e("dateeeeee", day);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                                Date date = inputFormat.parse(calendarDate);

                                String outputDateStr = outputFormat.format(date);
                                String upToNCharacters = outputDateStr.substring(0, Math.min(outputDateStr.length(), 2));

                                String dates = calendarDate;
                                String[] items1 = dates.split("-");
                                String year1 = items1[0];
                                String month1 = items1[1];
                                String date1 = items1[2];
                                Log.e("dateeeeee1", date1 + " " + month1 + " " + year1);

                                if (month1.equals("01")) {
                                    characterMonth = "jan";
                                    tvMonth.setText("jan" + year1);
                                } else if (month1.equals("02")) {
                                    characterMonth = "Feb";
                                    tvMonth.setText("Feb" + year1);
                                } else if (month1.equals("03")) {
                                    characterMonth = "Mar";
                                    tvMonth.setText("Mar" + year1);
                                } else if (month1.equals("04")) {
                                    characterMonth = "April";
                                    tvMonth.setText("April" + year1);
                                } else if (month1.equals("05")) {
                                    characterMonth = "May";
                                    tvMonth.setText("May" + year1);
                                } else if (month1.equals("06")) {
                                    characterMonth = "june";
                                    tvMonth.setText("June" + year1);
                                } else if (month1.equals("07")) {
                                    characterMonth = "July";
                                    tvMonth.setText("July" + year1);
                                } else if (month1.equals("08")) {
                                    characterMonth = "August";
                                    tvMonth.setText("Aug" + year1);
                                } else if (month1.equals("09")) {
                                    characterMonth = "Sept";
                                    tvMonth.setText("Sept" + year1);
                                } else if (month1.equals("10")) {
                                    characterMonth = "Oct";
                                    tvMonth.setText("Oct" + year1);
                                } else if (month1.equals("11")) {
                                    characterMonth = "Nov";
                                    tvMonth.setText("Nov" + year1);
                                } else if (month1.equals("12")) {
                                    characterMonth = "Dec";
                                    tvMonth.setText("Dec" + year1);
                                }
                                DateModel dateModel = new DateModel();

                                dateModel.setDate(date1);
                                dateModel.setMonth(characterMonth);
                                dateModel.setYear(year1);
                                dateModel.setNumnth(month1);
                                dateModelList.add(dateModel);


                                ModelClass modelClass = new ModelClass();
                                modelClass.setCalendar_date(upToNCharacters);
                                modelClass.setStatus(status);
                                modelClass.setMessage(message);
                                modelClass.setDay(day);

                                modelClassList.add(modelClass);
                            }
                            adapterClass = new AdapterClass(DatesAndTimeActivity.this, modelClassList, dateModelList);
                            recyclerView.setAdapter(adapterClass);
                            adapterClass.setOnDateClickListener(new AdapterClass.OnDateClickListener() {
                                @Override
                                public void onDateClickListener(DateModel dateModel, ModelClass modelClass) {
                                     datetext = dateModel.getDate() +"-"+ dateModel.getNumnth() +"-"+dateModel.getYear();
                                    String calander=modelClass.getCalendar_date();
                                    tvMonth.setText(dateModel.getMonth() + dateModel.getYear());
                                    Toast.makeText(DatesAndTimeActivity.this, "" + datetext, Toast.LENGTH_SHORT).show();
                                    Log.i("dateText", datetext);
                                    requestTimeSlotServiceApi();
                                    if (modelClass.getStatus().equals("blocked")) {
                                        Toast.makeText(DatesAndTimeActivity.this, "This date will be blocked because of" + modelClass.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } catch (Exception e) {
                            System.out.println("TAG" + " Exception=======>" + e.getMessage());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        try {
                            System.out.println("volley error...." + volleyError.getMessage().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DatesAndTimeActivity.this, "Bad internet connection please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                params.put("token", "c0304a62dd289bdc7364fb974c2091f6");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DatesAndTimeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getAvailableSlot() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BLOCK_TIME, response -> {
            apiCallingFlow.onSuccessResponse();
            try {
                JSONArray jsonArray = new JSONArray(response);
                       timeModels.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String status = jsonObject.getString("status");
                    String time_slot_name = jsonObject.getString("time_slot_name");
                    TimeSlotModel model = new TimeSlotModel();
                    model.setId(jsonObject.getString("id"));

                    model.setTimeSlotName(time_slot_name);
                    model.setStatus(status);
                    model.setSelected(false);
                    Log.v("DATE", "" + datetext);

                    if (status.equalsIgnoreCase("active")) {
//                                Toast.makeText(TimeSlotActivity.this, "DATE "+selectedDate, Toast.LENGTH_SHORT).show();

                    } else {

                    }

                    timeModels.add(model);

                    //timeStatusObject = jsonArray.getJSONObject(i);

                    Log.e(TAG, "My Status" + status);
                    if (timeModels.size() > 0) {
                        adapter = new TimeSlotAdapter(timeModels,
                                DatesAndTimeActivity.this);
                        recyclerView1.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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
                map.put("s_date", datetext);

                Log.e("SELECTEDdATE", "sELECTEDDATE" + datetext);
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
                Toast.makeText(DatesAndTimeActivity.this, "This slot is alredy booked " + "\n" +
                        "Please select other slot", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DatesAndTimeActivity.this, "Please Select Time slot", Toast.LENGTH_SHORT).show();
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
                params.put("servicedate", datetext);
                params.put("price", packagesListModel.getPackage_price());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DatesAndTimeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void onSuccessAddtoCart() {
        Intent intent = new Intent(DatesAndTimeActivity.this, CartActivity.class);
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
                Intent intent = new Intent(DatesAndTimeActivity.this, CartActivity.class);
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