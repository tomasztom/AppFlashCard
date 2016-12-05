package com.chmc.project.flascard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created by tomek on 2016-12-02.
 */
public class ManagerDB extends SQLiteOpenHelper{

    private SQLiteDatabase databaseWrite;
    private SQLiteDatabase databaseRead;
    // Kolumny w tabeli słowa
    private final String COLUMN_WORD_NAME = "name";
    private final String COLUMN_WORD_TRANSLATION = "translation";

    // Kolumny w tabeli kategorie
    private final String COLUMN_CATEGORY_NAME = "name";

    // Kolumny w tabeli tłumaczenia oraz w tabeli słowo-kategoria i ustawienia
    private final String COLUMN_ID_WORD = "id_word";                    // tłumaczenia, słowo-kategoria,
    private final String COLUMN_ID_CATEGORY = "id_category";            // słowo-kategoria,
    private final String COLUMN_IS_CHECK = "is_check";                  // ustawienia
    private final String COLUMN_SETTING_ID = "setting_id";


    private final String TABLE_WORDS = "words";                          // tabela słowa
    private final String TABLE_CATEGORIES = "categories";                 // tabela kategoria
    private final String TABLE_WORD_CATEGORY = "word_category";         // tabela słowo-kategoria
    private final String TABLE_SETTINGS = "settings";                   // tabela ustawienia


    public ManagerDB(Context context){
        super(context, "dictionary.db", null, 1);
        databaseRead = getReadableDatabase();
        databaseWrite = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /* ---  Tworzenie tabel jeśli jest to pierwsze uruchomienie naszej aplikacji i zakładamy bazę danych   --- */
        db.execSQL(
                "CREATE TABLE " + TABLE_WORDS + "(" +
                        "id integer primary key autoincrement," +
                        COLUMN_WORD_NAME + " text not null," +
                        COLUMN_WORD_TRANSLATION + " text not null);");

        db.execSQL(
                "CREATE TABLE " + TABLE_CATEGORIES + "(" +
                        "id integer primary key autoincrement," +
                        COLUMN_CATEGORY_NAME + " text not null," +
                        COLUMN_IS_CHECK + " integer not null);");


        db.execSQL(
                "CREATE TABLE " + TABLE_WORD_CATEGORY + "(" +
                        "id integer primary key autoincrement," +
                        COLUMN_ID_WORD + " integer not null," +
                        COLUMN_ID_CATEGORY + " integer not null);"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_SETTINGS + " (" +
                        "id integer primary key autoincrement," +
                        COLUMN_SETTING_ID + " integer not null," +
                        COLUMN_IS_CHECK + " integer nor null);"
        );
        /* ---------------------------------------------------------------------------------------------------------- */

        /* --- Dodanie wpisu "Ogólne" do tabeli Kategorie --- */
        //addCategory("ogólna");
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, "ogólna");
        values.put(COLUMN_IS_CHECK,1);
        db.insert(TABLE_CATEGORIES,null,values);


