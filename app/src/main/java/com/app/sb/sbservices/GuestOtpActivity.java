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
import com.app.sb.sbservices.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GuestOtpActivity extends AppCompatActivity {
Button generateOtpButn;
RequestQueue requestQueue;
EditText mobile_number;
ApiCallingFlow apiCallingFlow;
PrefManager prefManager;
ImageView imageviewArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_otp);
        getSupportActionBar().hide();
        prefManager=new PrefManager(this);
        requestQueue= Volley.newRequestQueue(this);
        generateOtpButn=findViewById(R.id.generateOtpButn);
        mobile_number=findViewById(R.id.mobileNumber);
        imageviewArrow=findViewById(R.id.arrow_image);

        imageviewArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuestOtpActivity.this,GuestLoginActivity.class);
                startActivity(intent);
            }
        });

        Typeface ttypeface=Typeface.createFromAsset(getAssets(),"MontserratAlternates-Medium.otf");
        mobile_number.setTypeface(ttypeface);
        generateOtpButn.setTypeface(ttypeface);


        generateOtpButn.setOnClickListener(new View.OnClickListener() {
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
            guestLogin();
        }
    }
    public void guestLogin() {
final  String mobile=mobile_number.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.Guest_Register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("responseGuest","guest"+response);
                    String status = jsonObject.getString("status");
                   String message = jsonObject.getString("msg");
                    if (status.equalsIgnoreCase("1")) {
                      //  prefManager.storeValue(AppConstants.APP_USER_LOGIN,true);
                        Intent i = new Intent(GuestOtpActivity.this, GuestOtpGenerateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobileNumber", mobile);
                        i.putExtras(bundle);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();

                        Toast.makeText(GuestOtpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }else if (status.equalsIgnoreCase("0"))
                    {
                       Toast.makeText(GuestOtpActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GuestOtpActivity.this,"Something Went wrong.. try again",Toast.LENGTH_LONG ).show();
                Log.i("notlogin","_---------------------------------" +error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map= new HashMap<>();
                map.put("token","c0304a62dd289bdc7364fb974c2091f6");
                map.put("mobile",mobile);
                map.put("fcm_token",SharedPreference.getStringPreference(GuestOtpActivity.this,"TOKEN"));
Log.i("guesttoken",SharedPreference.getStringPreference(GuestOtpActivity.this,"TOKEN"));


                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    private int validate() {


        int flag = 0;
        if (mobile_number.getText().toString().isEmpty()) {
            mobile_number.setError("Enter PhoneNumber");
            mobile_number.requestFocus();
            flag = 1;
        }

        return flag;
    }

}
