package com.app.sb.sbservices.DescriptionActivity;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GiveratingFragment extends Fragment {

View view;
    EditText etComment;
    RatingBar ratingBar;
RequestQueue requestQueue;
PrefManager prefManager;
    Button submitButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view= inflater.inflate(R.layout.fragment_giverating, container, false);
        ratingBar = view.findViewById(R.id.ratingBar);

        etComment = view.findViewById(R.id.comment);
        requestQueue= Volley.newRequestQueue(getContext());
        submitButton = view.findViewById(R.id.submitButton);
        prefManager=new PrefManager(getContext());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    ratingreview();
                }
            }
        });
  return  view;
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
    public  void  ratingreview(){
        String commentEt = etComment.getText().toString();
       int rate= (int) ratingBar.getRating();



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
                map.put("rating", String.valueOf(rate));
                map.put("rating_message", commentEt);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
