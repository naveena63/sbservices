package com.app.sb.sbservices.User;

import android.content.Intent;
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

public class ChangePasswordActivity extends AppCompatActivity {
    private static String NEWPWD = "new_pass";
    private static String CHANGE = "confirm_pass";
    private static String PHONE = "mobile_number";

    private EditText et_change, et_new;
    private Button btnSubmit;
    private ApiCallingFlow apiCallingFlow;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
getSupportActionBar().hide();
        prefManager = new PrefManager(this);
        et_new = (EditText) findViewById(R.id.etPassword);
        et_change = (EditText) findViewById(R.id.etconfirmPassword);
        btnSubmit = (Button) findViewById(R.id.buttonSbmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestServiceApi();
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
            ResetPassword();
        }
    }

    private void ResetPassword() {
        String phone = prefManager.getPhoneNumber();
        String new_pwd = et_new.getText().toString();
        String change_pwd = et_change.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.CHANGE_PASS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        apiCallingFlow.onSuccessResponse();
                        try {
                            JSONObject object = new JSONObject(response);

                            String status = object.getString("status");

                            String msg = object.getString("msg");
                            switch (status) {
                                case "success":
                                    Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ChangePasswordActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "0":
                                    Toast.makeText(ChangePasswordActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                    break;
                                case "error":
                                    Toast.makeText(ChangePasswordActivity.this, "Error Occured Please Try Again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChangePasswordActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(PHONE, phone);
                map.put(NEWPWD, new_pwd);
                map.put(CHANGE, change_pwd);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    }

