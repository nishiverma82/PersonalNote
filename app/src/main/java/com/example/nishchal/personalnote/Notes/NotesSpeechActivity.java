package com.example.nishchal.personalnote.Notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import java.util.Locale;

public class NotesSpeechActivity extends AppCompatActivity {

    private EditText edit_title, edit_content;
    private TextView datetime;
    private String timeStamp;
    private Context context = this;
    private NotesData notesData;

    @Override
    protected void onStart() {
        super.onStart();
        getSpeechInput();
    }

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

            //Empty note, not created...

        } else{

            notesData = new NotesData(context);

            Boolean check = notesData.insertData(title, content, timeStamp);
            if(check) {//Notes saved
                    }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            notesData.close();

        }
    }

    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(NotesSpeechActivity.this,"Your device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 10:
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String content = edit_content.getText().toString() + result.get(0);
                    edit_content.setText(content);
                }
                break;
        }
    }

}
