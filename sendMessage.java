package com.example.abhinay.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class sendMessage extends AppCompatActivity implements View.OnClickListener{



    private static final String REGISTER_URL = "http://172.28.42.29/uploading/message.php";

    public static final String KEY_MESSAGE = "message";


    private EditText etmessage;
    private Button btsend;

//    private Button buttonlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        etmessage = (EditText) findViewById(R.id.etmessage);

        btsend = (Button) findViewById(R.id.btsend);


        btsend.setOnClickListener(this);
    }

    private void sendMsg(){
        final String message = etmessage.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(sendMessage.this,response,Toast.LENGTH_LONG).show();

                        if(response.trim().equals("message sent")) {
                            openOptions();
                            finish();
                        }
                        else{
                            btsend.setEnabled(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(sendMessage.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_MESSAGE,message);
                params.put(MainActivity.KEY_USERNAME,MainActivity.username);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void openOptions(){
        Intent intent=new Intent(this,Options.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        btsend.setEnabled(false);
        sendMsg();


    }
}




