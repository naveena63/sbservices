package com.app.sb.sbservices.OfferBanners;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.PrefManager;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {


    private Context context;
    private List<OfferBanners> offerBannerList;
    PrefManager prefManager;

    public OffersAdapter(Context context, List<OfferBanners> offerBannerList) {
        this.context = context;
        this.offerBannerList = offerBannerList;
    }

    @NonNull
    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.offers_banner, viewGroup, false);
        prefManager = new PrefManager(context);
        return new OffersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String imgPath = offerBannerList.get(position).getBannerImage();
   /*     holder.serviceImg.getLayoutParams().height = GlobalVariable.deviceHeight / 5;
        holder.serviceImg.getLayoutParams().width = GlobalVariable.deviceWidth / 5;*/
        if (imgPath != null && !imgPath.equals("")) {
            Glide.with(context).load("https://sbservices.in/" + imgPath)
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_placeholder)
                    .crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.serviceImg);

            Log.e("horizontalimage", "https://sbservices.in/" + imgPath);
        }
        //holder.serviceNameTv.setText(offerBannerList.get(position).getImageName());
        holder.itemView.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {

        return offerBannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView serviceImg;

        ViewHolder(View itemView) {
            super(itemView);
            serviceImg = itemView.findViewById(R.id.image_view);
        }
    }
}
