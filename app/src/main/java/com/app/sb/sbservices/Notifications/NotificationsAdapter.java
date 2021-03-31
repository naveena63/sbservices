package com.app.sb.sbservices.Notifications;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sb.sbservices.R;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{

    private Context context;

    private List<NotificationsModel> notificationsModel;

    ViewGroup viewGroup;
    public NotificationsAdapter(List<NotificationsModel> notificationsModel, NotificationsActicity notificationsActicity) {
        this.notificationsModel = notificationsModel;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.notification_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int  i) {


        viewHolder.notification_name.setText(notificationsModel.get(i).getNotification());


    }

    @Override
    public int getItemCount() {

        return notificationsModel.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

      TextView notification_name;

        ViewHolder(View itemView) {
            super(itemView);
            notification_name=itemView.findViewById(R.id.textNotification);
        }
    }



}
