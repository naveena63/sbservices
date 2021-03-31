package com.app.sb.sbservices.User;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String KEY_NAME = "name";
    public static final String KEY_MAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_REFERAL = "referral_id";

    EditText  editTextEmail, editTextUsername, editTextPassword, editTextPhone;
    TextView linkSignup;
    Button buttonRegister;
    private ApiCallingFlow apiCallingFlow;
    private SharedPreferences registerPreferences;
    private SharedPreferences.Editor registerPrefsEditor;
    TextView verityReferealCode,referalPointsValue;
    LinearLayout llReferalCode;
    String refCode;
    EditText editReferalCode;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prefManager = new PrefManager(RegisterActivity.this);
        editTextUsername = findViewById(R.id.your_full_name);
        editTextEmail = findViewById(R.id.your_email);
        editTextPhone = findViewById(R.id.your_phonenumber);
        editTextPassword = findViewById(R.id.your_password);
        buttonRegister=findViewById(R.id.buttonRegister);
        linkSignup = findViewById(R.id.tvGotosignup);
        editReferalCode=findViewById(R.id.your_referalid);
        verityReferealCode=findViewById(R.id.verityReferealCode);
        llReferalCode= findViewById(R.id.llReferalCode);
        getSupportActionBar().hide();
        referalPointsValue= findViewById(R.id.referalPointsValue);
        registerPreferences = getSharedPreferences("registerPrefs", MODE_PRIVATE);
        registerPrefsEditor = registerPreferences.edit();


        registerPreferences = getSharedPreferences("registerPrefs", MODE_PRIVATE);
        registerPrefsEditor = registerPreferences.edit();
        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        verityReferealCode.setOnClickListener(v -> {

            refCode = editReferalCode.getText().toString().trim();
            if (!refCode.isEmpty() && refCode != null) {
                verifyReferalCode();
            } else {
                Toast.makeText(this, "Plaese enter Referal Code", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int verify = validate();
                if (verify == 0) {
                    requestServiceApi();
                } else {
                    Toast.makeText(RegisterActivity.this, "enter all fields", Toast.LENGTH_SHORT).show();
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
                registerOtp();
            }
        }

    private void registerOtp() {

        final String name = editTextUsername.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String phone = editTextPhone.getText().toString();
        final String Referal = editReferalCode.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        apiCallingFlow.onSuccessResponse();
                        try {
                            JSONObject object = new JSONObject(response);

                            String status = object.getString("status");


                            if (status.equals("success")) {
                                JSONObject json = object.getJSONObject("user_data");

                                String name = json.getString("name");
                                String email = json.getString("email");
                                String phone = json.getString("phone");



                                prefManager.setUsername(name);
                                prefManager.setPhoneNumber(phone);
                                prefManager.setEmailId(email);
                                Log.e("email_id", prefManager.getEmailId());
                                String user_id = json.getString("user_id");
                                SharedPreferences sp = getApplicationContext().
                                        getSharedPreferences(
                                                "sharedPrefName", 0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("userid", user_id);
                                editor.apply();

                                Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                                startActivity(intent);
                            } else if (status.equals("error1")) {
                                Toast.makeText(RegisterActivity.this, "Mobile Number Exist", Toast.LENGTH_SHORT).show();
                            } else if (status.equals("0")) {
                                Toast.makeText(RegisterActivity.this, "Email Exist Please Enter Another Email", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("android", "_--------------Registration Response----------------" + response);
                        // Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiCallingFlow.onErrorResponse();
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("An", "_-------------Error--------------------" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_NAME, name);
                map.put(KEY_MAIL, email);
                map.put(KEY_PASSWORD, password);
                map.put(KEY_PHONE, phone);
                map.put(KEY_REFERAL, Referal);
                map.put("fcm_token", SharedPreference.getStringPreference(RegisterActivity.this,"TOKEN"));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void verifyReferalCode() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ASSIGN_REWARDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("resposne", "response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    //    String msg=jsonObject.getString("msg");

                    if (status.equals("true")) {
                        Toast.makeText(RegisterActivity.this, "Referal Id Verified", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("false")) {

                        Toast.makeText(RegisterActivity.this, "Referal Id Not Verified", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("referral_id", refCode);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private int validate() {
        int flag = 0;
         if (editTextUsername.getText().toString().isEmpty()) {
            editTextUsername.setError(getString(R.string.enter_name));
            editTextUsername.requestFocus();
            flag = 1;

        } else if (editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setError(getString(R.string.enter_password));
            editTextPassword.requestFocus();
            flag = 1;
        } else if (editTextPhone.getText().toString().isEmpty()) {
            editTextPhone.setError(getString(R.string.enter_valid_number));
            editTextPhone.requestFocus();
            flag = 1;

        } else if (editTextPhone.length() != 10) {
            editTextPhone.requestFocus();
            editTextPhone.setError(getString(R.string.error_invalid_mobile_number));
        }
        return flag;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }



    }



