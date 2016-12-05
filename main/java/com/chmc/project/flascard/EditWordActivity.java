package com.chmc.project.flascard;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditWordActivity extends AppCompatActivity {
    private ManagerDB managerDB;
    private ArrayList<String> elementsList;
    private ArrayAdapter<String> elementsAdapter;
    private ArrayList<Word> listWords;
    private String categoryName;
    private Word word;
    private int indexList;

    private EditText editTextWord, editTextWord2;
    private EditText editTextTranslation, editTextTranslation2;
    private Spinner spinnerCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        editTextWord = (EditText) findViewById(R.id.editTextWord);
        editTextTranslation = (EditText) findViewById(R.id.editTextTranslation);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategoriesInEditWord);
        managerDB = new ManagerDB(this);
        listWords = new ArrayList<Word>();
        indexList = -1;

        loadData();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryName = elementsList.get(position);  // przechowujemy aktualnie wybraną kategorię
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadData() {
        elementsList = managerDB.getCategories();
        elementsList.add(0,getString(R.string.general));

        elementsAdapter = new ArrayAdapter<String>(this,R.layout.element_layout,elementsList);
        spinnerCategory.setAdapter(elementsAdapter);
    }


    public void clear(){
        editTextWord.setText(null);
        editTextTranslation.setText(null);
        changeButton(R.id.buttonDeleteWord,false);
        changeButton(R.id.buttonNextTranslation,false);
    }

    public void onSaveWordClick(View view) {

        Word word;
        String name, translate;


        // pobieramy wartości pól
        name = ((EditText) findViewById(R.id.editTextWord)).getText().toString();
        translate = ((EditText) findViewById(R.id.editTextTranslation)).getText().toString();

        if(name.equals("") || translate.equals("")){
            Toast.makeText(this,getString(R.string.text_toast_1),Toast.LENGTH_LONG).show();
        }else{
            // Tworzymy obiekty typu Word
            word = new Word();
            word.setName(name);
            word.setTranslation(translate);
            word.setCategory(categoryName);

            try {
                // Zapisujemy do bazy
                // Robimi tak po to by zawsze w pierwszej kolumnie miec słowa polskie, a w 2 tłumaczenie
                // ułatwi mi to chyba troche zadanie.
                managerDB.addWords(word);
            }catch (Exception e){
                Toast.makeText(this,getString(R.string.text_toast_2), Toast.LENGTH_LONG).show();
            }finally {
                clear();
            }
        }
    }

    public void changeButton(int id, boolean is){
        ((Button) findViewById(id)).setEnabled(is);
    }

    // aktualnie niepotrzebne, stworzone po to by zobaczyc czy dodaja nam sie wpisy do bazy danych
    public void onSearchWordClick(View view) {

        Cursor cursor;// = managerDB.search();
        String tekst = "";

        String name = ((EditText) findViewById(R.id.editTextWord)).getText().toString();

        try {
            listWords = managerDB.searchWordWithoutCategory(name);

            if(listWords != null && listWords.size()>0){
                indexList=-1;
                setTextViewTranslation();
                changeButton(R.id.buttonDeleteWord,true);
            }else{
                Toast.makeText(this,getString(R.string.dont_find_word),Toast.LENGTH_LONG).show();
                changeButton(R.id.buttonDeleteWord,false);
            }
        }catch (Exception e){
            Toast.makeText(this,getString(R.string.text_toast_2),Toast.LENGTH_LONG).show();
        }finally {
            //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            //startActivity(intent);
        }
    }


    public void setTextViewTranslation(){

        String word = editTextWord.getText().toString();
        indexList++;
        if(listWords!=null && listWords.size()>indexList) {
            ((Button) findViewById(R.id.buttonDeleteWord)).setEnabled(true);
            String translate = listWords.get(indexList).getTranslation();

            if(word.equals(translate)){
                editTextTranslation.setText(listWords.get(indexList).getName());
            }else{
                editTextTranslation.setText(translate);
            }
            if (listWords.size()-1 == indexList) {
                changeButton(R.id.buttonNextTranslation,false);
            }else{
                changeButton(R.id.buttonNextTranslation,true);
            }
        }
    }

    public void onNextTranslatonClick(View view) {
        setTextViewTranslation();
    }

    public void onDeleteWordClick(View view) {
        try{
        managerDB.deleteWord(listWords.get(indexList));
        }catch(Exception e){
            Toast.makeText(this,getString(R.string.text_toast_2),Toast.LENGTH_SHORT).show();
        }finally {
            clear();
        }
    }

    public void onStartLearning(View view) {
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
