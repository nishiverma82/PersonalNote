package com.example.nishchal.personalnote.Notes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.NotesData;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NotesCardClickActivity extends AppCompatActivity {

    private EditText edit_title, edit_content;
    private TextView datetime;
    private String value;
    private String timeStamp;
    private int i;
    private Context context = this;
    private NotesData notesData;
    private ArrayList<HashMap<String, String>> data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_item_activity);
        value = getIntent().getExtras().getString("ItemPosition");

        edit_title = findViewById(R.id.edit_title);
        edit_title.setHorizontallyScrolling(false);

        edit_content = findViewById(R.id.edit_content);
        edit_content.setHorizontallyScrolling(false);

        datetime = findViewById(R.id.datetime_textview);

        i = Integer.parseInt(value);
        data = new NotesData(this).readData();

        edit_title.setText(data.get(i).get("0"));
        edit_content.setText(data.get(i).get("1"));
        timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        datetime.setText("Edited " + data.get(i).get("2").substring(0,11));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateNotes();
    }

    private void updateNotes() {
        String title = edit_title.getText().toString();
        String content = edit_content.getText().toString();
        if(title.matches("") && content.matches("")){

            //Empty note, not created...

        } else{

            notesData = new NotesData(context);

            Boolean check = notesData.updateItem(title, content, timeStamp, data.get(i).get("2"));
            if(check) {//notes saved
                    }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            notesData.close();

        }
    }

}
