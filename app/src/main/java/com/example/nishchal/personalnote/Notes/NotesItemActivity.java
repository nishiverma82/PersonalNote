package com.example.nishchal.personalnote.Notes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.NotesData;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotesItemActivity extends AppCompatActivity {

    private EditText edit_title, edit_content;
    private TextView datetime;
    private String timeStamp;
    private Context context = this;
    private NotesData notesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_item_activity);

        edit_title = findViewById(R.id.edit_title);
        edit_title.setHorizontallyScrolling(false);

        edit_content = findViewById(R.id.edit_content);
        edit_content.setHorizontallyScrolling(false);

        datetime = findViewById(R.id.datetime_textview);

        timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        datetime.setText("Edited " + timeStamp.substring(0,11));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addNotes();
    }

    private void addNotes() {
        String title = edit_title.getText().toString();
        String content = edit_content.getText().toString();
        if(title.matches("") && content.matches("")){

            //empty note... not created

        } else {

            notesData = new NotesData(context);

            Boolean check = notesData.insertData(title, content, timeStamp);
            if (check){//note saved
                    }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            notesData.close();
            
        }
    }

}
