package com.example.nishchal.personalnote.Budget;

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
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.BudgetData;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BudgetAddEntry extends AppCompatActivity{

    private EditText budget_date, budget_entry_name, budget_amount;
    private Spinner budget_category;
    private Button add_entry_btn;
    private View layout;
    private String time, timeStamp;
    private BudgetData budgetData;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_add_entry);
        String strDate = new SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().getTime());

        budget_date = findViewById(R.id.date);
        budget_category = findViewById(R.id.spinner);
        budget_entry_name = findViewById(R.id.budget_entry_name);
        budget_amount = findViewById(R.id.budget_amount);
        add_entry_btn = findViewById(R.id.budget_add_btn);

        String[] items = new String[]{"Food", "Transport", "Shopping", "Recharge", "Study", "Entertainment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        budget_category.setAdapter(adapter);

        budget_date.setText(strDate);

        budget_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalenderPopUp();
            }
        });

        add_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntry();
            }
        });

        time = new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime());
        timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());


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
                    budget_date.setText(day + "-0" + (month) + "-" + year);
                else
                    budget_date.setText(day + "-" + (month) + "-" + year);
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

    private void addEntry(){

        String date = budget_date.getText().toString();
        String category = budget_category.getSelectedItem().toString();
        String entry_name = budget_entry_name.getText().toString();
        int amount = Integer.parseInt(budget_amount.getText().toString());

        if(category.matches("") && entry_name.matches("")){

            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();

        }else {

            budgetData = new BudgetData(context);

            Boolean check = budgetData.insertData(date, time, category, entry_name, amount, timeStamp);
            if(check){//entry saved
                 }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();
            budgetData.close();

        }

        finish();

    }

}
