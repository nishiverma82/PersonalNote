package com.example.nishchal.personalnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Budget.BudgetFragment;
import com.example.nishchal.personalnote.Diary.DiaryFragment;
import com.example.nishchal.personalnote.Notes.NotesFragment;
import com.example.nishchal.personalnote.PassStore.PassStoreFragment;
import com.example.nishchal.personalnote.Reminders.RemindersFragment;
import com.example.nishchal.personalnote.Settings.Settings;
import com.example.nishchal.personalnote.Signin.EmailPasswordSignIn;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String value;
    private Toolbar toolbar = null;
    private NavigationView navigationView = null;

    @Override
    protected void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value = getIntent().getExtras().getString("Email");

        NotesFragment notesFragment = new NotesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,notesFragment);
        fragmentTransaction.commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        View hView =  navigationView.getHeaderView(0);

        TextView user_email = (TextView)hView.findViewById(R.id.useremail);
        user_email.setText(value);

        mAuth = FirebaseAuth.getInstance();

        //google email signout listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, EmailPasswordSignIn.class));
                }
            }
        };

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            //exiting from application on back press
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moveTaskToBack(true);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            //alert.setCanceledOnTouchOutside(false);
            alert.show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.nav_setting:

                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);

                break;
            case R.id.nav_logout:

                FirebaseAuth.getInstance().signOut();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {

            item.setChecked(true);

            //notes fragment
            NotesFragment notesFragment = new NotesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            getSupportActionBar().setTitle(" Notes");
            fragmentTransaction.replace(R.id.framelayout,notesFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_my_diary) {

            //My Diary
            DiaryFragment diaryFragment = new DiaryFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            getSupportActionBar().setTitle("My Diary");
            fragmentTransaction.replace(R.id.framelayout,diaryFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_reminders) {

            //Reminders
            RemindersFragment remindersFragment = new RemindersFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            getSupportActionBar().setTitle("Reminders");
            fragmentTransaction.replace(R.id.framelayout,remindersFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_pass_store) {

            //Pass Store
            PassStoreFragment passStoreFragment = new PassStoreFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            getSupportActionBar().setTitle("Pass Store");
            fragmentTransaction.replace(R.id.framelayout,passStoreFragment);
            fragmentTransaction.commit();

        } else if(id == R.id.nav_my_budget){

            //My Budget
            BudgetFragment budgetFragment = new BudgetFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            getSupportActionBar().setTitle("My Budget");
            fragmentTransaction.replace(R.id.framelayout,budgetFragment);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_setting) {

            Intent i = new Intent(MainActivity.this, Settings.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

            Toast.makeText(this, "You selected to share!", Toast.LENGTH_SHORT).show();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}