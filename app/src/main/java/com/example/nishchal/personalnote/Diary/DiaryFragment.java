package com.example.nishchal.personalnote.Diary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.DiaryData;
import com.example.nishchal.personalnote.PinLockActivity;
import com.example.nishchal.personalnote.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.HashMap;

public class DiaryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeLayout;
    private DiscreteScrollView scrollView;
    private DiaryCustomAdapter diaryCustomAdapter;
    private FloatingActionButton fab;
    private TextView onecarddate, onecardtitle, onecardcontent;
    private Context context = getContext();
    private LinearLayout emptyLayout;
    private ArrayList<HashMap<String, String>> dataDB;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        scrollView = rootView.findViewById(R.id.scrollcardview);
        emptyLayout = rootView.findViewById(R.id.empty_layout);

        onecarddate = rootView.findViewById(R.id.diary_date);
        onecardtitle = rootView.findViewById(R.id.diary_title);
        onecardcontent = rootView.findViewById(R.id.diary_content);

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
                Intent intent = new Intent(getActivity(), DiaryAddItem.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void readDatabase() {

        dataDB = new DiaryData(getContext()).readData();

        if(dataDB.size() != 0){

            diaryCustomAdapter = new DiaryCustomAdapter(dataDB, getContext());
            scrollView.setAdapter(diaryCustomAdapter);
            scrollView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.INVISIBLE);
        }
        else{

            scrollView.setVisibility(View.INVISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {
        //My Diary
        DiaryFragment diaryFragment = new DiaryFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,diaryFragment);
        fragmentTransaction.commit();
        swipeLayout.setRefreshing(false);
    }
}