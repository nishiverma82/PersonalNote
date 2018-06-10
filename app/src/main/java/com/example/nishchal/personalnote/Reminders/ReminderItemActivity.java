package com.example.nishchal.personalnote.Reminders;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.RemindersData;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ReminderItemActivity extends AppCompatActivity {

    private EditText editText_reminder, editText_additional_note, phone_number, email_address, text_sms;
    private Spinner reminders_category, remind_me, reminder_repeat_type;
    private TextView radiobutton_type, time_view, date_view;
    private ImageButton radio_notify, radio_call, radio_sms, radio_email;
    private String date, time, timeStamp;
    private ArrayAdapter<String> spinneradapter1, spinneradapter2, spinneradapter3;
    private ArrayList<HashMap<String, String>> dataDB;
    private int reminder_type_option;
    private RemindersData remindersData;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_add_item);

        String datetime = getIntent().getExtras().getString("datetime");

        editText_reminder = findViewById(R.id.editText_reminder);
        editText_additional_note = findViewById(R.id.editText_additional_note);
        time_view = findViewById(R.id.time_view);
        date_view = findViewById(R.id.date_view);
        reminders_category = findViewById(R.id.reminders_category);
        remind_me = findViewById(R.id.remind_me);
        reminder_repeat_type =  findViewById(R.id.reminder_repeat_type);
        radio_notify = findViewById(R.id.radio_notify);
        radio_call = findViewById(R.id.radio_call);
        radio_sms = findViewById(R.id.radio_sms);
        radio_email = findViewById(R.id.radio_email);
        radiobutton_type = findViewById(R.id.radiobutton_type);
        phone_number = findViewById(R.id.phone_number);
        email_address = findViewById(R.id.email_address);
        text_sms = findViewById(R.id.text_sms);

        remindersData = new RemindersData(context);
        dataDB = remindersData.readByDateTime(datetime);

        timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());//current date-time

        String[] item1 = new String[]{"Birthday", "Anniversary", "Meeting", "Wake Up", "Shopping", "Others"};
        spinneradapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item1);
        reminders_category.setAdapter(spinneradapter1);

        String[] item2 = new String[]{"Once", "Daily", "Weekly", "Monthly", "Yearly"};
        spinneradapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item2);
        reminder_repeat_type.setAdapter(spinneradapter2);

        String[] item3 = new String[]{"1 hour before", "1 day before", "2 day before", "1 week before", "1 month before"};
        spinneradapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item3);
        remind_me.setAdapter(spinneradapter3);

        editText_reminder.setText(dataDB.get(0).get("0"));
        editText_additional_note.setText(dataDB.get(0).get("1"));
        time_view.setText(dataDB.get(0).get("2"));
        date_view.setText(dataDB.get(0).get("3"));
        int spinnerPosition = spinneradapter1.getPosition(dataDB.get(0).get("4"));
        reminders_category.setSelection(spinnerPosition);
        spinnerPosition = spinneradapter3.getPosition(dataDB.get(0).get("5"));
        remind_me.setSelection(spinnerPosition);
        spinnerPosition = spinneradapter2.getPosition(dataDB.get(0).get("6"));
        reminder_repeat_type.setSelection(spinnerPosition);

        reminder_type_option = Integer.parseInt(dataDB.get(0).get("7"));

        switch(reminder_type_option){
            case 1:
                radiobutton_type.setText("Notify");
                radio_notify.setImageResource(R.drawable.ic_notification);
                radio_call.setImageResource(R.drawable.ic_call_fade);
                radio_sms.setImageResource(R.drawable.ic_sms_fade);
                radio_email.setImageResource(R.drawable.ic_email_fade);
                break;
            case  2:
                radiobutton_type.setText("Call");
                radio_notify.setImageResource(R.drawable.ic_notification_fade);
                radio_call.setImageResource(R.drawable.ic_call);
                radio_sms.setImageResource(R.drawable.ic_sms_fade);
                radio_email.setImageResource(R.drawable.ic_email_fade);
                break;
            case 3:
                radiobutton_type.setText("SMS");
                radio_notify.setImageResource(R.drawable.ic_notification_fade);
                radio_call.setImageResource(R.drawable.ic_call_fade);
                radio_sms.setImageResource(R.drawable.ic_sms);
                radio_email.setImageResource(R.drawable.ic_email_fade);
                break;
            case 4:
                radiobutton_type.setText("Email");
                radio_notify.setImageResource(R.drawable.ic_notification_fade);
                radio_call.setImageResource(R.drawable.ic_call_fade);
                radio_sms.setImageResource(R.drawable.ic_sms_fade);
                radio_email.setImageResource(R.drawable.ic_email);
                break;
        }


        phone_number.setText(dataDB.get(0).get("8"));
        email_address.setText(dataDB.get(0).get("9"));
        text_sms.setText(dataDB.get(0).get("10"));

        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.clock_layout);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

                final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.timePicker);
                Button ok = (Button)dialog.findViewById(R.id.ok);
                Button cancel = (Button)dialog.findViewById(R.id.cancel_btn);

                final String[] AM_PM = new String[1];
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        int h;
                        if(hour > 12){
                            AM_PM[0] ="PM";
                            h = hour - 12;
                        }else{
                            AM_PM[0] ="AM";
                            h = hour;
                        }
                        time_view.setText(h + ":" + minute + " " + AM_PM[0]);
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
        });

        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.calender_layout);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

                final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker);
                Button ok = (Button)dialog.findViewById(R.id.ok);
                Button cancel = (Button)dialog.findViewById(R.id.cancel_btn);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        month = month+1;
                        int year = datePicker.getYear();
                        if(month>=0 && month <=9)
                            date_view.setText(day + "-0" + (month) + "-" + year);
                        else
                            date_view.setText(day + "-" + (month) + "-" + year);
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
        });

        radio_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_type_option = 1;
                radiobutton_type.setText("Notify");
                radio_notify.setImageResource(R.drawable.ic_notification);
                radio_call.setImageResource(R.drawable.ic_call_fade);
                radio_sms.setImageResource(R.drawable.ic_sms_fade);
                radio_email.setImageResource(R.drawable.ic_email_fade);
            }
        });
        radio_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_type_option = 2;
                radiobutton_type.setText("Call");
                radio_notify.setImageResource(R.drawable.ic_notification_fade);
                radio_call.setImageResource(R.drawable.ic_call);
                radio_sms.setImageResource(R.drawable.ic_sms_fade);
                radio_email.setImageResource(R.drawable.ic_email_fade);
            }
        });
        radio_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_type_option = 3;
                radiobutton_type.setText("SMS");
                radio_notify.setImageResource(R.drawable.ic_notification_fade);
                radio_call.setImageResource(R.drawable.ic_call_fade);
                radio_sms.setImageResource(R.drawable.ic_sms);
                radio_email.setImageResource(R.drawable.ic_email_fade);
            }
        });
        radio_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_type_option = 4;
                radiobutton_type.setText("Email");
                radio_notify.setImageResource(R.drawable.ic_notification_fade);
                radio_call.setImageResource(R.drawable.ic_call_fade);
                radio_sms.setImageResource(R.drawable.ic_sms_fade);
                radio_email.setImageResource(R.drawable.ic_email);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_save_btn){
            addReminder();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addReminder() {

        String reminder = editText_reminder.getText().toString();
        String additional_note = editText_additional_note.getText().toString();
        String time = time_view.getText().toString();
        String date = date_view.getText().toString();
        String category = reminders_category.getSelectedItem().toString();
        String remindme = remind_me.getSelectedItem().toString();
        String repeat_type = reminder_repeat_type.getSelectedItem().toString();
        String reminder_type = Integer.toString(reminder_type_option);
        String phone_no = phone_number.getText().toString();
        String email = email_address.getText().toString();
        String send_message = text_sms.getText().toString();

        if(reminder.matches("") && reminder_type.matches("-- None --")){
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
        } else {
            remindersData = new RemindersData(context);

            Boolean check = remindersData.updateItem(reminder,additional_note,time,date,category,remindme,repeat_type,
                    reminder_type,phone_no,email,send_message,timeStamp, dataDB.get(0).get("11"));
            if(check){//entry saved
            }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            remindersData.close();
        }
        finish();
    }

}
