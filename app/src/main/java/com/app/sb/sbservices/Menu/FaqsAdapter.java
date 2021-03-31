package com.app.sb.sbservices.Menu;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.PrefManager;
import com.app.sb.sbservices.services.HomeScreenListener;
import com.app.sb.sbservices.services.ServicesListModel;
import java.util.ArrayList;
import java.util.List;

public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.ViewHolder> {


    private Context context;
    private HomeScreenListener homeScreenListener;
    private List<ServicesListModel> servicesListModel;
    PrefManager prefManager;

    public FaqsAdapter(List<ServicesListModel> servicesList) {
        this.servicesListModel = servicesList;

    }

    @NonNull
    @Override
    public FaqsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.faq_item,viewGroup,false);

        prefManager = new PrefManager(context);
        return  new FaqsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
             holder.textServiceNAme.setText(servicesListModel.get(position).getServiceName());
             holder.nofaqs.setText(servicesListModel.get(position).getNo_packages_available());

        final ArrayList singleSectionItems = servicesListModel.get(position).getAllItemsInSection();

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setNestedScrollingEnabled(false);

        holder.recyclerView.setFocusable(false);
        QuesAnsAdapter itemListDataAdapter = new QuesAnsAdapter(context, singleSectionItems);
        holder.recyclerView.setAdapter(itemListDataAdapter);
    }
    @Override
    public int getItemCount() {
        return servicesListModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
     RecyclerView recyclerView;
        TextView textServiceNAme,nofaqs;

        ViewHolder(View itemView) {
            super(itemView);

          recyclerView=itemView.findViewById(R.id.recyclerView);
            textServiceNAme = itemView.findViewById(R.id.textviewserviceName);
            nofaqs = itemView.findViewById(R.id.no_packages_available);
        }
    }
}
