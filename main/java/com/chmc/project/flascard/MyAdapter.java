package com.chmc.project.flascard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Category> {

    private int coutChecked;

    Context context;
    int layoutResorceId;
    ArrayList<Category> list = null;

    public MyAdapter(Context c, int layoutResorceId, ArrayList<Category> list){
        super(c,layoutResorceId,list);
        this.context = c;
        this.layoutResorceId = layoutResorceId;
        this.list = list;
        coutChecked=0;
    }

    public static class Holder{
        CheckedTextView pole;
    }

    public View getView(int pos, View v, ViewGroup parent){
        View row = v;
        Holder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResorceId,parent,false);

            holder = new Holder();
            holder.pole = (CheckedTextView) row.findViewById(R.id.check);

            row.setTag(holder);
        }else{
            holder = (Holder)row.getTag();
        }
        Category category = list.get(pos);

        holder.pole.setText(category.getName());
        holder.pole.setChecked(category.isSelected());

        if(category.isSelected()) coutChecked++;    // zmienna odpowiadająca za inofmacje ile kategori jest zaznaczonych

        return row;

    }

    public int getCoutChecked(){
        return coutChecked;
    }

}