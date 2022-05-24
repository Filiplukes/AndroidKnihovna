package com.example.knihovna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;

public class DBBooks extends SQLiteOpenHelper {

//database knih

    public DBBooks(Context context) {
        super(context, "Userdata.db", null, 2);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE UsersBookApp (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, password TEXT, readPage INTEGER, numberBooks INTEGER)");

        db.execSQL("create Table Bookdetails (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name TEXT NOT NULL, author TEXT, page INTEGER, rating NUMERIC, genre TEXT, state INTEGER, userID INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists UsersBookApp");
        db.execSQL("drop Table if exists Bookdetails");


    }
// vytvoreni knihy
    public Boolean insertBookData(BookItem book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", book.getName());
        contentValues.put("author", book.getAuthor());
        contentValues.put("page", book.getPage());
        contentValues.put("rating", book.getRating());
        contentValues.put("genre", book.getGenre());
        contentValues.put("state", book.getState());
        contentValues.put("userID", book.getUserID());
        long result = db.insert("Bookdetails", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

// uprava knihy

    public Boolean updateBookData(BookItem book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", book.getName());
        contentValues.put("author", book.getAuthor());
        contentValues.put("page", book.getPage());
        contentValues.put("rating", book.getRating());
        contentValues.put("genre", book.getGenre());
        contentValues.put("state", book.getState());
        Cursor cursor = db.rawQuery("Select * from Bookdetails where _id=?",new String[] {Long.toString(book.getID())});

        if (cursor.getCount() > 0) {


            long result = db.update("Bookdetails", contentValues,
                    "_id=?", new String[] {Long.toString(book.getID())});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }



// smazani knihy
    public Boolean deleteBookData(long ID) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery("Select * from Bookdetails where _id=?",new String[] {Long.toString(ID)});
        if (cursor.getCount() > 0) {
            long result = db.delete("Bookdetails", "_id=?", new String[] {Long.toString(ID)});
            db.close();
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            db.close();
            return false;
        }
    }
// dostani knihy dle id knihy
    public Cursor getBookData(BookItem book){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Bookdetails where _id=?",new String[] {Long.toString(book.getID())});
        return cursor;

    }

    // dostani knihy dle id uzivatele
    public SimpleCursorAdapter getBookDataSimple(Context context,User user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Bookdetails where userID=?",new String[] {Long.toString(user.getId())});


        String [] fromFieldNames = new String[]{"name","author"};
        int [] toViewIDs = new int[]{R.id.item_book,R.id.item_author};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context,
                R.layout.single_book,
                cursor,
                fromFieldNames,
                toViewIDs);

       // System.out.println("looooog");
        return simpleCursorAdapter;

    }


}

//context,R.layout.single_book,fromFieldNames,toViewIDs

//"ID","name","author","page","rating","genre","state"