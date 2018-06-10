package com.example.nishchal.personalnote.Diary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.DiaryData;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiaryAddItem extends AppCompatActivity {

    private EditText diary_date, diary_topic;
    private LineEditText diary_content;
    private View layout;
    private String timeStamp;
    private Context context = this;
    private DiaryData diaryData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_add_item);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formatter.format(date);
        Toast.makeText(context, strDate, Toast.LENGTH_SHORT).show();

        diary_date = findViewById(R.id.diary_date);
        diary_topic = findViewById(R.id.diary_topic);
        diary_content =  findViewById(R.id.diary_content);

        diary_date.setText(strDate);
        diary_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalenderPopUp();
            }
        });

        timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addNotes();
    }

    private void showCalenderPopUp() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calender_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        Button ok = dialog.findViewById(R.id.ok);
        Button cancel = dialog.findViewById(R.id.cancel_btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                month = month+1;
                int year = datePicker.getYear();
                if(month>=0 && month <=9)
                    diary_date.setText(day + "-0" + (month) + "-" + year);
                else
                    diary_date.setText(day + "-" + (month) + "-" + year);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void addNotes() {
        String date = diary_date.getText().toString();
        String topic = diary_topic.getText().toString();
        String content = diary_content.getText().toString();
        if(topic.matches("") && content.matches("")){

            //Empty note, not created...

        } else{

            diaryData = new DiaryData(context);

            Boolean check = diaryData.insertData(date, topic, content, timeStamp);
            if(check) {//data saved
                    }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            diaryData.close();

        }
    }
}
