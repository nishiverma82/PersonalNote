package com.example.nishchal.personalnote.Notes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.NotesData;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private NotesCustomAdapter adapter;
    private Boolean layoutstate = false;
    private ImageButton layout_button,speech_button,camera_button, list_button;
    private Button takenote_button;

    private LinearLayout emptyLayout;
    private ArrayList<HashMap<String, String>> dataDB;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = rootView.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        layout_button = rootView.findViewById(R.id.layout_button);
        speech_button = rootView.findViewById(R.id.speech_button);
        camera_button = rootView.findViewById(R.id.camera_button);
        list_button = rootView.findViewById(R.id.list_button);
        takenote_button = rootView.findViewById(R.id.takenote_button);

        emptyLayout = rootView.findViewById(R.id.empty_layout);

        swipeLayout = rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        readDatabase();

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)); // Vertical Orientation By Default
        layoutstate = true;

        layout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout();
            }
        });
        speech_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput();
            }
        });
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCameraInput();
            }
        });
        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListActivity();
            }
        });
        takenote_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNotesActivity();
            }
        });

        return rootView;
    }

    private void setLayout() {

        if (layoutstate){
            layout_button.setImageResource(R.drawable.linear_layout);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity()); // (Context context)
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            layoutstate = !layoutstate;
        }  else {
            layout_button.setImageResource(R.drawable.staggered_layout);
            StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
            recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
            layoutstate = !layoutstate;
        }

    }

    private void getSpeechInput() {
            Intent intent = new Intent(getActivity(),NotesSpeechActivity.class);
            startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 20:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Toast.makeText(getActivity(), "Image stored", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void getCameraInput() {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 20);
    }

    private void getNotesActivity() {
        Intent intent = new Intent(getActivity(),NotesItemActivity.class);
        startActivity(intent);
    }

    private void getListActivity() {
        Intent intent = new Intent(getActivity(),NotesListActivity.class);
        startActivity(intent);
    }

    private void readDatabase() {

         dataDB = new NotesData(getContext()).readData();

        if(dataDB.size() != 0){

            adapter = new NotesCustomAdapter(getContext(), dataDB);
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

        dataDB = new NotesData(getContext()).readData();
        if(dataDB.size() != 0){
            adapter = new NotesCustomAdapter(getContext(), dataDB);
            if(layoutstate) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            } else{
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity());
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
            }
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.INVISIBLE);
        } else{
            recyclerView.setVisibility(View.INVISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        //notes fragment
        NotesFragment notesFragment = new NotesFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,notesFragment);
        fragmentTransaction.commit();
        swipeLayout.setRefreshing(false);
    }
}
