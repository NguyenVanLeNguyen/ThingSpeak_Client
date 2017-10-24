package com.example.hoang.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hoang.app.Chanel;

import java.util.ArrayList;

/**
 * Created by hoang on 24/10/2017.
 */

public class DbManagerGateway extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="gateway_list";
    private static final String TABLE_NAME ="gateways";
    private static final String APIKEY ="APIkey";
    private static final String NAME ="name";
    private static final String LATITUDE ="Latitude";
    private static final String LONGITUDE ="Longitude";
    private static final String LASTUPDTAE ="TimeUpdate";

    private Context context;
    public DbManagerGateway(Context context) {
        super(context, DATABASE_NAME,null, 1);
       //Log.d("DBManager", "DBManager: ");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                APIKEY +" INTEGER primary key, "+
                NAME + " TEXT, "+
                LATITUDE +" DOUBLE, "+
                LONGITUDE+" DOUBLE," +
                LASTUPDTAE +" TEXT)";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addGateway(String APIkey,double latitude,double longitude,String timeUpdate)
    {
        return  true;
    }

    public boolean removeGateway(String APIkey)
    {

        return true;
    }

    public boolean updateGateway(String APIkey,double latitude,double longitude,String timeUpdate)
    {
        return  true;
    }

    public Chanel getGatewaybyAPIkey(String APIkey)
    {
        Chanel chanel= new Chanel();
        return chanel;
    }

    public ArrayList<Chanel> getAllGateway()
    {
        ArrayList<Chanel> arrChanel = new ArrayList<>();
        return  arrChanel;
    }
}
