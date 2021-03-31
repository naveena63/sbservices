package com.app.sb.sbservices;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.Cart.CartActivity;
import com.app.sb.sbservices.HomeBanners.Banner;
import com.app.sb.sbservices.HomeBanners.BannerAdapter;
import com.app.sb.sbservices.OfferBanners.OfferBanners;
import com.app.sb.sbservices.OfferBanners.OffersAdapter;
import com.app.sb.sbservices.Packages.PackagesActivity;
import com.app.sb.sbservices.Profile.ProfileFragment;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;
import com.app.sb.sbservices.Utils.Singleton;
import com.app.sb.sbservices.services.HomeScreenListener;
import com.app.sb.sbservices.services.ServiceAdpter;
import com.app.sb.sbservices.services.ServicesListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment implements HomeScreenListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String NAMES = "category_name";
    public static final String IMAGES = "category_icon";
    private TextView mTextView, viewMoreServcies;
    private List<Banner> stringList;
    private ArrayList<String> images;
    private ArrayList<String> names;
    private ViewPager mPager;
    private int currentPage;
    View rootView;
    RequestQueue requestQueue;
    PrefManager prefManager;
    RecyclerView recyclerView, recyclerView1;
    List<OfferBanners> offerBanners;
    List<ServicesListModel> servicesList;
    ApiCallingFlow apiCallingFlow;
    private HomeScreenListener homeScreenListener;

    public static Fragment newInstance(String s, String s1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, s);
        args.putString(ARG_PARAM2, s1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.sb_toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.toolbar_background));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        requestQueue = Volley.newRequestQueue(getActivity());
        images = new ArrayList<>();
        names = new ArrayList<>();
        servicesList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        mTextView = rootView.findViewById(R.id.mytext);
        viewMoreServcies = rootView.findViewById(R.id.viewMoreServcies);
        prefManager = new PrefManager(getActivity());
        mTextView.setSelected(true);
        setHasOptionsMenu(true);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "MontserratAlternates-Medium.otf");
        viewMoreServcies.setTypeface(typeface);
        viewMoreServcies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewMoreServicesActivity.class);
                startActivity(intent);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        DividerItemDecoration itemDecor2 = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.addItemDecoration(itemDecor2);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        getSliderImages();
        getServices();
        getOffersBanner();
        loadData();
        return rootView;
    }

    private void getSliderImages() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.SLIDER_BANNERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        stringList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("banners");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String image = object.getString("banner_image");
                                    String imageName = object.getString("image_name");
                                    Banner banner = new Banner();
                                    banner.setBannerImage(image);
                                    if (imageName != null) {
                                        banner.setImageName(imageName);
                                    } else if (imageName.matches(".")) {
                                        banner.setImageName("Banners");
                                    }
                                    stringList.add(banner);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (stringList.size() > 0) {

                            mPager = rootView.findViewById(R.id.pager);
                            mPager.setAdapter(new BannerAdapter(getActivity(), stringList));
                            CircleIndicator indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
                            indicator.setViewPager(mPager);

                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == stringList.size()) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 5000, 5000);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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

                        Toast.makeText(getContext(), "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void showGrid(JSONArray jsonArray) {
        for (int i = 0; i < 9; i++) {
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
        getUserCartItemsCount();
    }

    private void getUserCartItemsCount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.CART_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog

                        String message = "";
                        String status;
                        JSONObject jsonMainObject;
                        JSONObject dataObject;

                        try {
                            jsonMainObject = new JSONObject(s);
                            status = jsonMainObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray listArray = jsonMainObject.getJSONArray("cartitems");
                                Singleton.getInstance().cartItemsCount = listArray.length();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                params.put("user_id", prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                return params;
            }
        };
        Context mContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    private void getOffersBanner() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.HORIZONTAL_IMAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        offerBanners = new ArrayList<>();
                        try {

                            JSONObject jsonMainObject = new JSONObject(response);

                            String status = jsonMainObject.getString("status");

                            if (status.equalsIgnoreCase("success")) {

                                JSONArray jsonArray = jsonMainObject.getJSONArray("horizantalimage");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String imageUrl = jsonObject.getString("offer_image");

                                    Log.e("hdhdhdh", "ddhdhdh" + imageUrl);
                                    OfferBanners model = new OfferBanners();
                                    model.setBannerImage(imageUrl);
                                    offerBanners.add(model);
                                }
                                OffersAdapter offersAdapter = new OffersAdapter(getActivity(), offerBanners);
                                recyclerView1.setAdapter(offersAdapter);


                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Bad internet connection please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void loadData() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                AppConstants.SCROLLING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String description = jsonObject.getString("description");
                            mTextView.setText(description);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestQueue.stop();
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

    @Override
    public void onClickGridItem(String serviceId, String serviceName) {
        Intent intent = new Intent(getActivity(), PackagesActivity.class);
        intent.putExtra("serviceId", serviceId);
        intent.putExtra("service_name", serviceName);
        startActivity(intent);
    }

    TextView textCartItemCount;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_credits);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.action_credits: {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}