package com.app.sb.sbservices.Notifications;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class NotificationsActicity extends AppCompatActivity {
RecyclerView recyclerView;
    List<NotificationsModel> notificationsModelList;
    NotificationsAdapter notificationsAdapter;

    public ApiCallingFlow apiCallingFlow;
    PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_acticity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.notifictaion));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        notificationsModelList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsActicity.this));
        requestServiceApi();


    }

    private void requestServiceApi() {



        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        apiCallingFlow = new ApiCallingFlow(this, parentLayout, false) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            notifiction();

        }

    }

    private void notifiction() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,AppConstants.NOTFY_URL,
                new Response.Listener<String>() {

                    //open this url in post man

                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        apiCallingFlow.onSuccessResponse();
                        String status;
                        JSONObject jsonMainObject;


                        try {

                            jsonMainObject = new JSONObject(s);

                            status = jsonMainObject.getString("status");

                            if (status.equalsIgnoreCase("success")) {

                                JSONArray listArray = jsonMainObject.getJSONArray("services");
                                JSONObject memberObject;


                                for (int i = 0; i < listArray.length(); i++) {
                                 Log.i("response","naveena"+i);
                                    memberObject = listArray.getJSONObject(i);

                                    NotificationsModel model = new NotificationsModel();

                                    model.setNotification(memberObject.getString("notification"));

                                    // model.setSubCatImg(memberOject.getString("sub_cat_image"));
                                    notificationsModelList.add(model);

                                }

                                notificationsAdapter = new NotificationsAdapter(notificationsModelList, NotificationsActicity.this);
                                recyclerView.setAdapter(notificationsAdapter);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        apiCallingFlow.onErrorResponse();

                        try {
                            System.out.println("volley error...." + volleyError.getMessage().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(NotificationsActicity.this, "Bad internet connection please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
//                params.put("service_id", prefManager.getServiceId());
//
//              //  Log.e("hddhdh", "dhhdhdh" + prefManager.getServiceId());
////                Log.e("service_id", serviceId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NotificationsActicity.this);
// stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);



        return true;
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






