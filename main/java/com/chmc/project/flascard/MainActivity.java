package com.chmc.project.flascard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onDictionaryClic(View view) {
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);
    }

    public void onStartLearningClick(View view) {
        ManagerDB managerDB = new ManagerDB(this);
        if(managerDB.dictonaryIsEmpty()){
            Toast.makeText(getApplicationContext(), "Słownik jest pusty", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Intent intent = new Intent(this, LearningActivity.class);
            startActivity(intent);
         //   finish();
        }
    }
}
