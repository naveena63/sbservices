package com.app.sb.sbservices.DescriptionActivity;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import in.juspay.godel.ui.OnScreenDisplay;

public class DescrptionMainActivity extends AppCompatActivity {


    PrefManager prefManager;
    RequestQueue requestQueue;
    TextView descriptionText, textview, textview1, textView2, textView3, textView4, textview5,textapproximate, equiopemnt, inclusion, exclusion;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descrption_main);
        descriptionText = findViewById(R.id.descrption);
        textapproximate = findViewById(R.id.approximate);
        equiopemnt = findViewById(R.id.equipment);
        inclusion = findViewById(R.id.inclision);
        exclusion = findViewById(R.id.exclusion);

        linearLayout1 = findViewById(R.id.linear1);
        linearLayout2 = findViewById(R.id.linear2);
        linearLayout3 = findViewById(R.id.linear3);
        linearLayout4 = findViewById(R.id.linear4);
        linearLayout5 = findViewById(R.id.linear5);
        textview5 = findViewById(R.id.textview6);

        prefManager = new PrefManager(this);
        textview = findViewById(R.id.textview);
        textview1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        textView4 = findViewById(R.id.textview4);

        textview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DescrptionMainActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout1.getVisibility() != View.VISIBLE) {
                    linearLayout1.setVisibility(View.VISIBLE);
                } else {
                    linearLayout1.setVisibility(View.GONE);
                }

            }
        });
        textview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout2.getVisibility() != View.VISIBLE) {
                    linearLayout2.setVisibility(View.VISIBLE);
                } else {
                    linearLayout2.setVisibility(View.GONE);
                }

            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout3.getVisibility() != View.VISIBLE) {
                    linearLayout3.setVisibility(View.VISIBLE);
                } else {
                    linearLayout3.setVisibility(View.GONE);
                }

            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout4.getVisibility() != View.VISIBLE) {
                    linearLayout4.setVisibility(View.VISIBLE);
                } else {
                    linearLayout4.setVisibility(View.GONE);
                }

            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout5.getVisibility() != View.VISIBLE) {
                    linearLayout5.setVisibility(View.VISIBLE);
                } else {
                    linearLayout5.setVisibility(View.GONE);
                }

            }
        });

        //descrption
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ALL_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("services");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String service_slug = jsonObject1.getString("description");
                            descriptionText.setText(service_slug);
                        }
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
                return map;
            }
        };
        requestQueue.add(stringRequest);




        //Approcimte
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConstants.PACKAGES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("packages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String approximate = jsonObject1.getString("approximate");
                            textapproximate.setText(approximate);
                        }

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
                map.put("service_id", prefManager.getServiceId());
                Log.i("serviceid", "serviceId" + prefManager.getServiceId());

                return map;


            }

        };
        requestQueue.add(stringRequest1);
        //eqipument
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, AppConstants.PACKAGES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("packages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String approximate = jsonObject1.getString("equipment");
                            equiopemnt.setText(approximate);
                        }

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
                map.put("service_id", prefManager.getServiceId());


                return map;


            }

        };
        requestQueue.add(stringRequest2);

        //inclusion
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, AppConstants.PACKAGES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("packages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String approximate = jsonObject1.getString("inclusion");
                            inclusion.setText(approximate);
                        }

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
                map.put("service_id", prefManager.getServiceId());


                return map;


            }

        };
        requestQueue.add(stringRequest3);
        //exclsion
        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, AppConstants.PACKAGES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("packages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String approximate = jsonObject1.getString("exclusion");
                            exclusion.setText(approximate);
                        }

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
                map.put("service_id", prefManager.getServiceId());


                return map;


            }

        };
        requestQueue.add(stringRequest4);


    }


    }






