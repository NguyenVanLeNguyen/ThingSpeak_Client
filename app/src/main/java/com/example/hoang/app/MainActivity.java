package com.example.hoang.app;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

//import android.support.design.widget.NavigationView;

import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import android.view.Menu;
import android.view.MenuItem;

import com.example.hoang.AboutNetWork.ConnectionReceiver;
import com.example.hoang.Notification.CheckNote;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import static com.example.hoang.app.R.layout.item;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public final static  String SHARED_PREFERENCES_NAME = "CURRENT_CHANEL";
    public static final String DEVICE = "DeviceSelect";
    private ListView lvDevice ;
    private SharedPreferences sharedPreferences;
    private String chanelID;
    private ArrayList<Device> listDevice;
    private  MaterialSearchView searchView;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
       // actionBar.setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //SharedPreferences.Editor editor = sharedPreferences.edit();

        sharedPreferences  = this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        chanelID = sharedPreferences.getString("CHANEL_ID","");

        if(ConnectionReceiver.isConnected()){
            if (chanelID.equals("") || chanelID.isEmpty()){
                displayDialogChanelID();

            }
            else{
                ComponentName serviceComponent = new ComponentName(this, CheckNote.class);
                JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
                builder.setPeriodic( 20000 );
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED );
                builder.setRequiresCharging(false);
                builder.setRequiresDeviceIdle(false);
                PersistableBundle bundle = new PersistableBundle();
                bundle.putString("abc",chanelID);
                builder.setExtras(bundle);
                jobInfo = builder.build();
                jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                startJob();
                listDevice = new ArrayList<>();
                GetChanel getch = new GetChanel(MainActivity.this);
                getch.execute(chanelID);
            }

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This Application requires Internet connection,Please enable data/wifi and restart!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener( this);
        //final ArrayList<Device> finalListDevice1 = listDevice;

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CHANEL_ID",chanelID);
        editor.apply();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tolbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item_) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item_.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }


        return super.onOptionsItemSelected(item_);
    }


    public void setList(){

        lvDevice = (ListView) findViewById(R.id.liv1);

        CustomAdapter custem = new CustomAdapter(this, item, listDevice);
        lvDevice.setAdapter(custem);
        final ArrayList<Device> finalListDevice = listDevice;

        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(finalListDevice.get(i) == null){
                }
                else{
                   GetFieldFeed loadingField = new GetFieldFeed(MainActivity.this);
                   loadingField.execute(finalListDevice.get(i));

                }

            }

        });

    }

    public void displayDialogChanelID(){
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.enter_chanelid, null);
        final EditText enterChanelID = alertLayout.findViewById(R.id.edt_chanelid);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertLayout).
                setPositiveButton("OK" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                chanelID = enterChanelID.getText().toString();
                listDevice = new ArrayList<>();
                GetChanel getch = new GetChanel(MainActivity.this);
                getch.execute(chanelID);

            }
        })
         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(chanelID == null || chanelID.equals("`"))
                             finish();
                    }
                });
        builder.setTitle("Enter your ChanelID");
        builder.setMessage("you can file chanelID in Chanel Settings");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
     public void provideGraph(Device devi){
        Intent intent = new Intent(MainActivity.this,GraphActivity.class);
        intent.putExtra(DEVICE, devi);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.changeCN) {
           displayDialogChanelID();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public ArrayList<Device> getListDevice() {
        return listDevice;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void searchViewCode(){
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

    }
    public void updateData(View view){
        sharedPreferences  = this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        chanelID = sharedPreferences.getString("CHANEL_ID","");

        if(ConnectionReceiver.isConnected()){
            if (chanelID.equals("") || chanelID.isEmpty()){
                displayDialogChanelID();

            }
            else{
                ComponentName serviceComponent = new ComponentName(this, CheckNote.class);
                JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
                builder.setPeriodic( 20000 );
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED );
                builder.setRequiresCharging(false);
                builder.setRequiresDeviceIdle(false);
                PersistableBundle bundle = new PersistableBundle();
                bundle.putString("abc",chanelID);
                builder.setExtras(bundle);
                jobInfo = builder.build();
                jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                startJob();
                listDevice = new ArrayList<>();
                GetChanel getch = new GetChanel(MainActivity.this);
                getch.execute(chanelID);
            }

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This Application requires Internet connection,Please enable data/wifi and restart!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    public void startJob() {
        jobScheduler.schedule(jobInfo);
        Log.d("status","start button");
    }


    public void cancelJob() {
        jobScheduler.cancelAll();
        Log.d("status","stop button");

    }


}
