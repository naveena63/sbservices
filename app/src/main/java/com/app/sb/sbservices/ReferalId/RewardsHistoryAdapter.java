package com.app.sb.sbservices.ReferalId;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.sb.sbservices.R;
import java.util.List;

public class RewardsHistoryAdapter extends RecyclerView.Adapter<RewardsHistoryAdapter.VH> {
    Context context;
    List<RewardsHistoryModel> rewardsHistoryModels;

    public RewardsHistoryAdapter(Context context, List<RewardsHistoryModel> rewardsHistoryModels)
    {
        this.context = context;
        this.rewardsHistoryModels = rewardsHistoryModels;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.rewards_history_layout,
                        viewGroup,
                        false);
        return new VH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Log.v("DDDD","");
        holder.textOrderId.setText("Order id:"+rewardsHistoryModels.get(position).getOrder_id());
        holder.textUSedRewrdpoints.setText("Used RewardPoints:"+rewardsHistoryModels.get(position).getUsed_reward_points());
        holder.texViewDate.setText("date :"+rewardsHistoryModels.get(position).getDate());
    }
    @Override
    public int getItemCount() {
        return rewardsHistoryModels.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView textOrderId,textUSedRewrdpoints,texViewDate;

        public VH( View itemView) {
            super(itemView);
            textUSedRewrdpoints=itemView.findViewById(R.id.textView_usedRewardpoints);
            textOrderId=itemView.findViewById(R.id.textView_orderId);
            texViewDate=itemView.findViewById(R.id.date);
        }
    }
}