        /* --- Dodanie wpisu {-1, -1} do tabeli Ustawienia - wpis ten będzie odpowiadał za --- */
        /* --- rodzaj losowania czyli. 1 - Tylko Polskie, 0 - Tylko Angielskie, -1 - PL i EN --- */
        values = new ContentValues();
        values.put(COLUMN_SETTING_ID,-1);
        values.put(COLUMN_IS_CHECK,-1);
        db.insert(TABLE_SETTINGS,null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getIdWord(Word word){
        Cursor cursor = databaseRead.rawQuery("SELECT id FROM " + TABLE_WORDS +
                " WHERE " + COLUMN_WORD_NAME + " = '" + word.getName() +
                "' AND " + COLUMN_WORD_TRANSLATION + " = '" + word.getTranslation() + "'",null);

        if(cursor.moveToNext()) return cursor.getInt(0);
        return 0;
    }

    public void deleteWord(Word word){
        String args[] = {word.getName(),word.getTranslation()};
        databaseWrite.delete(TABLE_WORDS,COLUMN_WORD_NAME+"=? AND "+COLUMN_WORD_TRANSLATION+"=?",args);
    }

    public boolean dictonaryIsEmpty(){
        String query = "SELECT COUNT(*) FROM " + TABLE_WORDS;
        Cursor cursor = databaseRead.rawQuery(query,null);
        cursor.moveToNext();
        if(cursor.getInt(0)>0){
            return false;
        }
        return true;
    }

//    public  getAllTranslations(){
//        String query = "SELECT COUNT(*) FROM " + TABLE_TRANSLATIONS;
//        Cursor cursor = databaseRead.rawQuery(query,null);
//
//        cursor.moveToNext();
//        return cursor.getInt(0);
//    }


    public void addWord(Word word){
        if(getIdWord(word)>0){                  // jezeli istnieje nic nie robimy
        }else{                                   // wstawiamy slowo do bazydanych
            ContentValues values = new ContentValues();
            values.put(COLUMN_WORD_NAME,word.getName());
            values.put(COLUMN_WORD_TRANSLATION,word.getTranslation());
            databaseWrite.insertOrThrow(TABLE_WORDS,null,values);
        }
    }


//    public ArrayList<Word> getTranslations(int idWord, String categoryName){
//
//        ArrayList<Word> list = new ArrayList<Word>();
//
//        String ss = "SELECT DISTINCT ww.id, ww."+COLUMN_WORD_NAME+ ", ww."+COLUMN_WORD_TYPE+
//                " FROM "+TABLE_WORD+" as w, "+TABLE_CATEGORY+" as c, "+TABLE_WORD_CATEGORY+" as wc, "+TABLE_WORD+" as ww, "+TABLE_TRANSLATIONS+" as t, "+TABLE_TRANSLATIONS+" tt " +
//                " WHERE" +
//                " c."+COLUMN_CATEGORY_NAME+" = '" + categoryName + "'" +
//                " AND c.id = wc."+COLUMN_ID_CATEGORY+
//                " AND " + idWord +" = wc."+COLUMN_ID_WORD+
//                " AND ((" + idWord +" = t."+COLUMN_ID_WORD +
//                " AND t."+COLUMN_ID_TRANSLATION+" = ww.id)" +
//                " OR (" + idWord +" = tt."+COLUMN_ID_TRANSLATION +
//                " AND tt."+COLUMN_ID_WORD+" = " + idWord +"))";
//
//        Cursor cursor = databaseRead.rawQuery(ss,null);
//
//        while (cursor.moveToNext()){
//            list.add(createWord(cursor));
//        }
//        return list;
//    }


//    public Word createWord(Cursor cursor){
//        word = new Word();
//        word.setId(cursor.getInt(0));
//        word.setName(cursor.getString(1));
//        if(cursor.getType(2)==0) word.setType(false);
//        else word.setType(true);
//        return word;
//    }

//    public ArrayList<Word> searchWord(String name){
//        ArrayList<Word> list = new ArrayList<Word>();
//        Cursor cursor;
//
//        String query = "SELECT DISTINCT w.id, w." + COLUMN_WORD_NAME + ", w." + COLUMN_WORD_TRANSLATION + ", c." + COLUMN_CATEGORY_NAME +
//                " FROM " + TABLE_WORDS + " AS w, " + TABLE_CATEGORIES + " AS c, " + TABLE_WORD_CATEGORY + " AS t " +
//                "WHERE w." + COLUMN_WORD_NAME + " = '" + name + "'" +
//                " AND w.id = t." + COLUMN_ID_WORD +
//                " AND t." + COLUMN_ID_CATEGORY + " = c.id";
//
//        cursor = databaseRead.rawQuery(query,null);
//
//        while (cursor.moveToNext()){
//            list.add(
//                    new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
//            );
//        }
//        return list;
//    }

    public ArrayList<Word> searchWordWithoutCategory(String name){
        ArrayList<Word> list = new ArrayList<Word>();
        Cursor cursor;

        String query = "SELECT DISTINCT * " +
                " FROM " + TABLE_WORDS +
                " WHERE " + COLUMN_WORD_NAME + " = '" + name + "'" +
                " OR " + COLUMN_WORD_TRANSLATION + " = '" + name + "'";

        cursor = databaseRead.rawQuery(query,null);

        while (cursor.moveToNext()){
            list.add(
                    new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
            );
        }
        return list;
    }


    // Metoda dodająca pary słowo-tłumaczenie
    // Wpierw dodaje pojedynczo slowa do tabeli WORDS
    // Następnie dodaje zależność między tymi słowami do tabeli TRANSLATION
    // Oraz dodaje zależności między tymi słowami a kategorią do której należą
    public void addWords(Word word){
        int idWord, idTranslation, idCategory;

        addWord(word);                                  //  dodajemy słowo
        idWord = getIdWord(word);                       // pobieramy jego id bedzie potrzebne


        idCategory = getCategoryId(word.getCategory());           // pobieramy id categorii

        addLinkWordCategory(idWord,idCategory);         // wstawiamy powiazanie miedzy slowem a kategorią

    }

    public void addLinkWordCategory(int idWord, int idCategory){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_WORD,idWord);
        values.put(COLUMN_ID_CATEGORY,idCategory);
        databaseWrite.insert(TABLE_WORD_CATEGORY,null,values);
    }




    // Metoda zwraca nam id categorii którą podaliśmy jako argument
    public int getCategoryId(String name){
        Cursor cursor = databaseRead.rawQuery("SELECT id FROM " + TABLE_CATEGORIES +
                " WHERE " + COLUMN_CATEGORY_NAME + " = '" + name + "'", null);
        cursor.moveToNext();

        return cursor.getInt(0);
    }

