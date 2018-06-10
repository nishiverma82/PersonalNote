package com.example.nishchal.personalnote.Notes;

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

import com.example.nishchal.personalnote.ConversionStringArray;
import com.example.nishchal.personalnote.Databases.NotesData;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesCustomAdapter extends RecyclerView.Adapter<NotesCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private NotesData notesData;
    private LayoutInflater inflater;
    private String strSeparator = "\n☐ ";
    private ArrayList<HashMap<String, String>> list;
    private String listString;

    private int previousPosition = 0;

    public NotesCustomAdapter(Context context, ArrayList<HashMap<String, String>> data) {

        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = inflater.inflate(R.layout.notes_card_view, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.title.setText(data.get(position).get("0"));
        if(data.get(position).get("1").contains("☐")){
           list = ConversionStringArray.convertStringToArray(data.get(position).get("1"));
           listString = "☐ ";
           for(int x=0; x<list.size();x++){
               listString = listString + list.get(x).get("item");
               if(x<list.size()-1){
                   listString = listString + strSeparator;
               }
           }
           myViewHolder.content.setText(listString);
        }else
            myViewHolder.content.setText(data.get(position).get("1"));

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
                        String timeStamp = data.get(cposition).get("2");
                        Boolean check = notesData.deleteItem(timeStamp);
                        if(check) {
                            //data deleted
                            data = new NotesData(context).readData();
                            notifyItemRemoved(cposition);
                        }
                        else Toast.makeText(context, "Deletion is unsuccessful", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cposition = String.valueOf(position);
                if(data.get(Integer.parseInt(cposition)).get("1").indexOf("\n☐") != -1 ){
                    //activity for list card view item
                    Intent intent = new Intent(context,NotesCardListActivity.class);
                    intent.putExtra("ItemPosition",cposition);
                    context.startActivity(intent);
                }
                else {
                    //activity for simple content type card view item
                    Intent intent = new Intent(context, NotesCardClickActivity.class);
                    intent.putExtra("ItemPosition", cposition);
                    context.startActivity(intent);
                }
            }
        });

        previousPosition = position;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        protected TextView title,content;
        protected ImageButton delete_menu_btn;

        private final Context context;

        private MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            delete_menu_btn = itemView.findViewById(R.id.delete_menu_btn);
            notesData = new NotesData(context);

        }

    }

}
