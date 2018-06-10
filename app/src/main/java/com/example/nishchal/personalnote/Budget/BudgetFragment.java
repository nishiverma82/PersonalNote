package com.example.nishchal.personalnote.Budget;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.BudgetData;
import com.example.nishchal.personalnote.EncryptDecryptClass;
import com.example.nishchal.personalnote.PassStore.PassStoreFragment;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BudgetFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private BudgetCustomAdapter budgetCustomAdapter;
    private LinearLayout emptyLayout;
    private FloatingActionButton fab;
    private ArrayList<HashMap<String, String>> dataDB;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_budget, container, false);

        recyclerView = rootView.findViewById(R.id.budget_recycler_view);
        emptyLayout = rootView.findViewById(R.id.empty_layout);

        dataDB = new ArrayList<>();

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        swipeLayout = rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        readDatabase();

        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),BudgetAddEntry.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void readDatabase(){

        dataDB = new BudgetData(getContext()).readByDate();

        if(dataDB.size() != 0){

            budgetCustomAdapter = new BudgetCustomAdapter(getContext(), dataDB);
            recyclerView.setAdapter(budgetCustomAdapter);
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
        BudgetFragment budgetFragment = new BudgetFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.budget_frame_layout,budgetFragment);
        fragmentTransaction.commit();
        swipeLayout.setRefreshing(false);
    }

}
