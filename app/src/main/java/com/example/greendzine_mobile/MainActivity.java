package com.example.greendzine_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button b1;
    EditText ed1,ed2;

    String name="",password="";

    ArrayList<HashMap<String, String>> employee = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button1);
        ed1 = (EditText) findViewById(R.id.editText1);
        ed2 = (EditText) findViewById(R.id.editText2);
        

        String json = loadJSONFromAsset(this,"emp.json");
        parseJson(employee,json);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ed1.getText().toString();
                password = ed2.getText().toString();
                if(authenticate_user(name, password,employee)) {
//                    On successful authentication, Redirecing to DashBoard
                    Intent i = new Intent(MainActivity.this, Dashboard.class);
                    i.putExtra("name", name);
                    startActivity(i);
                }else {
//                    Displaying Wrong credentials
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private static boolean authenticate_user(String name, String password, ArrayList<HashMap<String,String>> employee) {
//                Checking name and password from the emp.json
        Boolean bool_name = false, bool_password = false;

        if (!name.isEmpty() && !password.isEmpty()) {
            System.out.println(name+" "+password);
            for(int i=0;i<employee.size();i++){
                bool_name = false;
                bool_password = false;
                for (Map.Entry<String, String> entry : employee.get(i).entrySet()) {
                    if (entry.getKey().equals("Name") && entry.getValue().equals(name)) {
                        bool_name = true;
                    }
                    if (entry.getKey().equals("Password") && entry.getValue().equals(password)) {
                        bool_password = true;
                    }
                    if (bool_name && bool_password) {
                        break;
                    }
                }

//                On successful authentication
                if (bool_name && bool_password) {
                    return true;
                }
            }
        }
    return false;
    }
    public static String loadJSONFromAsset(Context context, String fileName) {
//        reading emp.json
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            System.out.println("JSON HERE LOOK "+json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static void parseJson(ArrayList<HashMap<String,String>> employee, String jsonString) {
//        Parsing emp.json
        try
        {
            JSONObject jObject= new JSONObject(jsonString).getJSONObject("employee");
            Iterator<String> keys = jObject.keys();
            HashMap<String,String> emp = new HashMap<>();
            while( keys.hasNext() )
            {
                emp = new HashMap<>();
                String key = keys.next();
                JSONObject innerJObject = jObject.getJSONObject(key);
                Iterator<String> innerKeys = innerJObject.keys();
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