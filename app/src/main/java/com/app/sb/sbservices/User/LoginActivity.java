package com.app.sb.sbservices.User;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.sb.sbservices.BottomNavActivity;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
TextView tvGotosignup,forgotPswrd;
EditText phoneNumber,passwrd;
Button buttonLogin;
    private static final String KEY_MOBILE = "phone";
    private static final String KEY_PASSWORD = "password";
    private PrefManager prefManager;
    private ApiCallingFlow apiCallingFlow;
    private static final int GRANT_LOC_ACCESS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber=findViewById(R.id.etPhone);
        passwrd=findViewById(R.id.etPassword);
        prefManager = new PrefManager(this);
        tvGotosignup=findViewById(R.id.tvGotosignup);
        forgotPswrd=findViewById(R.id.forgot_paswrd);
       getSupportActionBar().hide();
        tvGotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgotPswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        buttonLogin=findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int verify = validate();
                if (verify == 0) {
                    requestServiceApi();
                }

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
            login();
        }
    }

    private void login() {
        final String phone = phoneNumber.getText().toString();
        final String password = passwrd.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LOGIN_URL,
                            new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        apiCallingFlow.onSuccessResponse();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status=object.getString("status");
                            String msg=object.getString("msg");
                            switch (status) {
                                case "success":
                                    JSONObject json = object.getJSONObject("user_profile");
                                    String user_id = json.getString("user_id");
                                    String username=json.getString("name");
                                    String email=json.getString("email");
                                    String mobile=json.getString("mobile");

                                    prefManager.storeValue(AppConstants.APP_USER_LOGIN,true);
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_ID, user_id);
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, json.getString("name"));
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, json.getString("email"));
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_MOBILE, json.getString("mobile"));
                                    Log.e("phon", prefManager.getPhoneNumber());
                                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, BottomNavActivity.class);
                                    startActivity(intent);
                                    prefManager.setUserId(user_id);
                                    Log.e("uesr_id", prefManager.getUserId());
                                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                                    prefManager.setUsername(username);
                                    prefManager.setPhoneNumber(mobile);
                                    prefManager.setEmailId(email);
                                    finish();

                                    break;
                                case "0":
                                    Toast.makeText(LoginActivity.this, "Mobile Number Not Registered With Us", Toast.LENGTH_SHORT).show();
                                    break;
                                case "error1":
                                    Toast.makeText(LoginActivity.this,"otp not verified", Toast.LENGTH_SHORT).show();
                                    Intent in=new Intent(LoginActivity.this,OTPActivity.class);
                                    startActivity(in);

                                    break;

                                case "error":
                                    Toast.makeText(LoginActivity.this,"Password Wrong", Toast.LENGTH_SHORT).show();

                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("login","_--------------Login Response----------------" +response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiCallingFlow.onErrorResponse();
                        error.printStackTrace();
                        Toast.makeText(LoginActivity.this,"Something Went wrong.. try again",Toast.LENGTH_LONG ).show();
                        Log.i("notlogin","_---------------------------------" +error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_MOBILE, phone);
                map.put(KEY_PASSWORD, password);
                map.put("fcm_token", SharedPreference.getStringPreference(LoginActivity.this,"TOKEN"));
                Log.e("dchvbdgvcgdvc","cdh vb "+SharedPreference.getStringPreference(LoginActivity.this,"TOKEN"));

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private int validate() {


        int flag = 0;
        if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumber.setError(getString(R.string.enter_valid_number));
            phoneNumber.requestFocus();
            flag = 1;
        }
        else if (passwrd.getText().toString().isEmpty()) {
            passwrd.setError(getString(R.string.enter_password));
            passwrd.requestFocus();
            flag = 1;
        }
        else if (phoneNumber.length() != 10) {
            phoneNumber.requestFocus();
            phoneNumber.setError(getString(R.string.error_invalid_mobile_number));
        }
        return flag;
    }

}
