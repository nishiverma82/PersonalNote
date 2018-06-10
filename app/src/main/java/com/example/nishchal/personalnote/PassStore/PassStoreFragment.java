package com.example.nishchal.personalnote.PassStore;

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

import com.example.nishchal.personalnote.Databases.PassStoreData;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PassStoreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private PassStoreCustomAdapter adapter;
    private FloatingActionButton fab;
    private LinearLayout emptyLayout;
    private ArrayList<HashMap<String, String>> dataDB;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pass_store, container, false);

        emptyLayout = rootView.findViewById(R.id.empty_layout);
        fab = rootView.findViewById(R.id.fab);
        recyclerView = rootView.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        swipeLayout = rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        readDatabase();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager); // Vertical Orientation By Default

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PassStoreAddItemActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


    private void readDatabase() {

        dataDB = new PassStoreData(getContext()).readData();

        if(dataDB.size() != 0){
            adapter = new PassStoreCustomAdapter(getContext(), dataDB);
            recyclerView.setAdapter(adapter);
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
        //Pass Store
        PassStoreFragment passStoreFragment = new PassStoreFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,passStoreFragment);
        fragmentTransaction.commit();
        swipeLayout.setRefreshing(false);
    }
}
