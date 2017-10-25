package com.example.hoang.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

//import android.support.design.widget.NavigationView;

import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import android.view.Menu;
import android.view.MenuItem;


import com.example.hoang.AboutNetWork.ConnectionReceiver;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


import java.util.ArrayList;


import static com.example.hoang.app.R.layout.item;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public final static  String SHARED_PREFERENCES_NAME = "CURRENT_CHANEL";
    public static final String DEVICE = "DeviceSelect";
    public static final String RESULTSEARCH = "ResultSearch";
    private ListView lvDevice ;
    private SharedPreferences sharedPreferences;
    private String chanelID;
    private ArrayList<Device> listDevice;
    private ArrayList<Device> CopyList;
    private CustomAdapter custem;
    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view_q);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
       actionBar.setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();




        //SharedPreferences.Editor editor = sharedPreferences.edit();
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
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tolbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }




    public void setList(final String APIkey){

        lvDevice = (ListView) findViewById(R.id.liv1);


        custem = new CustomAdapter(this, item,listDevice);
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
        View alertLayout = inflater.inflate(R.layout.enter_chanelid, null);
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.changeCN) {
           displayDialogChanelID();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void searchViewCode(final ArrayList<Device> Devices){
       String[] strArr = new String[Devices.size()];
        int i = 0;
        for (Device de : Devices) {
            strArr[i] = de.getName();
            i++;
        }
       searchView.setEllipsize(true);
        searchView.setSuggestions(strArr);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Device> serArr = new ArrayList<>();
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


                Intent intent = new Intent(MainActivity.this,ResultSearchDevice.class);
                intent.putParcelableArrayListExtra(RESULTSEARCH,serArr);
                startActivity(intent);

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
        searchView.dismissSuggestions();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ArrayList<Device> getListDevice() {
        return listDevice;
    }

    public void setListDevice(ArrayList<Device> listDevice) {
        this.listDevice = listDevice;
    }

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(MaterialSearchView searchView) {
        this.searchView = searchView;
    }
}
