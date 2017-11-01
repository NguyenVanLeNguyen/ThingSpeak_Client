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
import android.database.MatrixCursor;
import android.os.PersistableBundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
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
import android.widget.Toast;


import com.example.hoang.AboutNetWork.ConnectionReceiver;
import com.example.hoang.Notification.CheckNote;


import java.util.ArrayList;

import static com.example.hoang.app.R.layout.item;

public class MainActivity extends ShowListDevice implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnSuggestionListener,SearchView.OnQueryTextListener{
    public final static  String SHARED_PREFERENCES_NAME = "CURRENT_CHANEL";
    public static final String RESULTSEARCH = "ResultSearch";
    private ListView lvDevice ;
    private SharedPreferences sharedPreferences;
    private String chanelID;
    private ArrayList<Device> listDevice;
    private SearchView searchView;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;


    private SimpleCursorAdapter mAdapter;
    private static  String[] DEVICES ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = new SearchView(this);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
       // actionBar.setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        hint_sugges();
        sharedPreferences  = this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        chanelID = sharedPreferences.getString("CHANEL_ID","");

        if(ConnectionReceiver.isConnected()){
            if (chanelID.equals("") || chanelID.isEmpty()){
                displayDialogChanelID();

            }
            else{

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
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        searchView.setSuggestionsAdapter(mAdapter);

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
                if(ConnectionReceiver.isConnected()) {
                    if (finalListDevice.get(i) == null) {
                    } else {
                        GetFieldFeed loadingField = new GetFieldFeed(MainActivity.this);
                        loadingField.execute(finalListDevice.get(i));

                    }
                }
                else
                    Toast.makeText(MainActivity.this,"network error",Toast.LENGTH_SHORT).show();

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

    @Override
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
         if (!searchView.isIconified()) {
        searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
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
               // startJob();
                listDevice = new ArrayList<>();
                GetChanel getch = new GetChanel(MainActivity.this);
                getch.execute(chanelID);
                Toast.makeText(MainActivity.this,"update",Toast.LENGTH_SHORT).show();
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
        jobScheduler.schedule(jobInfo);
        Log.d("status","start button");
    }

    public void cancelJob() {
        jobScheduler.cancelAll();
        Log.d("status","stop button");

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        ArrayList<Device> serArr = new ArrayList<>();

        ArrayList<Device> Devices = listDevice;
        if (Devices.size() > 0){
            for (Device devi : Devices) {
                if(devi.getName().contains(query)){
                    try {
                        Device aDevi =(Device) devi.clone();
                        serArr.add(aDevi);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }


        Intent intent = new Intent(MainActivity.this,ResultSearchActivity.class);
        intent.putParcelableArrayListExtra(RESULTSEARCH,serArr);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        giveSuggestions(newText);
        return false;
    }

    private void giveSuggestions(String query) {
        final MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
        for (int i = 0; i < DEVICES.length; i++) {
            if (DEVICES[i].toLowerCase().contains(query.toLowerCase()))
                cursor.addRow(new Object[]{i, DEVICES[i]});
        }
        mAdapter.changeCursor(cursor);

    }

    private void hint_sugges(){
        final String[] from = new String[]{"cityName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.hint_row,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public boolean onSuggestionSelect(int position) {

        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        searchView.setQuery(mAdapter.getCursor().getString(position+1),false);
        return true;
    }

    public static void setDEVICES(String[] DEVICES) {
        MainActivity.DEVICES = DEVICES;
    }
}
