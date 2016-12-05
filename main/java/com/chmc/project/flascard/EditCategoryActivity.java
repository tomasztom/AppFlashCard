package com.chmc.project.flascard;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class EditCategoryActivity extends AppCompatActivity {


    private ArrayAdapter<String> elementsSpinner;
    private ArrayList<String> categories;
    private ManagerDB managerDB;
    private Spinner spinner;
    private int posCategoryToDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        managerDB = new ManagerDB(this);
        loadData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                    posCategoryToDelete = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

        }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void loadData() {
        Button button = (Button)findViewById(R.id.buttonDeleteCategory);
        categories = managerDB.getCategories();
        if(categories.isEmpty()){
            button.setEnabled(false);
        }else {
            button.setEnabled(true);
        }
            elementsSpinner = new ArrayAdapter<String>(this,R.layout.element_layout,categories);
            spinner = (Spinner) findViewById(R.id.spinnerCategoriesInEditCat);
            spinner.setAdapter(elementsSpinner);
    }

    public void onDeleteCategoryClick(View view) {
            String name = categories.get(posCategoryToDelete);
            try{
                managerDB.deleteCategory(categories.get(posCategoryToDelete));
            }catch (Exception e) {
                Toast.makeText(this, "Nie udalo sie usunac", Toast.LENGTH_LONG).show();
            }finally {
                // spinner.setAdapter(null);
                 loadData();
            }


    }

    public void onAddCategoryClick(View view) {
        EditText text = (EditText) findViewById(R.id.editTextCategory);
        String name = text.getText().toString();
        if(managerDB.isExistCategory(name)){
            Toast toast = new Toast(this);
            //toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);

            toast.makeText(this,getString(R.string.category_exist),Toast.LENGTH_LONG).show();
            text.setText("");
        }else{
            managerDB.addCategory(name);
            text.setText("");
            loadData();
        }

    }

    public void onLearning(View view) {
        ManagerDB managerDB = new ManagerDB(this);
        if(managerDB.dictonaryIsEmpty()){
            Toast.makeText(getApplicationContext(), "Słownik jest pusty", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Intent intent = new Intent(getApplicationContext(), LearningActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}

