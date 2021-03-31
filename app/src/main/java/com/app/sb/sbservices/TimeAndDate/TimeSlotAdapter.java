package com.app.sb.sbservices.TimeAndDate;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sb.sbservices.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private Context context;

    private List<TimeSlotModel> timeModelList;
    private TImeSlotClickListener tImeSlotClickListener;
    public TimeSlotAdapter(List<TimeSlotModel> timeModels,TImeSlotClickListener timeSlotClickListener) {
        this.timeModelList = timeModels;
        this.tImeSlotClickListener = timeSlotClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.time_slot_list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        Log.v("F",""+currentDateandTime);


        if (timeModelList.get(position).getStatus().equals("active")) {
            holder.timeSlotTextView.setText(timeModelList.get(position).getTimeSlotName() + "\t" +
                    timeModelList.get(position).getStatus());
        }
        else {

            holder.timeSlotTextView.setVisibility(View.GONE);
            holder.time_slot_textview_block.setVisibility(View.VISIBLE);

            holder.time_slot_textview_block.setText(timeModelList.get(position).getTimeSlotName() + "\t" +
                    timeModelList.get(position).getStatus());
        }
        if(timeModelList.get(position).isSelected()){
            holder.time_slot_textview_block.setBackground(context.getResources().getDrawable(R.drawable.time_slot_bg));
            holder.timeSlotTextView.setBackground(context.getResources().getDrawable(R.drawable.time_slot_bg));
            holder.timeSlotTextView.setTextColor(context.getResources().getColor(R.color.button_green));
        }else{
            holder.time_slot_textview_block.setBackground(context.getResources().getDrawable(R.drawable.time_slot_bg));
            holder.timeSlotTextView.setBackground(context.getResources().getDrawable(R.drawable.time_slot_bg));
            holder.timeSlotTextView.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tImeSlotClickListener!=null){
                    tImeSlotClickListener.onClickTimeSlot(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeSlotTextView,time_slot_textview_block;


        ViewHolder(View itemView) {
            super(itemView);
            timeSlotTextView = itemView.findViewById(R.id.time_slot_textview);
            time_slot_textview_block= itemView.findViewById(R.id.time_slot_textview_block);

        }

    }
}

