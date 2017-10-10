package com.example.hoang.AboutNetWork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hoang.app.R;

public class ShowInformationActivity extends AppCompatActivity {
    public static final String APIKEY = "APIkey";
    public static final String PART = "part";
    //public static final String TEXTVIEWID = "tewviewid";
    Button button_Tem;
    Button button_Hum;
    Bundle bundle_1 = new Bundle();
    Bundle bundle_2 = new Bundle();
    GetData st = new GetData(ShowInformationActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_information);
        button_Tem = (Button) findViewById(R.id.but_Tem);
        button_Hum = (Button) findViewById(R.id.but_Hum);


        bundle_1.putString(APIKEY,"E3FFAF95BBICV2YP");
        bundle_1.putString(PART,"field1");
       // bundle_1.putString(TEXTVIEWID,"tvw_Tem");
        bundle_2.putString(APIKEY,"E3FFAF95BBICV2YP");
        bundle_2.putString(PART,"field2");
        //bundle_2.putString(TEXTVIEWID,"tvw_Hum");
        button_Tem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData st = new GetData(ShowInformationActivity.this);
                st.execute(bundle_1);
            }
        });

        button_Hum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData st = new GetData(ShowInformationActivity.this);
                st.execute(bundle_2);
            }
        });
    }
}
