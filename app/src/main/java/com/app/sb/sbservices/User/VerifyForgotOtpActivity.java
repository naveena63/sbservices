package com.app.sb.sbservices.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyForgotOtpActivity extends AppCompatActivity {
    public static final String KEY_OTP = "otp";

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";


    EditText enterOtp;
    Button subtButton;
    private ApiCallingFlow apiCallingFlow;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_forgot_otp);

        enterOtp=findViewById(R.id.etOtp);
        subtButton=findViewById(R.id.buttonSbmit);
getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefManager = new PrefManager(this);
        subtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!enterOtp.getText().toString().isEmpty())
                    requestServiceApi() ;
            }
        });


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
            otp();
        }
    }

    private void otp() {

        SharedPreferences sp = getApplicationContext().getSharedPreferences(
                "sharedPrefName", 0);





        final String otp = enterOtp.getText().toString();
        //  final String userid = "CUST48570";
        //  Log.e("user/otp",otp + "" + userid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.VERIFY_FORGOR_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String myResponce = jsonObject.getString("status");

                            if (myResponce.equals("success"))
                            {

                                Toast.makeText(VerifyForgotOtpActivity.this, "otp verified", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(VerifyForgotOtpActivity.this,ChangePasswordActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (myResponce.equals("error"))
                            {

                                Toast.makeText(VerifyForgotOtpActivity.this, "Please Enter Valid otp", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("log", "_-------------- Response----------------" + response);
                        //   Toast.makeText(OTPpageActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerifyForgotOtpActivity.this, "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();
                        Log.i("uyt", "_----" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //  map.put("mobile_number", "9701064173");
                map.put("mobile_number",prefManager.getPhoneNumber());
                map.put(KEY_OTP, otp);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    }
