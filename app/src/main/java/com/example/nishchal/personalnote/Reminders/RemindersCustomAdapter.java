package com.example.nishchal.personalnote.Reminders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.RemindersData;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RemindersCustomAdapter extends RecyclerView.Adapter<RemindersCustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private RemindersData remindersData;
    private int previousPosition = 0;

    public RemindersCustomAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.reminders_card_view, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.reminder.setText(data.get(position).get("0"));
        myViewHolder.show_time.setText(data.get(position).get("1"));
        myViewHolder.show_date.setText(data.get(position).get("2"));
        myViewHolder.repeat_type.setText(data.get(position).get("4"));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cposition = position;
                String datetime = data.get(cposition).get("5");
                Intent intent = new Intent(context, ReminderItemActivity.class);
                intent.putExtra("datetime", datetime);
                context.startActivity(intent);
            }
        });

        myViewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context,myViewHolder.delete_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_delete_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //to delete an item
                        int cposition = position;
                        String timeStamp = data.get(cposition).get("5");
                        Boolean check = remindersData.deleteItem(timeStamp);
                        if(check) {
                            //data deleted
                            data = new RemindersData(context).readData();
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

        TextView reminder, show_time, show_date, repeat_type;
        ImageButton delete_btn, reminder_category;

        public MyViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();

            reminder = itemView.findViewById(R.id.reminder);
            show_time = itemView.findViewById(R.id.show_time);
            show_date = itemView.findViewById(R.id.show_date);
            repeat_type = itemView.findViewById(R.id.repeat_type);
            reminder_category = itemView.findViewById(R.id.reminder_category);
            delete_btn = itemView.findViewById(R.id.delete_btn);

            remindersData = new RemindersData(context);

        }
    }

}
