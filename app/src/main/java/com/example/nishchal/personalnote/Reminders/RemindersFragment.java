package com.example.nishchal.personalnote.Reminders;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.nishchal.personalnote.Databases.RemindersData;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;


public class RemindersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private FloatingActionButton fab;
    private LinearLayout emptyLayout;
    private RemindersCustomAdapter remindersCustomAdapter;
    private ArrayList<HashMap<String, String>> dataDB;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminders, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.reminder_recyclerview);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        emptyLayout = (LinearLayout)rootView.findViewById(R.id.empty_layout);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        dataDB = new ArrayList<>();

        swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        readDatabase();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReminderAddItem.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void readDatabase() {

        dataDB = new RemindersData(getContext()).readData();

        if(dataDB.size() != 0){

            remindersCustomAdapter = new RemindersCustomAdapter(getContext(), dataDB);
            recyclerView.setAdapter(remindersCustomAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.INVISIBLE);
        }
        else{

            recyclerView.setVisibility(View.INVISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        readDatabase();
    }

    @Override
    public void onRefresh() {
        //My Budget
        RemindersFragment budgetFragment = new RemindersFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.reminder_fragment_layout,budgetFragment);
        fragmentTransaction.commit();
        swipeLayout.setRefreshing(false);
    }

}
