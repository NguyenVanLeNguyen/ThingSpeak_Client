package com.example.hoang.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.hoang.app.R.layout.item;

public class MainActivity extends AppCompatActivity {
    public static final String DEVICE = "DeviceSelect";
    ListView lvDevice ;
    private ArrayList<Device> listDevice;
    private Toolbar toolbar;
    SearchView searchview ;
    Calendar c = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //searchview = (SearchView) findViewById(R.id.action_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        lvDevice = (ListView) findViewById(R.id.liv1);
        ArrayList<Device> listDevice = new ArrayList<>();
        listDevice = new ArrayList<Device>();

        GregorianCalendar time1 = new GregorianCalendar(2017,2,2,1,30,30);
        GregorianCalendar time2 = new GregorianCalendar(2017,2,2,2,30,30);
        GregorianCalendar time3 = new GregorianCalendar(2017,2,2,3,30,30);
        GregorianCalendar time4 = new GregorianCalendar(2017,2,2,4,30,30);
        GregorianCalendar time5 = new GregorianCalendar(2017,2,2,5,30,30);
        GregorianCalendar time6 = new GregorianCalendar(2017,2,2,6,30,30);
        GregorianCalendar time7 = new GregorianCalendar(2017,2,3,0,30,30);
        GregorianCalendar time8 = new GregorianCalendar(2017,2,3,2,30,30);
        GregorianCalendar time9 = new GregorianCalendar(2017,2,3,4,30,30);
        GregorianCalendar time10 = new GregorianCalendar(2017,2,3,5,30,30);

        Pair<GregorianCalendar,Integer> va_1 = Pair.create(time1,40);
        Pair<GregorianCalendar,Integer> va_2 = Pair.create(time2,80);
        Pair<GregorianCalendar,Integer> va_3 = Pair.create(time3,10);
        Pair<GregorianCalendar,Integer> va_4 = Pair.create(time4,10);
        Pair<GregorianCalendar,Integer> va_5 = Pair.create(time5,10);
        Pair<GregorianCalendar,Integer> va_6 = Pair.create(time6,10);
        Pair<GregorianCalendar,Integer> va_7 = Pair.create(time7,10);
        Pair<GregorianCalendar,Integer> va_8 = Pair.create(time8,10);
        Pair<GregorianCalendar,Integer> va_9 = Pair.create(time9,10);
        Pair<GregorianCalendar,Integer> va_10 = Pair.create(time10,10);

        ArrayList<Pair<GregorianCalendar,Integer>> arr =  new ArrayList<Pair<GregorianCalendar,Integer>>();
        ArrayList<Pair<GregorianCalendar,Integer>> arr1 =  new ArrayList<Pair<GregorianCalendar,Integer>>();
        ArrayList<Pair<GregorianCalendar,Integer>> arr2 =  new ArrayList<Pair<GregorianCalendar,Integer>>();
        arr.add(va_1);
        arr.add(va_1);
        arr.add(va_1);
        arr.add(va_1);
        arr.add(va_1);
        arr.add(va_1);
        arr.add(va_1);
        arr.add(va_1);


        arr1.add(va_2);
        arr2.add(va_3);

        Device de_1 = new Device("0001aaaaa",arr);
        Device de_2 = new Device("0004ababa",arr1);
        Device de_3 = new Device("0004ababa",arr2);


        listDevice.add(de_1);
        listDevice.add(de_2);
        listDevice.add(de_3);
        listDevice.add(de_1);
        listDevice.add(de_2);
        listDevice.add(de_3);
        listDevice.add(de_1);
        listDevice.add(de_2);
        listDevice.add(de_3);
        listDevice.add(de_1);
        listDevice.add(de_2);
        listDevice.add(de_3);

        CustomAdapter custem = new CustomAdapter(this, item,listDevice);
        lvDevice.setAdapter(custem);

        final ArrayList<Device> finalListDevice = listDevice;

        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                provideGraph(finalListDevice.get(i));
            }

        });


        final ArrayList<Device> finalListDevice1 = listDevice;

        /*searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Device> newlist = new ArrayList<Device>();
                for(Device de : finalListDevice1){
                    if(newText.contains(de.getAPIkey()));
                    newlist.add(de);

                }
                CustomAdapter custem2 = new CustomAdapter(MainActivity.this, item, newlist);
                lvDevice.setAdapter(custem2);

                return false;
            }
        });*/
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item_) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item_.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            searchview = (SearchView) findViewById(R.id.action_search);
            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<Device> newlist = new ArrayList<Device>();
                    for(Device de : listDevice){
                        if(de.getAPIkey().contains(newText));
                        newlist.add(de);
                    }
                    CustomAdapter custem2 = new CustomAdapter(MainActivity.this, item, newlist);
                    lvDevice.setAdapter(custem2);
                    lvDevice.invalidateViews();
                    return false;
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item_);
    }

    public void provideGraph(Device devi){
        Intent intent = new Intent(MainActivity.this,GraphActivity.class);
        intent.putExtra(DEVICE,devi);
        startActivity(intent);
    }
}
