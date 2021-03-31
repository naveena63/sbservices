package com.app.sb.sbservices.ReferalId;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetReferalCode extends AppCompatActivity {
    TextView textViewrefid,textViewreferPoints;
    Button inviteButton;
    String refid;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Refer Your Friends");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        textViewrefid=findViewById(R.id.textview_refid);
        inviteButton=findViewById(R.id.button_invite);
        textViewreferPoints=findViewById(R.id.textView_rpoints);

        prefManager = new PrefManager(this);
        getReferalCode();

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //      getReferelCode();

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                            "ReferalCode : "+refid);
                    startActivity(shareIntent);
                }catch (Exception e){
                    //   Toast.makeText(RewardPointsActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getReferalCode() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_REFERAL_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String myResponce = jsonObject.getString("status");
                            if (myResponce.equals("success")) {
                                refid= jsonObject.getString("referral_id");
                                textViewrefid.setText("Your Referal Id : "+refid);
                            } else if (myResponce.equals("error")) {
                                Toast.makeText(GetReferalCode.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("log", "_-------------- Response----------------" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GetReferalCode.this, "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();
                        Log.i("error", "_----" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", prefManager.getUserId());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

