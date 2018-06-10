package com.example.nishchal.personalnote.Notes;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.ConversionStringArray;
import com.example.nishchal.personalnote.Databases.NotesData;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NotesListActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton fab;
    private EditText list_item_add_edittext, edit_title;
    private Button list_item_add_btn;
    private TextView datetime;
    private String timeStamp;
    private NotesListCustomAdapter mAdapter;
    private ConversionStringArray cStringArray = new ConversionStringArray();
    private NotesData notesData;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list_activity);

        listView = findViewById(R.id.listview);
        edit_title = findViewById(R.id.edit_title);
        datetime = findViewById(R.id.datetime_textview);

        timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        datetime.setText("Edited " + timeStamp.substring(0,11));

        mAdapter = new NotesListCustomAdapter(this);
        listView.setAdapter(mAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(NotesListActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.dialog_notes_list_item_add);
                dialog.show();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                Double width = displayMetrics.widthPixels*0.9;
                dialog.getWindow().setLayout(width.intValue(), WindowManager.LayoutParams.WRAP_CONTENT);

                list_item_add_edittext = dialog.findViewById(R.id.list_item_add_edittext);
                list_item_add_btn = dialog.findViewById(R.id.list_item_add_btn);

                list_item_add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String li = list_item_add_edittext.getText().toString();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("item", li);
                        map.put("value", "0");
                        ListItemsClass.list.add(map);
                        //listItems.add(map);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addNotes();
    }

    public void addNotes() {

        String title = edit_title.getText().toString();
        String listString = cStringArray.convertArrayToString(ListItemsClass.list);

        if(listString.matches("") && title.matches("")){

            //Empty note, not created...

        } else{

            notesData = new NotesData(context);

            Boolean check = notesData.insertData(title, listString, timeStamp);
            if(check) {//notes saved
                    }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            notesData.close();

        }
        ListItemsClass.list.clear();
    }

}
