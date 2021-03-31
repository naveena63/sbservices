package com.app.sb.sbservices.Packages;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.Cart.CartActivity;
import com.app.sb.sbservices.NewDate.DatesAndTimeActivity;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.TimeAndDate.TimesoltActivity;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.GlobalVariable;
import com.app.sb.sbservices.Utils.PrefManager;
import com.app.sb.sbservices.Utils.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PackagesActivity extends AppCompatActivity implements PackageListener {

    public static String TAG = "PackageActivity";
    List<PackageModel> packageModelslist;
    PackageAdapter packageAdapter;
    RecyclerView recyclerView;
    private String serviceId;
    public ApiCallingFlow apiCallingFlow;
    PrefManager prefManager;
    ImageView packgeImage;
    TextView noPackageAvailable;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.select_package));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        prefManager = new PrefManager(this);
        packgeImage=findViewById(R.id.imageView);
        noPackageAvailable = findViewById(R.id.no_packages_available);
        packageModelslist = new ArrayList<>();
        serviceId = getIntent().getStringExtra("serviceId");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PackagesActivity.this));
        requestServiceApi();


    }

    private void requestServiceApi() {
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        apiCallingFlow = new ApiCallingFlow(this, parentLayout, false) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            packagesList(AppConstants.PACKAGES_URL, serviceId);

        }

    }

    public void packagesList(String packagesUrl, String serviceId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, packagesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        apiCallingFlow.onSuccessResponse();


                        try {
                            JSONObject   jsonMainObject = new JSONObject(s);
                            String  status = jsonMainObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray listArray = jsonMainObject.getJSONArray("packages");
                                JSONObject memberObject;
                                if (packageModelslist != null) {
                                    packageModelslist.clear();
                                }
                                for (int i = 0; i < listArray.length(); i++) {
                                    memberObject = listArray.getJSONObject(i);
                                    PackageModel model = new PackageModel();
                                    model.setPackage_name(memberObject.getString("package_name"));
                                    model.setPackageId(memberObject.getString("id"));
                                    model.setService_id(memberObject.getString("service_id"));
                                    model.setService_slug(memberObject.getString("service_slug"));
                                    model.setPackage_price(memberObject.getString("package_price"));
                                    model.setSub_package_status(memberObject.getString("sub_package_status"));
                                    model.setPackageImage(memberObject.getString("package_image"));
                                    model.setExclusionl(memberObject.getString("exclusion"));
                                    model.setInclusion(memberObject.getString("inclusion"));
                                    model.setEquipment(memberObject.getString("equipment"));
                                    model.setApproximate(memberObject.getString("approximate"));

                                    packgeImage.getLayoutParams().height = GlobalVariable.deviceHeight / 5;
                                    packgeImage.getLayoutParams().width = GlobalVariable.deviceWidth / 1;
                                   // Glide.with(this).load("https://goklean4u.com/"+subServicesListModel.getSub_cat_banner()).placeholder(R.drawable.icon_placeholder).error(R.drawable.icon_placeholder).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(serviceImageView);

                                    packageModelslist.add(model);
                                }
                                if (packageModelslist.size() > 0) {
                                    packageAdapter = new PackageAdapter(packageModelslist, PackagesActivity.this);
                                    recyclerView.setAdapter(packageAdapter);
                                    noPackageAvailable.setVisibility(View.GONE);
                                }
                            } else {
                                noPackageAvailable.setText(jsonMainObject.getString("msg"));
                                noPackageAvailable.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            System.out.println(TAG + " Exception=======>" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        apiCallingFlow.onErrorResponse();
                        try {
                            System.out.println("volley error...." + volleyError.getMessage().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PackagesActivity.this, "Bad internet connection please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                params.put("service_id", prefManager.getServiceId());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PackagesActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onClickAddToCart(PackageModel packagesListModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_package", packagesListModel);
        Intent intent = new Intent(PackagesActivity.this, DatesAndTimeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
TextView textCartItemCount;
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_credits);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_credits: {
                Intent intent = new Intent(PackagesActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }


        }
        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() {
        if (textCartItemCount != null) {
            if (Singleton.getInstance().cartItemsCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(Singleton.getInstance().cartItemsCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }



}
