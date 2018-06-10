package com.example.nishchal.personalnote.Diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.DiaryData;
import com.example.nishchal.personalnote.Notes.NotesCustomAdapter;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DiaryCustomAdapter extends RecyclerView.Adapter<DiaryCustomAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> data;
    private Context context;
    private LayoutInflater inflater;
    private DiaryData diaryData;
    private int previousPosition = 0;

    public DiaryCustomAdapter(ArrayList<HashMap<String, String>> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.diary_card_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.date.setText(data.get(position).get("0"));
        myViewHolder.title.setText(data.get(position).get("1"));
        myViewHolder.content.setText(data.get(position).get("2"));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cposition = String.valueOf(position);
                Intent intent = new Intent(context, DiaryItemActivity.class);
                intent.putExtra("ItemPosition", cposition);
                context.startActivity(intent);
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
                        String timeStamp = data.get(cposition).get("3");
                        Boolean check = diaryData.deleteItem(timeStamp);
                        if(check) {
                            //data deleted
                            data = new DiaryData(context).readData();
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


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date, title, content;
        ImageView delete_menu_btn;
        private final Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            date = itemView.findViewById(R.id.diary_date);
            title = itemView.findViewById(R.id.diary_title);
            content = itemView.findViewById(R.id.diary_content);
            delete_menu_btn = itemView.findViewById(R.id.delete_menu_btn);
            diaryData = new DiaryData(context);

        }
    }
}
