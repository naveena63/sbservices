package com.app.sb.sbservices.Menu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.AppConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsActivity extends AppCompatActivity {
    TextView textCompanyName, textAddress, textLocation, textNmber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        textCompanyName = findViewById(R.id.company);
        textAddress = findViewById(R.id.address);
        textLocation = findViewById(R.id.location);
        textNmber = findViewById(R.id.number);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.contact));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        loadData();
    }

    private void loadData() {
        final RequestQueue requestQueue = Volley.newRequestQueue(ContactUsActivity.this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                AppConstants.CONTACT_US,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String compny= jsonObject.getString("company_name");
                            String add= jsonObject.getString("address");
                            String loc= jsonObject.getString("location");
                            String num= jsonObject.getString("number");
                            textCompanyName.setText(compny);
                            textAddress.setText(add);
                            textLocation.setText(loc);
                            textNmber.setText(num);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                        requestQueue.stop();
                    }
                }
        );

        requestQueue.add(stringRequest);
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
