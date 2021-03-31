package com.app.sb.sbservices.Packages;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.app.sb.sbservices.Cart.CartAdapter;
import com.app.sb.sbservices.DescriptionActivity.DescrptionMainActivity;
import com.app.sb.sbservices.DescriptionActivity.TabwithViewpager;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.sb.sbservices.Utils.GlobalVariable;
import com.app.sb.sbservices.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> implements CartAdapter.OnItemClickListener {

    private Context context;
    private PackageListener packageListener;
    private List<PackageModel> packageModels;
    LinearLayout linearlayout;
    Button descrption;
    PrefManager prefManager;
    ViewGroup viewGroup;

    public PackageAdapter(List<PackageModel> packageModel, PackageListener packageListener) {
        this.packageModels = packageModel;
        this.packageListener = packageListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.custom_packages_layout, viewGroup, false);
        prefManager=new PrefManager(context);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        String imgPath = packageModels.get(i).getPackageImage();

        viewHolder.pack_imag.getLayoutParams().height = GlobalVariable.deviceHeight / 10;
        viewHolder.pack_imag.getLayoutParams().width = GlobalVariable.deviceWidth / 5;
        if (imgPath != null && !imgPath.equals("")) {
            Glide.with(context).load("https://sbservices.in/" + imgPath).placeholder(R.drawable.icon_placeholder).error(R.drawable.icon_placeholder).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.pack_imag);

            Log.e("horizontalimage", "https://sbservices.in/" + imgPath);
        }
        viewHolder.pack_name_tv.setText(packageModels.get(i).getPackage_name());

        viewHolder.package_slug.setText(packageModels.get(i).getService_slug());
        viewHolder.price_tv.setText("Rs." + packageModels.get(i).getPackage_price());


        if (packageModels.get(i).getSub_package_status().equalsIgnoreCase("" + "0")) {
            viewHolder.addbtn.setVisibility(View.VISIBLE);
        } else {
            viewHolder.addbtn.setVisibility(View.VISIBLE);
        }
        viewHolder.itemView.setOnClickListener(view -> {
            if (packageModels.get(i).getSub_package_status().equalsIgnoreCase("0")) {
                if (packageListener != null) {

                }
            }
        });
        viewHolder.addbtn.setOnClickListener(view -> {
            if (packageListener != null) {
                packageListener.onClickAddToCart(packageModels.get(i));
            }
        });

        descrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescrptionMainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return packageModels.size();
    }

    @Override
    public void OnClick(View view, int position, String userID, String Cart_id) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button addbtn;
        TextView pack_name_tv, package_slug, price_tv, inclusion, exculsion,avgRating;
        ImageView pack_imag;

        ViewHolder(View itemView) {
            super(itemView);
            addbtn = itemView.findViewById(R.id.btn);
            pack_name_tv = itemView.findViewById(R.id.pack_name_tv);
            package_slug = itemView.findViewById(R.id.package_slug);
            avgRating = itemView.findViewById(R.id.avgRating);
            price_tv = itemView.findViewById(R.id.price_tv);
            linearlayout = itemView.findViewById(R.id.linearlayout);
            pack_imag = itemView.findViewById(R.id.image_view);
            descrption = itemView.findViewById(R.id.description);




                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.RATING_SERVICE_REVIEW,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                String status;
                                JSONObject jsonMainObject;
                                try {
                                    jsonMainObject = new JSONObject(s);
                                    status = jsonMainObject.getString("status");
                                    if (status.equalsIgnoreCase("1")) {
                                        JSONArray listArray = jsonMainObject.getJSONArray("reviews");
                                        JSONObject memberObject;
                                        for (int i = 0; i < listArray.length(); i++) {
                                            Log.i("rtaing service response","reposne"+s);
                                            memberObject = listArray.getJSONObject(i);
                                        }
                                        avgRating.setText(jsonMainObject.getString("total_rating")+"\u2605");
                                        avgRating.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(context, TabwithViewpager.class);
                                                context.startActivity(intent);
                                            }
                                        });


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
                                    Toast.makeText(context, "Bad internet connection please try again", Toast.LENGTH_SHORT).show();
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

                RequestQueue requestQueue = Volley.newRequestQueue(context);
// stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(stringRequest);
            }

    }
}
