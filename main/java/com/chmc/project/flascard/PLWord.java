package com.chmc.project.flascard;

public class PLWord extends Word {

    private final String URL = "https://translate.google.pl/m/translate#pl/en/";

    public PLWord(){
        super();
    }

    public PLWord(int id, String name, String translation, String category){
        super(id,name,translation,category);
    }

    @Override
    public String getURL(){
        return URL+getName();
    }
}
