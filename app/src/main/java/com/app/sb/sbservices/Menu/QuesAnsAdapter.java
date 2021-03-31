package com.app.sb.sbservices.Menu;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sb.sbservices.R;

import java.util.ArrayList;

public class QuesAnsAdapter extends RecyclerView.Adapter<QuesAnsAdapter.SingleItemRowHolder> {

    private ArrayList<QuestionAndAnswerModel> itemsList;
    private Context mContext;


    public QuesAnsAdapter(Context context, ArrayList<QuestionAndAnswerModel> itemsList) {

        this.mContext = context;
        this.itemsList = itemsList;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.quesans_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        QuestionAndAnswerModel singleItem = itemsList.get(i);
        holder.question.setText(itemsList.get(i).getQuestion());
        holder.answer.setText(itemsList.get(i).getAnswer());


    }

    @Override
    public int getItemCount() {
        return (itemsList.size());
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        TextView question, answer, no_packages_available;

        public SingleItemRowHolder(View view) {
            super(view);
            question = view.findViewById(R.id.question);
            answer = view.findViewById(R.id.answer);
            no_packages_available = view.findViewById(R.id.no_packages_available);


        }
    }
}