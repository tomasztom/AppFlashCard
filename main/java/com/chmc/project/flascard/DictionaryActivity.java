package com.chmc.project.flascard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DictionaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
    }

    public void onEditWordClick(View view) {
        Intent intent = new Intent(this,EditWordActivity.class);
        startActivity(intent);
    }

    public void onEditCategoryClick(View view) {
        Intent intent = new Intent(this,EditCategoryActivity.class);
        startActivity(intent);
    }
}
