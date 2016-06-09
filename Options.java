package com.example.abhinay.loginapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }
    public void sendImage(View view) {
        Intent intent = new Intent(this, sendImage.class);
        startActivity(intent);

    }

    public void sendMsg(View view) {
        Intent intent = new Intent(this, sendMessage.class);
        startActivity(intent);

    }
    public void medDispense(View view)
    {
        Intent intent =new Intent(this,medicineDispenser.class);
        startActivity(intent);

    }
    public  void logout(View view){
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.putBoolean("hasLoggedIn",false);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
