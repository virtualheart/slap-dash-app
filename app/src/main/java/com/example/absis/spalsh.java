package com.example.absis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class spalsh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                startActivity(new Intent(spalsh.this,MainActivity.class));
                finish();
            }
        },4000);  // every 1000 = 1 sec(currently 5 sec)
    }
}
