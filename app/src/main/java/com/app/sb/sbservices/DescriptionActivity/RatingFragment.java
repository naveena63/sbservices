package com.app.sb.sbservices.DescriptionActivity;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class RatingFragment extends Fragment {

    View view;
    RatingBar ratingBar;
    EditText etComment;
    RequestQueue requestQueue;
    PrefManager prefManager;
    Button submitButton;
    public ApiCallingFlow apiCallingFlow;
    RecyclerView recyclerView;
    List<RatingModel> ratingModelList;
    RatingAdapter ratingAdapter;
    TextView no_packages_available;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_rating, container, false);
        prefManager = new PrefManager(getContext());
        requestQueue = Volley.newRequestQueue(getContext());
        no_packages_available = view.findViewById(R.id.no_packages_available);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.submitButton);
        etComment = view.findViewById(R.id.comment);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ratingModelList = new ArrayList<>();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    ratingreview();
                }
            }
        });
        requestServiceApi();
        return view;
    }

    private int validate() {
        int flag = 0;
        if (etComment.getText().toString().isEmpty()) {
            etComment.setError("enter review");
            etComment.requestFocus();
            flag = 1;
        }

        return flag;
    }
    private void requestServiceApi() {



        RelativeLayout parentLayout =(RelativeLayout)view. findViewById(R.id.relative_layout);

        apiCallingFlow = new ApiCallingFlow(getContext(), parentLayout, false) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            rating_user_review();

        }

    }

    private void rating_user_review() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,AppConstants.RATING_SERVICE_REVIEW,
                new Response.Listener<String>() {

                    //open this url in post man

                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        apiCallingFlow.onSuccessResponse();
                        String status;
                        JSONObject jsonMainObject;


                        try {

                            jsonMainObject = new JSONObject(s);

                            status = jsonMainObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                JSONArray listArray = jsonMainObject.getJSONArray("reviews");
                                JSONObject memberObject;


                                for (int i = 0; i < listArray.length(); i++) {
                                    Log.i("rtaing service response","reposne"+i);
                                    memberObject = listArray.getJSONObject(i);

                                    RatingModel model = new RatingModel();

                                    model.setDescrption(memberObject.getString("review_text"));
                                     model.setRating((memberObject.getString("review_rating")));
                                    ratingModelList.add(model);

                                }
                                if (ratingModelList.size() > 0) {
                                    ratingAdapter = new RatingAdapter(ratingModelList, getActivity());
                                    ratingAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(ratingAdapter);
                                    no_packages_available.setVisibility(View.GONE);
                                }
                            } else {
                                no_packages_available.setText("no data found");
                                no_packages_available.setVisibility(View.VISIBLE);



                              /*  ratingAdapter = new RatingAdapter(ratingModelList, getActivity());
                                recyclerView.setAdapter(ratingAdapter);*/

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
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
                            Toast.makeText(getActivity(), "Bad internet connection please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
               params.put("service_id", prefManager.getServiceId());
               params.put("token","c0304a62dd289bdc7364fb974c2091f6");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
// stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    public  void  ratingreview(){
        String commentEt = etComment.getText().toString();
        String rate=String.valueOf(ratingBar);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.RATING_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("reviews");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String review_rating = jsonObject1.getString("review_rating");
                            String review_text = jsonObject1.getString("review_text");
                            Toast.makeText(getContext(), "Successfully review updated", Toast.LENGTH_SHORT).show();
                            Log.i("rating", "rating" + response);
                        }
                    }

                    String yourString = jsonObject.getString("total_rating"); // TODO read from DB
                    float rating = Float.parseFloat(yourString);
                    ratingBar.setRating(rating);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("token", "c0304a62dd289bdc7364fb974c2091f6");
                map.put("service_id", prefManager.getServiceId());
                map.put("user_id", prefManager.getUserId());
                map.put("rating", rate);
                map.put("rating_message", commentEt);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
