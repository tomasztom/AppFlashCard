package com.chmc.project.flascard;


import java.util.ArrayList;
import java.util.Random;

public class ManagerDraw {
    private ArrayList<Word> listWords;
    private ArrayList<Word> tmpListWords;
    private int countWords;             // liczba zaczytanych słów
    private Random rand;
    private int currentIndex;           // index zaczytanego słowa
    private Word currentWord;           // aktualnie zaczytane słowo
    private int correctAnswer;

    public ManagerDraw(ArrayList<Word> listWords){
        this.listWords = listWords;
        tmpListWords = new ArrayList<>();
        correctAnswer = 0;
        countWords = listWords.size();
        rand = new Random(countWords);
    }

    public int randomIndex(){
        currentIndex = rand.nextInt(countWords);
        return getCurrentIndex();
    }

    public int getCountWords(){
        return countWords;
    }

    public boolean isCountWords(){
        if(countWords>0){
            return true;
        }else{
            return false;
        }
    }

    public int getCurrentIndex(){
        return currentIndex;
    }

    // Losuje nowe słowo
    public void randomWord(){
        if(isCountWords()){
            currentWord = listWords.get(randomIndex());
        }else{
            currentWord = null;
        }
    }

    // Zwraca nowo wylosowane słowo
    public Word getWord(){
        randomWord();
        return getCurrentWord();
    }

    // Zwraca aktualne słowo
    public Word getCurrentWord(){
        return currentWord;
    }

    // Sprawdzamy czy tłumaczenie jest prawidłowe
    public String checkTranslation(Word word){
        if(currentWord.equals(word)){           // sprawdzamy czy tlumaczenie zgadza się z aktualnie wylosowanym słowem
            tmpListWords.add(listWords.remove(currentIndex));   // usuwamy słowo z listy i dorzucamy do listy słów które już odgadliśmy
            countWords--;
            correctAnswer++;
            randomWord();   // losujemy nowe słowo
            return "Brawo.! Prawidłowa odpowiedź";
        }else {
            if(searchInList(word)){             // sprawdzmy czy tlumaczenie wystepuje na lisci słów
                tmpListWords.add(listWords.remove(currentIndex));  // usuwamy słowo z listy i dorzucamy do listy słów które już odgadliśmy
                countWords--;
                correctAnswer++;
                randomWord();   // losujemy nowe słowo;
                return "Brawo.! Prawidłowa odpowiedź";
            }else{
                if(searchInTmpList(word)){      // sprawdzamy czy tlumaczenie wysteuje na liscie słów które już odgadliśmy
                    return "Podaj inne tłumaczenie";
                }else{
                    countWords++;
                    correctAnswer=0;
                    randomWord();
                    return "Niestety.! :(";              // oznacza ze tłumaczenie jest błędne
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

    public String getURL(){
        return currentWord.getURL();
    }

    public String getTranslation(){
        return currentWord.getTranslation();
    }

    public int getCorrectAnswer(){
        return correctAnswer;
    }

}
