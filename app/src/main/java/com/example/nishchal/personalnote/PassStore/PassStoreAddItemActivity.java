package com.example.nishchal.personalnote.PassStore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.nishchal.personalnote.Databases.PassStoreData;
import com.example.nishchal.personalnote.EncryptDecryptClass;
import com.example.nishchal.personalnote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PassStoreAddItemActivity extends AppCompatActivity {

    private EditText web_url, username_email, password, additional_notes;
    private SeekBar password_strength;
    private Button save_btn;
    private ImageButton password_show_btn;
    private Boolean password_show_check = false;
    private String pswd, timeStamp;
    private Context context = this;
    private PassStoreData passStoreData;
    private EncryptDecryptClass encryptDecryptClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_item_activity);

        web_url = (EditText)findViewById(R.id.web_url);
        username_email = (EditText)findViewById(R.id.username_email);
        password = (EditText)findViewById(R.id.password);
        password_strength = (SeekBar)findViewById(R.id.password_strength);
        additional_notes = (EditText)findViewById(R.id.additional_notes);
        save_btn = (Button)findViewById(R.id.save);
        password_show_btn = (ImageButton)findViewById(R.id.password_show_btn);
        encryptDecryptClass = new EncryptDecryptClass();
        //password_strength.setEnabled(false);

        timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNotes();
                finish();
            }
        });

        password_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!password_show_check) {
                    password_show_btn.setImageResource(R.drawable.ic_visibility);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password_show_check = !password_show_check;
                }
                else {
                    password_show_btn.setImageResource(R.drawable.ic_visibility_off);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_show_check = !password_show_check;
                }
            }
        });

    }

    private void addNotes() {
        String weburl = web_url.getText().toString();
        String usernameemail = username_email.getText().toString();
        try {
            pswd = encryptDecryptClass.encryptMsg(password.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String additionalnotes = additional_notes.getText().toString();
        if(weburl.matches("") && usernameemail.matches("") && pswd.matches("") && additionalnotes.matches("")){

            //Empty note, not created...

        } else{

            passStoreData = new PassStoreData(context);

            Boolean check = passStoreData.insertData(weburl, usernameemail, pswd, additionalnotes, timeStamp);
            if(check) {//note saved
            }
            else
                Toast.makeText(getBaseContext(), "Nothing saved", Toast.LENGTH_SHORT).show();

            passStoreData.close();

        }
    }

}
