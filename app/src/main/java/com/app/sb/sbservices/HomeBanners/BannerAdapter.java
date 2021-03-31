package com.app.sb.sbservices.HomeBanners;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.AppConstants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private List<Banner> images;
    private LayoutInflater inflater;
    private Context context;


    public BannerAdapter(Context context, List<Banner> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final Banner banner = images.get(position);
        View myImageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.slidin_image, view, false);
        final ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);

        Picasso.with(context)
                .load(AppConstants.PICASSO_BASE_URL + banner.getBannerImage())
                .placeholder(R.drawable.sb_services_logo)
                .error(R.drawable.error_black)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(myImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("image_slide", "image_slide " + AppConstants.PICASSO_BASE_URL + banner.getBannerImage());
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(AppConstants.PICASSO_BASE_URL + banner.getBannerImage())
                                .error(R.drawable.error_black)
                                .into(myImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e("image_s", AppConstants.PICASSO_BASE_URL + banner.getBannerImage());
                                    }

                                    @Override
                                    public void onError() {
                                        Log.e("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
