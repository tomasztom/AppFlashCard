package com.chmc.project.flascard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class SettingsAcvtivity extends AppCompatActivity {
    private ArrayList<Category> listCategorySettings;
    private ManagerDB managerDB;
    private int setting;        // ustawienia losowania słow
    private ListView listView;
    private ArrayList<Category> categoryList;
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_acvtiviy);

        managerDB = new ManagerDB(this);
        listView = (ListView) findViewById(R.id.listViewSettings);


        loadData();
        setRadioButton();
        adapter = new MyAdapter(this,R.layout.element_settings,listCategorySettings);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Category tmp;
                CheckedTextView ctv = (CheckedTextView)view;
                if(ctv.isChecked()){
                    tmp = listCategorySettings.get(position);
                    Toast.makeText(getApplicationContext(),"" + tmp.getName(),Toast.LENGTH_SHORT).show();
                   tmp.setSelected(false);
                    ctv.setChecked(false);
                }else{
                    tmp = listCategorySettings.get(position);
                    Toast.makeText(getApplicationContext(),"" + tmp.getName(),Toast.LENGTH_SHORT).show();
                    tmp.setSelected(true);
                    ctv.setChecked(true);
                }
            }


        });


    }



    public void setRadioButton(){
        RadioButton r;

        switch (setting){
            case 1:
                r = (RadioButton) findViewById(R.id.radio_pl);
                r.setChecked(true);
                break;
            case 0:
                r = (RadioButton) findViewById(R.id.radio_en);
                r.setChecked(true);
                break;
            case -1:
                r = (RadioButton) findViewById(R.id.radio_pl_en);
                r.setChecked(true);
                break;
        }
    }

    public void loadData() {
        try {
            listCategorySettings = managerDB.getCategoriesSettings();
            setting = managerDB.loadSettingsRandom();
        } catch (Exception e) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void onSaveSettings(View view) {
        ArrayList<Word> listTmp = null;
            try{
                managerDB.updateSettingsCategory(listCategorySettings);
                managerDB.updateSettingsRandom(setting);


                listTmp = managerDB.loadAllWordsOfCategory();
                if(listTmp==null || listTmp.isEmpty()) {
                    for(Category w : listCategorySettings) {
                        w.setSelected(true);
                    }
                    managerDB.updateSettingsCategory(listCategorySettings);
                    Toast.makeText(getApplicationContext(),getString(R.string.must_check_category),Toast.LENGTH_LONG).show();
                    adapter = new MyAdapter(this,R.layout.element_settings,listCategorySettings);
                    listView.setAdapter(adapter);
                }else {
                    Intent intent = new Intent(this,LearningActivity.class);
                    startActivity(intent);
                }


            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Uppss",Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    public void onRadioButtonClicked(View view) {

        switch(view.getId()){
            case R.id.radio_pl:
                setting = 1;
                break;
            case R.id.radio_en:
                setting = 0;
                break;
            case R.id.radio_pl_en:
                setting = -1;
                break;
        }
    }
}