package com.example.nishchal.personalnote.PassStore;

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

import com.example.nishchal.personalnote.Databases.PassStoreData;
import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PassStoreCustomAdapter extends RecyclerView.Adapter<PassStoreCustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private PassStoreData passStoreData;

    public PassStoreCustomAdapter(Context context, ArrayList<HashMap<String, String>> data) {

        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.pass_card_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.websiteView.setText(data.get(position).get("0"));

        myViewHolder.delete_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, myViewHolder.delete_menu_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_delete_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //to delete an item
                        int cposition = position;
                        String timeStamp = data.get(cposition).get("4");
                        Boolean check = passStoreData.deleteItem(timeStamp);
                        if(check) {
                            //data deleted
                            data = new PassStoreData(context).readData();
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
                Intent intent = new Intent(context, PassStoreItemActivity.class);
                intent.putExtra("ItemPosition",cposition);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView websiteView;
        protected ImageButton delete_menu_btn;
        private final Context context;

        public MyViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();

            websiteView = itemView.findViewById(R.id.websiteview);
            delete_menu_btn = itemView.findViewById(R.id.delete_menu_btn);
            passStoreData = new PassStoreData(context);
        }


    }

}
