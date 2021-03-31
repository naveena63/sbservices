package com.app.sb.sbservices.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sb.sbservices.Packages.PackagesActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.GlobalVariable;
import com.app.sb.sbservices.Utils.PrefManager;

import java.util.List;

public class ServiceAdpter extends RecyclerView.Adapter<ServiceAdpter.ViewHolder> {


    private Context context;
    private HomeScreenListener homeScreenListener;
    private List<ServicesListModel> servicesListModel;
    PrefManager prefManager;

    public ServiceAdpter(List<ServicesListModel> servicesList,HomeScreenListener homeScreenListener) {
        this.servicesListModel = servicesList;
        this.homeScreenListener = homeScreenListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.custom_serivce_layout,viewGroup,false);

        prefManager = new PrefManager(context);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String imgPath = servicesListModel.get(position).getServiceImage();
        holder.serviceImg.getLayoutParams().height = GlobalVariable.deviceHeight / 10;
        holder.serviceImg.getLayoutParams().width = GlobalVariable.deviceWidth / 10;
        if (imgPath != null && !imgPath.equals("")) {
            Glide.with(context).load("https://sbservices.in/" + imgPath).placeholder(R.drawable.icon_placeholder).error(R.drawable.icon_placeholder).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.serviceImg);
            Log.e("imgPath", "https://sbservices.in/" + imgPath);
        }
        holder.serviceNameTv.setText(servicesListModel.get(position).getServiceName());
       holder.itemView.setOnClickListener(view->{

               Intent intent = new Intent(context,PackagesActivity.class);
               context.startActivity(intent);
               Log.e("ServiceId","hhdhdhdh"+servicesListModel.get(position).getServiceId());
               prefManager.setServiceId(servicesListModel.get(position).getServiceId());
              // homeScreenListener.onClickGridItem(servicesListModel.get(position).getServiceId(),servicesListModel.get(position).getServiceName());

       });
    }

    @Override
    public int getItemCount() {

        return servicesListModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

 ImageView serviceImg;
 TextView serviceNameTv;
     ViewHolder(View itemView) {
         super(itemView);
         serviceImg = itemView.findViewById(R.id.gridview_image);
         serviceNameTv = itemView.findViewById(R.id.gridview_text);
     }
 }
}
