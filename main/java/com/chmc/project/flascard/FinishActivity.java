package com.chmc.project.flascard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

//    public void onEndClick(View view) {
//        Intent intent = new Intent(getApplication(),MainActivity.class);
//        intent.putExtra("isFinisz",true);
//        startActivity(intent);
//        System.exit(0);
//    }

    public void onTryAgainClick(View view) {

        Intent intent = new Intent(getApplication(),LearningActivity.class);
        startActivity(intent);

    }
}
