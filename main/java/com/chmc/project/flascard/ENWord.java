package com.chmc.project.flascard;

public class ENWord extends Word{
    private final String URL = "https://translate.google.pl/m/translate#en/pl/";

    public ENWord(){
        super();
    }

    public ENWord(int id, String name, String translation, String category){
        super(id,name,translation,category);
    }

    @Override
    public String getURL(){
        return URL + getName();
    }

}
