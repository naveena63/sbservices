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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {
    public static final String KEY_MOBILE = "mobile_number";
    EditText mobileNumber;
    Button button_submit;
    private ApiCallingFlow apiCallingFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        mobileNumber = findViewById(R.id.etMobile);
        button_submit = findViewById(R.id.buttonSbmit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobileNumber.getText().toString().isEmpty()) {
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
            forgotPassword();
        }
    }

    private void forgotPassword() {
        final String phoneNumber = mobileNumber.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.FORGOT_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String myResponce = jsonObject.getString("status");

                    if (myResponce.equals("success")) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyForgotOtpActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ForgotPasswordActivity.this, "otp Sent to your mobile number", Toast.LENGTH_SHORT).show();
                    } else if (myResponce.equals("error")) {
                        Toast.makeText(ForgotPasswordActivity.this, "Mobile number not register  with US", Toast.LENGTH_SHORT).show();
                    finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              //  Log.i("log", "_-------------- Response----------------" + response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPasswordActivity.this, "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();
                        Log.i("forgot Password", "_----" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_MOBILE, phoneNumber);
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
