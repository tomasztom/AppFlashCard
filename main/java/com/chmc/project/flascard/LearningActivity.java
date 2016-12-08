package com.chmc.project.flascard;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class LearningActivity extends AppCompatActivity {

    private static String URL_EN = "https://translate.google.pl/m/translate#en/pl/";
    private static String URL_PL = "https://translate.google.pl/m/translate#pl/en/";
    private ManagerDB managerDB;
    private ArrayList<Word> listWords;
    private ArrayList<Word> tmpListWords;
    private TextView textViewCategory;
    private TextView textViewWord;
    private EditText editTextEnterTranslation;
    private TextView textViewCountWords;
    private TextView textViewCorrectAnswer;
    private boolean isWin, goSetting;
    private ManagerDraw managerDraw;

    private int setting;        // ustawienia losowania słow
    private int lang;           // zmienna odpowiadająca za informacje na temat czy zaczytane
                                // słowo jest PL czy EN

    private int correctAnswer;  // zmienna odpowiadajaca za liczbe poprawnych odpowiedzi z rzędu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        textViewWord = (TextView) findViewById(R.id.textViewWord);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        editTextEnterTranslation = (EditText) findViewById(R.id.editTextEnterTranslation);
        textViewCorrectAnswer = (TextView) findViewById(R.id.correctAnswer);
        textViewCountWords = (TextView) findViewById(R.id.countWord);

        managerDB = new ManagerDB(this);
        loadData();
        managerDraw = new ManagerDraw(listWords);
        randomWord();
    }


    public void loadData(){
        try{
            listWords = managerDB.loadAllWordsOfCategories();
        }catch (Exception e){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }


    public void onRandomWordClick(View view) {
        randomWord();
    }

    public void randomWord(){
            setTextView(managerDraw.getWord());
    }

    // Ustawienie wartosci wylosowanego słowa
    public void setTextView(Word word){
        if(word == null){
            goToFinishActivity();
        }else{
            textViewWord.setText(word.getName());
            textViewCategory.setText(word.getCategory());
            editTextEnterTranslation.setText("");
            textViewCountWords.setText(getString(R.string.count_words)+" : "+ managerDraw.getCountWords());
            textViewCorrectAnswer.setText("+"+managerDraw.getCorrectAnswer());
        }

    }

    public void onCheckTranslationClick(View view) {
        Toast.makeText(getApplicationContext(),managerDraw.checkTranslation(loadWord()),Toast.LENGTH_SHORT).show();
        setTextView(managerDraw.getWord());
    }

    // Funkcja zwraca nam słowo utworzone z wartości znajdujących się w polach TextView oraz EditText
    public Word loadWord(){
        Word word = new Word();
        word.setCategory(textViewCategory.getText().toString());
        word.setName(textViewWord.getText().toString());
        word.setTranslation(editTextEnterTranslation.getText().toString());
        return word;
    }


    public void goToFinishActivity(){
        isWin = true;
        Intent intent = new Intent(getApplicationContext(),FinishActivity.class);
        startActivity(intent);
    }

    public void onShowTranslation(View view) {
        editTextEnterTranslation.setText(managerDraw.getTranslation());
    }

    @Override
    public void onPause(){
        super.onPause();
        if(isWin || goSetting ) finish();
    }

    public void onSettingsClick(View view) {
        goSetting = true;
        Intent intent = new Intent(getApplicationContext(),SettingsAcvtivity.class);
        startActivity(intent);
    }

    public void onGoToGoogleTranslateClick(View view) {
        Uri uri = Uri.parse(managerDraw.getURL());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
