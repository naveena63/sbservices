package com.app.sb.sbservices.DescriptionActivity;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.PrefManager;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private Context context;
PrefManager prefManager;
    private List<RatingModel> ratingModelList;

    ViewGroup viewGroup;

    public RatingAdapter(List<RatingModel> ratingModelList, FragmentActivity activity) {
        this.ratingModelList = ratingModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rating_layout, viewGroup, false);
        prefManager=new PrefManager(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        try {
            viewHolder.ratingdescrption.setText(ratingModelList.get(i).getDescrption());
            viewHolder.rating.setRating(Float.parseFloat(ratingModelList.get(i).getRating()));
viewHolder.userName.setText(ratingModelList.get(i).getUsenamree());
        } catch (NumberFormatException e) {
            Log.e("NumberFormatException", e.getMessage().toString());
        }
    }

    @Override
    public int getItemCount() {

        return ratingModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ratingdescrption,userName;
        RatingBar rating;

        ViewHolder(View itemView) {
            super(itemView);
            ratingdescrption = itemView.findViewById(R.id.ratingDescrption);
            userName = itemView.findViewById(R.id.userName);
            rating = itemView.findViewById(R.id.ratingBar);
        }
    }


}
