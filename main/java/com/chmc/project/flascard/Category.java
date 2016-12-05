package com.chmc.project.flascard;


 class Category {
    String name;
    boolean selected;

    Category(){

    }

    Category(String name, boolean selected){
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
