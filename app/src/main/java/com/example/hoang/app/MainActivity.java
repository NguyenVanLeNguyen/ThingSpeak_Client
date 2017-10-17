package com.example.hoang.app;

import android.content.Intent;

//import android.support.design.widget.NavigationView;

import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.view.Menu;
import android.view.MenuItem;

import com.example.hoang.AboutNetWork.ConnectionReceiver;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static com.example.hoang.app.R.layout.item;

public class MainActivity extends AppCompatActivity {
    public final static  String TAG = "devi";
    public static final String DEVICE = "DeviceSelect";
    private ListView lvDevice ;
    private Chanel chanel;
    private ArrayList<Device> listDevice;
    private ArrayList<Device> CopyList;
    CustomAdapter custem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
       // actionBar.setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        listDevice = new ArrayList<>();
        GetChanel getch = new GetChanel(MainActivity.this);
        getch.execute("313786");










       // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        //final ArrayList<Device> finalListDevice1 = listDevice;

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tolbar, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchView searchview = (SearchView) searchViewItem.getActionView();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (listDevice.size() > 0) {
                    listDevice.clear();
                    if (query.length() == 0) {
                        listDevice.addAll(CopyList);
                    } else {
                        for (Device de : CopyList) {
                            if (de.getName().contains(query)) {
                                listDevice.add(de);
                            }
                        }
                    }
                    custem.notifyDataSetChanged();

                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (listDevice.size() > 0) {
                    listDevice.clear();
                    if(newText.length() == 0) {
                        listDevice.addAll(CopyList);
                    }
                    else
                    {
                        for(Device de : CopyList)
                        {
                            if(de.getName().contains(newText))
                            {
                                listDevice.add(de);
                            }
                        }
                    }
                    custem.notifyDataSetChanged();
                }
                return false;
            }
        });


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
        CopyList = new ArrayList<>();
        CopyList.addAll(listDevice);

        custem = new CustomAdapter(this, item,listDevice);
        lvDevice.setAdapter(custem);
        Log.d("tag:::","above");
        final ArrayList<Device> finalListDevice = listDevice;

        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(finalListDevice.get(i).getData() == null){
                }
                else
                    provideGraph(finalListDevice.get(i));
            }

        });
        Log.d("tag:::","behive");
    }

     public void provideGraph(Device devi){
        Intent intent = new Intent(MainActivity.this,GraphActivity.class);
        intent.putExtra(DEVICE, devi);
        startActivity(intent);
    }

    public ArrayList<Device> getListDevice() {
        return listDevice;
    }

    public void setListDevice(ArrayList<Device> listDevice) {
        this.listDevice = listDevice;
    }
}
