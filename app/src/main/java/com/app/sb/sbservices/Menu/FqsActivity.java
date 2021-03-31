package com.app.sb.sbservices.Menu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.app.sb.sbservices.services.ServicesListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FqsActivity extends AppCompatActivity {

    ArrayList<ServicesListModel> servicesListModels;
    ArrayList<QuestionAndAnswerModel> questionAndAnswerModelArrayList;
    RecyclerView my_recycler_view;
    RequestQueue requestQueue;
    FaqsAdapter faqsAdapter;
    QuesAnsAdapter quesAnsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fqs);
        requestQueue = Volley.newRequestQueue(this);
        my_recycler_view = findViewById(R.id.recycelrview);

        servicesListModels = new ArrayList<>();
        questionAndAnswerModelArrayList = new ArrayList<>();
        my_recycler_view.setLayoutManager(new LinearLayoutManager(FqsActivity.this));
        my_recycler_view.setHasFixedSize(true);

        getFaqs();
    }

    private void getFaqs() {

        String url_formation = "https://www.sbservices.in/api/Services/allservices";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_formation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Faqresponse", "response" + response);
                questionAndAnswerModelArrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("services");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject json = jsonArray.getJSONObject(i);
                            ServicesListModel servicesListModel = new ServicesListModel();
                            String namecatrgry = json.getString("category_name");
                            String faq = json.getString("faq");
                            Log.e("faq", "faq" + namecatrgry + "  " + faq);




                            JSONArray jsonArray1 = json.getJSONArray("faq");
                            Log.e("faq112", String.valueOf(jsonArray1));
                            if (jsonArray1.length()==0)
                            {
                                Toast.makeText(FqsActivity.this, "no", Toast.LENGTH_SHORT).show();
                                servicesListModel.setNo_packages_available("NoFaqs");
                                Log.e("faq112", "nofaq");

                            }else
                            {

                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                    String question = jsonObject2.getString("question");
                                    String answer = jsonObject2.getString("answer");

                                    Log.e("faq111", "faq" + question + "  " + answer);
                                    questionAndAnswerModelArrayList.add(new QuestionAndAnswerModel(question, answer));

                                }
                                servicesListModel.setServiceName(namecatrgry);

                                servicesListModel.setAllItemsInSection(questionAndAnswerModelArrayList);
                                servicesListModels.add(servicesListModel);
                                faqsAdapter = new FaqsAdapter(servicesListModels);
                                quesAnsAdapter = new QuesAnsAdapter(FqsActivity.this, questionAndAnswerModelArrayList);
                                my_recycler_view.setAdapter(faqsAdapter);
                                faqsAdapter.notifyDataSetChanged();
                            }
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(FqsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
