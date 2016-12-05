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
    private int countWords;             // liczba zaczytanych słów
    private Random rand;
    private int currentIndex;           // index zaczytanego słowa
    private Word currentWord;           // aktualnie zaczytane słowo
    private TextView textViewCategory;
    private TextView textViewWord;
    private EditText editTextEnterTranslation;
    private TextView textViewCountWords;
    private TextView textViewCorrectAnswer;
    private boolean isWin, goSetting;

    private int setting;        // ustawienia losowania słow
    private int lang;           // zmienna odpowiadająca za informacje na temat czy zaczytane
                                // słowo jest PL czy EN

    private int correctAnswer;  // zmienna odpowiadajaca za liczbe poprawnych odpowiedzi z rzędu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        tmpListWords = new ArrayList<>();
        currentIndex = 0;
        isWin = false;
        managerDB = new ManagerDB(this);
        textViewWord = (TextView) findViewById(R.id.textViewWord);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        editTextEnterTranslation = (EditText) findViewById(R.id.editTextEnterTranslation);
        textViewCorrectAnswer = (TextView) findViewById(R.id.correctAnswer);
        textViewCountWords = (TextView) findViewById(R.id.countWord);
        lang=-1;
        correctAnswer=0;
        goSetting = false;

        loadData();

    }

    public void loadData(){
        try{
            listWords = managerDB.loadAllWordsOfCategory();
            setting = managerDB.loadSettingsRandom();
            countWords = listWords.size();
            rand = new Random();
        }catch (Exception e){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }


        randomWord();
        textViewCountWords.setText(getString(R.string.count_words)+" : "+countWords);
        textViewCorrectAnswer.setText("+"+correctAnswer);
    }


    public void onRandomWordClick(View view) {
        randomWord();
    }

    public void randomWord(){
        if(countWords>0){
            currentIndex = rand.nextInt(countWords);
            currentWord = listWords.get(currentIndex);

            switch(setting){
                // Losowanie randomowe Polskie lub Angielskie
                case -1:
                    int language = rand.nextInt(2);
                    if(language==0){
                        setTextView(currentWord.getTranslation(), currentWord.getCategory());
                        lang = 0;
                    }
                    else {
                        setTextView(currentWord.getName(), currentWord.getCategory());
                        lang = 1;
                    }
                    break;
                // Losowanie tylko EN słów
                case 0:
                    setTextView(currentWord.getTranslation(), currentWord.getCategory());
                    lang = 0;
                    break;
                // Losowanie tylko PL słów
                case 1:
                    setTextView(currentWord.getName(), currentWord.getCategory());
                    lang = 1;
                    break;
            }
        }else{
            goToFinishActivity();
        }

    }

    // Ustawienie wartosci wylosowanego słowa
    public void setTextView(String word, String category){
        textViewWord.setText(word);
        textViewCategory.setText(category);
        textViewCountWords.setText(getString(R.string.count_words)+" : "+countWords);
        textViewCorrectAnswer.setText("+"+correctAnswer);

    }

    public void onCheckTranslationClick(View view) {
        //String translation = editTextEnterTranslation.getText().toString();
        Word word = loadWord();
        if(currentWord.equals(word)){           // sprawdzamy czy tlumaczenie zgadza się z aktualnie wylosowanym słowem
            correctAnswer();
        }else {
            if(searchInList(word)){             // sprawdzmy czy tlumaczenie wystepuje na lisci słów
                correctAnswer();
            }else{
                if(searchInTmpList(word)){      // sprawdzamy czy tlumaczenie wysteuje na liscie słów które już odgadliśmy
                    changeTranslationComunikat();   // Prosimy o wpsisanie innego tłumaczenia
                }else{
                    wrongAnswer();              // oznacza ze tłumaczenie jest błędne
                }
            }
        }
    }

    public boolean searchInList(Word word){
        int tmpIndex = 0;
        for(Word w : listWords){
            if(w.equals(word)){
                currentIndex = tmpIndex;
                return true;
            }
            tmpIndex++;
        }
        return false;
    }

    public boolean searchInTmpList(Word word){
        for(Word w : tmpListWords){
            if(w.equals(word)){
                return true;
            }
        }
        return false;
    }

    public Word loadWord(){
        Word word = new Word();
        word.setCategory(textViewCategory.getText().toString());

        if(lang == 1){
            word.setName(textViewWord.getText().toString());
            word.setTranslation(editTextEnterTranslation.getText().toString());
            return word;
        }else{
            word.setName(editTextEnterTranslation.getText().toString());
            word.setTranslation(textViewWord.getText().toString());
            return word;
        }
    }

    public String getCurrentTranslation(){
        if(lang == 1) return currentWord.getTranslation().toString();
        else return currentWord.getName().toString();
    }

    public void correctAnswer(){
        clear();
        Toast.makeText(getApplicationContext(),"Brawo.! Prawidłowa odpowiedź.",Toast.LENGTH_SHORT).show();
        tmpListWords.add(listWords.remove(currentIndex));
        countWords = listWords.size();
        if(countWords>0){
            correctAnswer++;
            randomWord();
        }else{
            goToFinishActivity();
        }

    }

    public void goToFinishActivity(){
        isWin = true;
        Intent intent = new Intent(getApplicationContext(),FinishActivity.class);
        startActivity(intent);
    }

    public void wrongAnswer(){
        clear();
        Toast.makeText(getApplicationContext(),"Ojj niestety :( ... ",Toast.LENGTH_SHORT).show();
        listWords.add(currentWord);
        countWords = listWords.size();
        correctAnswer=0;
        randomWord();
    }

    public void changeTranslationComunikat(){
        clear();
        Toast.makeText(getApplicationContext(),"Podaj inne tłumaczenie",Toast.LENGTH_LONG).show();
    }

    public void clear(){
        textViewWord.setText("");
        textViewCategory.setText("");
        editTextEnterTranslation.setText("");
    }

    public void onShowTranslation(View view) {
        editTextEnterTranslation.setText(getCurrentTranslation());
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
        String url;
        if(lang==1){
            url = URL_PL+currentWord.getName();
        }else{
            url = URL_EN+currentWord.getTranslation();
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
