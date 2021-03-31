package com.app.sb.sbservices;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GuestOtpGenerateActivity extends AppCompatActivity {
    Button submitOtpButn;
    EditText enterOtp;
    ApiCallingFlow apiCallingFlow;
    RequestQueue requestQueue;
    PrefManager prefManager;
    ImageView imageArrow;
TextView resendOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_otp_generate);
        getSupportActionBar().hide();
        submitOtpButn = findViewById(R.id.submitOtpButn);
        imageArrow = findViewById(R.id.imageArrow);
        enterOtp = findViewById(R.id.enterOtp);
        resendOtp = findViewById(R.id.resendOtp);
        prefManager = new PrefManager(this);
        requestQueue = Volley.newRequestQueue(this);
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    apiRequestFlow();

                }

        });


        Typeface typeface = Typeface.createFromAsset(getAssets(), "MontserratAlternates-Medium.otf");
        submitOtpButn.setTypeface(typeface);
        enterOtp.setTypeface(typeface);

        submitOtpButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    requestServiceApi();

                }
            }
        });

        imageArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestOtpGenerateActivity.this, GuestOtpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void apiRequestFlow() {
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        apiCallingFlow = new ApiCallingFlow(this, parentLayout, true) {
            @Override
            public void callCurrentApiHere() {
                apiRequestFlow();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            guestResendGenrateOtp();
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
            guestGenrateOtp();
        }
    }

    private void guestGenrateOtp() {
        Bundle bundle = getIntent().getExtras();
        String mobilenum = bundle.getString("mobileNumber");
        final String otp = enterOtp.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.guest_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("responseGuest", "guest" + response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equalsIgnoreCase("1")) {
                        prefManager.storeValue(AppConstants.APP_USER_LOGIN,true);
                        Intent i = new Intent(GuestOtpGenerateActivity.this, BottomNavActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();

                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                        String userId = jsonObject1.getString("user_id");
                        String phone = jsonObject1.getString("phone");


                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_ID, userId);
                        prefManager.setUserId(userId);
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_MOBILE, phone);
                        prefManager.setPhoneNumber(phone);

                        Toast.makeText(GuestOtpGenerateActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (status.equalsIgnoreCase("0")) {
                        Toast.makeText(GuestOtpGenerateActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiCallingFlow.onErrorResponse();
                error.printStackTrace();
                Toast.makeText(GuestOtpGenerateActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                Log.i("notlogin", "_---------------------------------" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("token", "c0304a62dd289bdc7364fb974c2091f6");
                map.put("mobile", mobilenum);
                map.put("otp", otp);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void guestResendGenrateOtp() {
        Bundle bundle = getIntent().getExtras();
        String mobilenum = bundle.getString("mobileNumber");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.RESEND_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("responseGuest", "guest" + response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(GuestOtpGenerateActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else if (status.equalsIgnoreCase("0")) {
                        Toast.makeText(GuestOtpGenerateActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiCallingFlow.onErrorResponse();
                error.printStackTrace();
                Toast.makeText(GuestOtpGenerateActivity.this, ""+error, Toast.LENGTH_LONG).show();
                Log.i("notlogin", "_---------------------------------" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("token","c0304a62dd289bdc7364fb974c2091f6");
                map.put("mobile","9908562939");
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }
    private int validate() {


        int flag = 0;
        if (enterOtp.getText().toString().isEmpty()) {
            enterOtp.setError("Enter Otp");
            enterOtp.requestFocus();
            flag = 1;
        }

        return flag;
    }
}
