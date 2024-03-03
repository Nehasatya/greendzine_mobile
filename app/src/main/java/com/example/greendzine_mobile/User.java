package com.example.greendzine_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class User extends AppCompatActivity {

    ImageButton home, user;
    ListView l;
    ArrayList<HashMap<String,String>> employee = new ArrayList<>();
    EditText etSearch;
    ImageButton search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        home = (ImageButton) findViewById(R.id.home_btn);
        user = (ImageButton) findViewById(R.id.emp_btn);
        etSearch = (EditText) findViewById(R.id.etSearch);
        search_btn = (ImageButton) findViewById(R.id.search_btn);

        l = (ListView) findViewById(R.id.list);

//        Parsing emp.json to get details of all employee
        String json = loadJSONFromAsset(this, "emp.json");
        parseJson(employee,json);

//      Custom adapter for Listview
        MyAdapter adapter = new MyAdapter(employee);
        l.setAdapter(adapter);

//        Search Feature for searching employees
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(etSearch.getText().toString());
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(User.this, Dashboard.class);
                startActivity(i);

            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "In user",Toast.LENGTH_SHORT).show();
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
    public static void parseJson(ArrayList<HashMap<String,String>> employee, String jsonString) {
            try
            {
                JSONObject jObject= new JSONObject(jsonString).getJSONObject("employee");
                Iterator<String> keys = jObject.keys();
                while( keys.hasNext() )
                {
                    String key = keys.next();
                    JSONObject innerJObject = jObject.getJSONObject(key);
                    Iterator<String> innerKeys = innerJObject.keys();
                    HashMap<String,String> emp = new HashMap<>();
                    while( innerKeys.hasNext() )
                    {
                        String innerKkey = innerKeys.next();
                        String value = innerJObject.getString(innerKkey);
                        emp.put(innerKkey, value);
                    }
                    employee.add(emp);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
