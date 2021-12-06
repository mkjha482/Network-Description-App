package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.*;
import java.*;

public class MainActivity extends AppCompatActivity {

    Button chk;               //button to check for connectivity
    AlertDialog dialog;       //Alert box to take the url input
    LinearLayout layout;
    String net;               //variable for net-connectivity
    String ur;                //variable for URL entered by the user
    String avail;             //to check URL validity
    String free;
    int sr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chk = findViewById(R.id.check);
        layout = findViewById(R.id.contain);

        buildDialog();

        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }


    public String checkConnection()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null!=activeNetwork){
            if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI){
                 net = "Connected to Wi-Fi";
            }

            else if(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                 net = "Connected to Mobile Data";
            }

        }

        else {
            net = "No Internet Connection!";
        }
        return net;
    }

    private void buildDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog,null);

        EditText url = view.findViewById(R.id.urlEdit);

        builder.setView(view);
        builder.setTitle("Enter URL to ping").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ur = url.getText().toString();
                try {
                    addCard(ur);
                }catch(Exception e){
                    free = "Nothing";
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog = builder.create();
    }

    private void addCard(String str) throws Exception
    {
        View view = getLayoutInflater().inflate(R.layout.card,null);

        TextView urlView = view.findViewById(R.id.url);
        Button change = view.findViewById(R.id.remove);


        urlView.setText(str +"\n"+ checkConnection() + "\n" + Valid(str) + "\n" + ping(str) );

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }
    public String Valid(String av){
        if(urlValid(av)){
            avail = "Valid URL";
        }
        else avail = "Invalid URL";

        return avail;
    }

    public static boolean urlValid(String lr){
        try {
            new URL(lr).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public String ping(String lt) throws IOException{

        String msg="";           //ping URL message

        try {
            URL urs = new URL(lt);
            HttpURLConnection connection = (HttpURLConnection) urs.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.connect();

            int code = connection.getResponseCode();

            sr = code;

            if(sr == 200){
                msg= "Successful Connection";
            }
            else{
                msg = "CODE: " + Integer.toString(sr);
            }



        }catch(Exception e){
            msg = e.getMessage();
        }

        return msg;

    }



}