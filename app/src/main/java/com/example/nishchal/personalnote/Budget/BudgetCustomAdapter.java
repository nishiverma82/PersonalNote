package com.example.nishchal.personalnote.Budget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BudgetCustomAdapter extends RecyclerView.Adapter<BudgetCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater;

    private int previousPosition = 0;

    public BudgetCustomAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.budget_card_view, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {

        myViewHolder.date.setText(data.get(position).get("0"));
        myViewHolder.entry.setText(data.get(position).get("1"));
        myViewHolder.amount.setText(data.get(position).get("2"));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cposition = position;
                String date = data.get(cposition).get("0");
                Intent intent = new Intent(context, BudgetDateEntryActivity.class);
                intent.putExtra("Date", date);
                context.startActivity(intent);
            }
        });

        previousPosition = position;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView date, entry, amount;

        private final Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            date = itemView.findViewById(R.id.budget_card_date);
            entry = itemView.findViewById(R.id.budget_card_entries);
            amount = itemView.findViewById(R.id.budget_card_amount);

        }
    }
}
