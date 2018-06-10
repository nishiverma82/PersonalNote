package com.example.nishchal.personalnote.Notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.nishchal.personalnote.R;

import java.util.ArrayList;
import java.util.HashMap;


public class NotesListCustomAdapter extends BaseAdapter {

    private Context context;
    private CheckBox checkBox;

    public NotesListCustomAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return ListItemsClass.list.size();
    }

    @Override
    public Object getItem(int i) {
        return ListItemsClass.list.get(i).get("item");
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View rowView = view;

        rowView = LayoutInflater.from(context).inflate(R.layout.notes_list_item_view, null);
        checkBox = rowView.findViewById(R.id.checkBox);
        final HashMap<String, String> map = new HashMap<>();
        if(ListItemsClass.list.size() != 0) {
            checkBox.setText(ListItemsClass.list.get(i).get("item"));

            if (ListItemsClass.list.get(i).get("value").matches("1"))
                checkBox.setChecked(true);
            else if (ListItemsClass.list.get(i).get("value").matches("0"))
                checkBox.setChecked(false);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        map.put("item", ListItemsClass.list.get(i).get("item"));
                        map.put("value", "1");
                        ListItemsClass.list.set(i, map);
                    }
                    if (!compoundButton.isChecked()) {
                        map.put("item", ListItemsClass.list.get(i).get("item"));
                        map.put("value", "0");
                        ListItemsClass.list.set(i, map);
                    }
                }
            });
        }

        return rowView;
    }
}
