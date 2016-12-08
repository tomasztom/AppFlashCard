package com.chmc.project.flascard;

import java.util.ArrayList;


public class Word {

    protected int id;
    protected String name;
    protected String translation;
    protected String category;
    protected final String URL="https://google.pl";

    public String getURL(){
        return URL;
    }

    public  Word(){
        this(-1,null,null,null);
    }

    public Word(int id, String name, String translation){
        this(id,name,translation,null);
    }

    public Word(int id, String name, String translation, String category/*ArrayList<Word> translations*/){
        this.id = id;
        this.name = name;
        this.category = category;
        this.translation = translation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    boolean equals(Word word){
        if(word.getName().equals(name) && word.getTranslation().equals(translation) && word.getCategory().equals(category)){
            return true;
        }else{
            return false;
        }
    }
}
