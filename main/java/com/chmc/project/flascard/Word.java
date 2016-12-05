package com.chmc.project.flascard;

import java.util.ArrayList;

/**
 * Created by ola on 2016-12-02.
 */
public class Word {

    private int id;
    private String name;
    private String translation;
    private String category;
    //private ArrayList<Word> translations;


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
