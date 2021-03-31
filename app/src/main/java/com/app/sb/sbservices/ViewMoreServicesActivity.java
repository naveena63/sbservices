package com.app.sb.sbservices;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.services.HomeScreenListener;
import com.app.sb.sbservices.services.ServiceAdpter;
import com.app.sb.sbservices.services.ServicesListModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMoreServicesActivity extends AppCompatActivity {
    private ArrayList<String> images;
    private ArrayList<String> names;
    public static final String NAMES = "category_name";
    public static final String IMAGES = "category_icon";
    List<ServicesListModel> servicesList;
    RecyclerView recyclerView;
    private HomeScreenListener homeScreenListener;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more_services);
        recyclerView=findViewById(R.id.recyclerView);
        images = new ArrayList<>();
        names = new ArrayList<>();
        servicesList = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor2 = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(itemDecor2);

        getServices();
    }
    private void getServices() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ALL_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  appBarLayout.setExpanded(true);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("services");
                                Log.i("notlogin", "_-----------" + jsonArray);
                                showGrid(jsonArray);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("HOME", "_--------------CATEGORY Response----------------" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ViewMoreServicesActivity.this, "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ViewMoreServicesActivity.this);
        requestQueue.add(stringRequest);
    }
    public void showGrid(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = null;
            try {
                obj = jsonArray.getJSONObject(i);
                images.add(obj.getString(IMAGES));
                names.add(obj.getString(NAMES));
                ServicesListModel model = new ServicesListModel();
                model.setServiceId(obj.getString("id"));
                model.setServiceImage(obj.getString(IMAGES));
                model.setServiceName(obj.getString(NAMES));
                servicesList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ServiceAdpter adapter = new ServiceAdpter(servicesList, homeScreenListener);
        recyclerView.setAdapter(adapter);
    }
}
