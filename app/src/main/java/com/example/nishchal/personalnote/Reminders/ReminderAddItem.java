package com.example.nishchal.personalnote.Reminders;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.example.nishchal.personalnote.AlertReceiver;
import com.example.nishchal.personalnote.Databases.RemindersData;
import com.example.nishchal.personalnote.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ReminderAddItem extends AppCompatActivity {

    private EditText editText_reminder, editText_additional_note, phone_number, email_address, text_sms;
    private Spinner reminders_category, remind_me, reminder_repeat_type;
    private TextView radiobutton_type, time_view, date_view;
    private ImageButton radio_notify, radio_call, radio_sms, radio_email;
    private String date, time, timeStamp;
    private ArrayAdapter<String> spinneradapter;
    private int reminder_type_option = 1;
    private RemindersData remindersData;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_add_item);

        editText_reminder = findViewById(R.id.editText_reminder);
        editText_additional_note = findViewById(R.id.editText_additional_note);
        time_view = findViewById(R.id.time_view);
        date_view = findViewById(R.id.date_view);
        reminders_category = findViewById(R.id.reminders_category);
        remind_me = findViewById(R.id.remind_me);
        reminder_repeat_type = findViewById(R.id.reminder_repeat_type);
        radio_notify = findViewById(R.id.radio_notify);
        radio_call = findViewById(R.id.radio_call);
        radio_sms = findViewById(R.id.radio_sms);
        radio_email = findViewById(R.id.radio_email);
        radiobutton_type = findViewById(R.id.radiobutton_type);
        phone_number = findViewById(R.id.phone_number);
        email_address = findViewById(R.id.email_address);
        text_sms = findViewById(R.id.text_sms);

        date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());//current date
        date_view.setText(date);
        time = new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime());//current time
        time_view.setText(time);

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


        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.clock_layout);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

                final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.timePicker);
                Button ok = dialog.findViewById(R.id.ok);
                Button cancel = dialog.findViewById(R.id.cancel_btn);

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

        String[] item1 = new String[]{"Birthday", "Anniversary", "Meeting", "Wake Up", "Shopping", "Others"};
        spinneradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item1);
        reminders_category.setAdapter(spinneradapter);

        String[] item2 = new String[]{"Once", "Daily", "Weekly", "Monthly", "Yearly"};
        spinneradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item2);
        reminder_repeat_type.setAdapter(spinneradapter);

        String[] item3 = new String[]{"1 hour before", "1 day before", "2 day before", "1 week before", "1 month before"};
        spinneradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item3);
        remind_me.setAdapter(spinneradapter);

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

        timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());//current date-time

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

            setAlarm();
            Boolean check = remindersData.insertData(reminder,additional_note,time,date,category,remindme,repeat_type,
                    reminder_type,phone_no,email,send_message,timeStamp);
            if(check){//entry saved
            }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            remindersData.close();
        }
        finish();
    }

    private void setAlarm() {

        Date date = new Date();
        Date cdate = new Date();
        int[] arr = new int[2];
        String final_time;
        String tTime = time_view.getText().toString();
        String[] timeArray = tTime.split(" ");
        String[] HHmm = timeArray[0].split(":");
        Log.e("time format", timeArray[1]);
        if(timeArray[1].matches("PM")){
            arr[0] = Integer.parseInt(HHmm[0]) + 12;
            arr[1] = Integer.parseInt(HHmm[1]);
            final_time = Integer.toString(arr[0]) + ":" + Integer.toString(arr[1]);
        }else{
            arr[0] = Integer.parseInt(HHmm[0]);
            arr[1] = Integer.parseInt(HHmm[1]);
            final_time = Integer.toString(arr[0]) + ":" + Integer.toString(arr[1]);
        }

        String tDate = date_view.getText().toString();

        String dateTime = tDate + " " + final_time + ":00"; // format as --- 2014/10/29 18:10:45

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            date = sdf.parse(dateTime);
            cdate = sdf.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //current time in millisecs
        long ctimemillis = cdate.getTime();

        //set time in millisecs
        long millis = date.getTime();

        Long diff = millis - ctimemillis;

        Log.e("timeStamp", timeStamp);
        Log.e("date-time", dateTime);
        Log.e("millis", diff.toString());

        Long alertTime  = new GregorianCalendar().getTimeInMillis() + diff;
        int id = (int) System.currentTimeMillis();
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        alertIntent.putExtra("requestcode",id);
        alertIntent.putExtra("reminder",editText_reminder.getText().toString());
        alertIntent.putExtra("additional_note",editText_additional_note.getText().toString());

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, id, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

    }

}
