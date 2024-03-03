package com.example.greendzine_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    ImageButton home, user;
    ArrayList<HashMap<String,String>> employee = new ArrayList<>();

    TextView mon,tues,wed,thurs,fri,sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        home = findViewById(R.id.home_btn);
        user = findViewById(R.id.emp_btn);
        mon = findViewById(R.id.monday);
        tues = findViewById(R.id.tuesday);
        wed = findViewById(R.id.wednesday);
        thurs = findViewById(R.id.thursday);
        fri = findViewById(R.id.friday);
        sat = findViewById(R.id.saturaday);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

//        To read JSON source data from emp.json
        String json = loadJSONFromAsset(this,"emp.json");
        String productivity = parseJson(name,employee,json);

//        Display employee productivity details
        if(!productivity.isEmpty()) {
            ((TextView) mon.findViewById(R.id.monday)).setText("Productivity on Monday => " + productivity.charAt(1)+"%");
            ((TextView) tues.findViewById(R.id.tuesday)).setText("Productivity on Tuesday => " + productivity.charAt(3)+"%");
            ((TextView) wed.findViewById(R.id.wednesday)).setText("Productivity on Wednesday => " + productivity.charAt(5)+"%");
            ((TextView) thurs.findViewById(R.id.thursday)).setText("Productivity on Thursday => " + productivity.charAt(7)+"%");
            ((TextView) fri.findViewById(R.id.friday)).setText("Productivity on Friday => " + productivity.charAt(9)+"%");
            ((TextView) sat.findViewById(R.id.saturaday)).setText("Productivity on Saturaday => " + productivity.charAt(11)+"%");
        }


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    To Employee page
                    Intent i = new Intent(Dashboard.this,User.class);
                    startActivity(i);

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "In home",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static String parseJson(String name, ArrayList<HashMap<String,String>> employee, String jsonString) {
        try
        {
            JSONObject jObject= new JSONObject(jsonString).getJSONObject("employee");
            Iterator<String> keys = jObject.keys();
            HashMap<String,String> emp = new HashMap<>();
            ArrayList<String> al = new ArrayList<>();
            while( keys.hasNext() )
            {
                String key = keys.next();
                JSONObject innerJObject = jObject.getJSONObject(key);
                Iterator<String> innerKeys = innerJObject.keys();
                while( innerKeys.hasNext() )
                {
                    String innerKkey = innerKeys.next();
                    String value = innerJObject.getString(innerKkey);
                    emp.put(innerKkey,value);
                }
                employee.add(emp);
                if(emp.get("Name").equals(name)){
                    String productivity =  emp.get("Productivity");
                    return productivity;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return  "";
    }
}