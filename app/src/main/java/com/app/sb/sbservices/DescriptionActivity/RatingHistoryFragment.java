package com.app.sb.sbservices.DescriptionActivity;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    List<RatingModel> ratingModelList;
    RatingAdapter ratingAdapter;
    TextView no_packages_available,averageRating;
    PrefManager prefManager;
    RatingBar ratingBar;
    public RatingHistoryFragment() {
        // Required empty public constructor
    }
View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_rating_history, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        averageRating=view.findViewById(R.id.averageRating);
        prefManager=new PrefManager(getContext());
        ratingBar = view.findViewById(R.id.ratingBar);
no_packages_available=view.findViewById(R.id.no_packages_available);
        ratingModelList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rating_user_review();
  return  view;
    }
    private void rating_user_review() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.RATING_SERVICE_REVIEW,
                new Response.Listener<String>() {

                    //open this url in post man

                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog

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
                                    model.setUsenamree((memberObject.getString("name")));
                                    ratingModelList.add(model);

                                }
                                ratingBar.setRating(Float.parseFloat(String.valueOf((jsonMainObject.getString("total_rating")))));
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

}
