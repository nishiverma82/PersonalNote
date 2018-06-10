package com.example.nishchal.personalnote.Budget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.BudgetData;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BudgetDateEntryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private TextView entry_date;
    private String date;
    private int swidth, sheight, rotation;
    private ArrayList<HashMap<String, String>> dataDB;
    private Context context = this;
    private BudgetDateEntryCustomAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_date_entry_activity);
        date = getIntent().getExtras().getString("Date");

        recyclerView = findViewById(R.id.recycler_view);
        entry_date = findViewById(R.id.entry_date);

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        dataDB = new ArrayList<>();

        entry_date.setText(date);

        dataDB = new BudgetData(context).readByTime(date);

        for(int i=0;i<dataDB.size();i++)System.out.println(dataDB.get(i));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        sheight = displayMetrics.heightPixels;
        swidth = displayMetrics.widthPixels;
        rotation = getWindowManager().getDefaultDisplay().getRotation();

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        adapter = new BudgetDateEntryCustomAdapter(context, dataDB, date, swidth,sheight, rotation);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onRefresh() {
        dataDB = new BudgetData(context).readByTime(date);
        adapter = new BudgetDateEntryCustomAdapter(context, dataDB, date, swidth, sheight, rotation);
        recyclerView.setAdapter(adapter);
        swipeLayout.setRefreshing(false);
    }
}