    // Metoda zwracająca nam wszystkie kategorie za wyjątkiem kategori "Ogólnej"
    // Zrobione tak by kategoria ta nie wyświetlała się w liście rozwijanej w module "Edycja Kategorii"
    // Dzięki temu nie można jej usunąć z bazy
    public ArrayList<String> getCategories(){
        Category category;
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = databaseRead.rawQuery("SELECT * FROM categories ORDER BY name",null);

        while(cursor.moveToNext()){
            if(!cursor.getString(1).equals("ogólna")){
                list.add(cursor.getString(1));
            }
        }
        return list;
    }

    // Metoda sprawdzająca czy podana kategoria już istnieje w bazie danych
    public boolean isExistCategory(String name){
       Cursor cursor = databaseRead.rawQuery("SELECT COUNT(*) FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_NAME + " = '" + name + "'", null);
        cursor.moveToNext();
        int i = cursor.getInt(0);
        if(i>0){
            return true;
        }else{
            return false;
        }
    }

    // Metoda dodająca kategorię do bazy, oraz wpis do tabeli ustawienia z id naszej kategorii
    public void addCategory(String name){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME,name);
        values.put(COLUMN_IS_CHECK,true);
        databaseWrite.insertOrThrow(TABLE_CATEGORIES, null, values);
    }

    // Metoda usuwająca kategorie (wszystkie slowa z nią powiązane) oraz usuwajaca wpis z tabeli ustawienia dotyczacy naszej kategorii
    public void deleteCategory(String name){
        String[] args = {name};
        String[] args2 = {String.valueOf(getCategoryId(name))};     // pobranie id kategori
        databaseRead.rawQuery("DELETE FROM " + TABLE_WORDS + " WHERE " +
                "id = (SELECT wc." + COLUMN_ID_WORD + " FROM " + TABLE_CATEGORIES + " AS c, " + TABLE_WORD_CATEGORY + " AS wc " +
                "WHERE c.id = wc." + COLUMN_ID_CATEGORY + " AND c." + COLUMN_CATEGORY_NAME + " = '" + name + "')",null);
        databaseWrite.delete(TABLE_CATEGORIES,COLUMN_CATEGORY_NAME + "=?",args);      // usuniecie kategorii
      //  databaseWrite.delete(TABLE_SETTINGS,COLUMN_ID_CATEGORY + "=?",args2);       // usuniecie ustawien z nia zwiazanych
    }

    // Dodanie ukategori do ustawien
    public void addSetting(int idCategory){
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTING_ID,idCategory);
        values.put(COLUMN_IS_CHECK,1);
        databaseWrite.insertOrThrow(TABLE_SETTINGS,null,values);
    }

    public ArrayList<Word> loadAllWordsOfCategory(){
        ArrayList<Word> list = new ArrayList<Word>();
        Word word;
        String query = "SELECT w.id, w." + COLUMN_WORD_NAME + ", w." + COLUMN_WORD_TRANSLATION + ", c." + COLUMN_CATEGORY_NAME +" FROM " +
                TABLE_WORDS +" AS w , " +
                TABLE_CATEGORIES + " AS c, "+
                TABLE_WORD_CATEGORY + " AS wc " +
                "WHERE c." + COLUMN_IS_CHECK + " = 1 " +
                "AND wc." + COLUMN_ID_CATEGORY + " = c.id " +
                "AND w.id = wc." + COLUMN_ID_WORD ;

        Cursor cursor = databaseRead.rawQuery(query,null);

        while(cursor.moveToNext()){
            word = new Word(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );

            list.add(word);
        }
        return list;
    }

    public ArrayList<Category> getCategoriesSettings(){
        ArrayList<Category> list = new ArrayList<>();
        Category category;
        boolean isSelected;

        String query = "SELECT " + COLUMN_CATEGORY_NAME + "," + COLUMN_IS_CHECK + " FROM " + TABLE_CATEGORIES;
        Cursor cursor = databaseRead.rawQuery(query,null);

        while(cursor.moveToNext()){
            category = new Category();
            category.setName(cursor.getString(0));
            if(cursor.getInt(1)==1){
                category.setSelected(true);
            }else{
                category.setSelected(false);
            }
            list.add(category);
        }
        return list;

    }


    // Metoda zwraca nam wartosc ustawienia dotyczacego losowania słów : PL or EN or PL+EN
    public int loadSettingsRandom(){
        String query = "SELECT " + COLUMN_IS_CHECK + " FROM " + TABLE_SETTINGS + " WHERE " + COLUMN_SETTING_ID + " = -1";
        Cursor cursor = databaseRead.rawQuery(query,null);

        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public void updateSettingsCategory(ArrayList<Category> categories){
        ContentValues contentValues = new ContentValues();

        for(Category c : categories){
            contentValues.put(COLUMN_IS_CHECK,c.isSelected()?1:0);
            String args[] = {c.getName()};
            databaseWrite.update(TABLE_CATEGORIES,contentValues,COLUMN_CATEGORY_NAME+"=?",args);
        }


    }

    public void updateSettingsRandom(int i){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IS_CHECK,i);
        String args[] = {"-1"};
        databaseWrite.update(TABLE_SETTINGS,contentValues,COLUMN_SETTING_ID+"=?",args);
    }

}
