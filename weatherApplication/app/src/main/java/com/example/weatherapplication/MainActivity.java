package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText ;
    TextView textView;
    public void clickFunction (android.view.View view){
        DownloadTask task = new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() +"&appid=439d4b804bc8187953eb36d2a8c26a02");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
         String result = "";
         URL url;
         HttpURLConnection urlConnection = null;
         try {
             url = new URL(urls[0]);
             urlConnection = (HttpURLConnection) url.openConnection();
             InputStream in = urlConnection.getInputStream();
             InputStreamReader reader = new InputStreamReader(in);
             int data = reader.read();
             while (data != -1){
                 char current = (char) data;
                 result += current;
                 data = reader.read();
             }return result;

         } catch (Exception e){
             e.printStackTrace();
             Toast.makeText( getApplicationContext(), "Could not find weather for city that you may enter!", Toast.LENGTH_SHORT).show();
             return null;
         }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");

                JSONArray array = new JSONArray(weatherInfo);
                String message = "";
                for (int j = 0; j < array.length(); j++){
                    JSONObject jsonPart = array.getJSONObject(j);
                    String main =  jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                   if (!main.equals("") && !description.equals("")){
                       message = main + ":" + description;
                   }
                } if (!message.equals("")){
                    textView.setText(message);
                }else {
                    Toast.makeText( getApplicationContext(), "Could not find weather for city that you may enter!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find the weather for city that you may enter!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.resulttextView);


    }

}
