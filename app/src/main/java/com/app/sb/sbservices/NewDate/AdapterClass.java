package com.app.sb.sbservices.NewDate;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.app.sb.sbservices.R;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.VH> {
    Context context;
    List<ModelClass> modelClassList;
    List<DateModel> dateModelList;
    private OnDateClickListener onDateClickListener;
    int row_index;
    public AdapterClass(Context context, List<ModelClass> modelClassList, List<DateModel> dateModelList) {
        this.context = context;
        this.modelClassList = modelClassList;
        this.dateModelList = dateModelList;

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.item_day,
                        viewGroup,
                        false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {

        holder.tvdate.setText(modelClassList.get(position).getCalendar_date());
        holder.tvWeekDay.setText(modelClassList.get(position).getDay());

        if (modelClassList.get(position).getStatus().equals("blocked")) {

            holder.tvdate.setBackgroundColor(Color.RED);

        }else if (modelClassList.get(position).getStatus().equals("active"))
        {
            holder.tvdate.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                row_index=position;
                notifyDataSetChanged();
                if (onDateClickListener != null)
                    onDateClickListener.onDateClickListener(dateModelList.get(position),modelClassList.get(position));
            }
        });

        holder.tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
                if (onDateClickListener != null)
                    onDateClickListener.onDateClickListener(dateModelList.get(position),modelClassList.get(position));
            }
        });

        if(row_index==position){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#567845"));

        }
        else
        {
            holder.linearLayout.setBackgroundColor(Color.TRANSPARENT);

        }
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {

        this.onDateClickListener = onDateClickListener;
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvdate, tvWeekDay;
        LinearLayout linearLayout;
        public VH(View itemView) {
            super(itemView);
            tvdate = (TextView) itemView.findViewById(R.id.tvdate);
            tvWeekDay = (TextView) itemView.findViewById(R.id.tvWeekDay);
            linearLayout=itemView.findViewById(R.id.linearLayout);
        }
    }

    public interface OnDateClickListener {
        void onDateClickListener(DateModel dateModel,ModelClass modelClass);
    }
}
