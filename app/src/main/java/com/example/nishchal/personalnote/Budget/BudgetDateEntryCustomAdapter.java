package com.example.nishchal.personalnote.Budget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.BudgetData;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.WINDOW_SERVICE;

public class BudgetDateEntryCustomAdapter extends RecyclerView.Adapter<BudgetDateEntryCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater;
    private BudgetData budgetData;
    private String date;
    int swidth, sheight, rotation;

    private int previousPosition = 0;

    public BudgetDateEntryCustomAdapter(Context context, ArrayList<HashMap<String, String>> data, String date,int screen_width, int screen_height, int rotation) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.date = date;
        this.swidth = screen_width;
        this.sheight = screen_height;
        this.rotation = rotation;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.budget_date_entry_card_view, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.time.setText(data.get(position).get("0"));
        myViewHolder.category.setText(data.get(position).get("1"));
        myViewHolder.entry_name.setText(data.get(position).get("2"));
        myViewHolder.amount.setText(data.get(position).get("3"));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cposition = position;
                updateDialog(cposition);
            }
        });

        myViewHolder.delete_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context,myViewHolder.delete_menu_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_delete_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //to delete an item
                        int cposition = position;
                        String timeStamp = data.get(cposition).get("4");
                        Boolean check = budgetData.deleteItem(timeStamp);
                        if(check) {
                            //data deleted
                            data = new BudgetData(context).readByTime(date);
                            notifyItemRemoved(cposition);
                        }
                        else Toast.makeText(context, "Deletion is unsuccessful", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        previousPosition = position;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time, category, entry_name, amount;
        ImageButton delete_menu_btn;
        private final Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            time = itemView.findViewById(R.id.time);
            category = itemView.findViewById(R.id.category);
            entry_name = itemView.findViewById(R.id.entry_name);
            amount = itemView.findViewById(R.id.budget_amount);
            delete_menu_btn = itemView.findViewById(R.id.delete_menu_btn);
            budgetData = new BudgetData(context);

        }
    }

    private void updateDialog(int position){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.budget_add_entry);
        dialog.show();
        if((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && sheight > swidth){
            Double width = swidth*0.9;
            dialog.getWindow().setLayout(width.intValue(),WindowManager.LayoutParams.WRAP_CONTENT);
        }else if((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && swidth > sheight){
            Double width = swidth*0.5;
            Double height = sheight*0.9;
            dialog.getWindow().setLayout(width.intValue(),width.intValue());
        }

        final EditText budget_date = dialog.findViewById(R.id.date);
        final Spinner budget_category = dialog.findViewById(R.id.spinner);
        final EditText budget_entry_name = dialog.findViewById(R.id.budget_entry_name);
        final EditText budget_amount = dialog.findViewById(R.id.budget_amount);
        Button add_entry_btn = dialog.findViewById(R.id.budget_add_btn);

        String[] items = new String[]{"Food", "Transport", "Shopping", "Recharge", "Study", "Entertainment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        budget_category.setAdapter(adapter);

        final String time = new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime());
        final String timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        final String pre_timeStamp = data.get(position).get("4");

        budget_date.setText(date);
        int spinnerPosition = adapter.getPosition(data.get(position).get("1"));
        budget_category.setSelection(spinnerPosition);
        budget_entry_name.setText(data.get(position).get("2"));
        budget_amount.setText(data.get(position).get("3"));

        add_entry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = budget_date.getText().toString();
                String category = budget_category.getSelectedItem().toString();
                String entry_name = budget_entry_name.getText().toString();
                int amount = Integer.parseInt(budget_amount.getText().toString());

                Boolean check = budgetData.updateItem(date, time, category, entry_name,amount, timeStamp, pre_timeStamp);
                if(check) {
                    //data updated
                    data = new BudgetData(context).readByTime(date);
                    notifyDataSetChanged();
                }
                else Toast.makeText(context, "Update process is unsuccessful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

}