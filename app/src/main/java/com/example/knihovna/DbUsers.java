
package com.example.knihovna;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.widget.Toast;

        import androidx.annotation.Nullable;

public class DbUsers extends SQLiteOpenHelper {


    public DbUsers(Context context) {
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
// pridani uzivatele
    public long insertUsers(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name",user.getName());
        contentValues.put("password",user.getPassword());
        long result = db.insert("UsersBookApp",null, contentValues);
        if (result == -1) {
            return result;
        } else {
            return result;
        }

    }
// uprava hesla uzivatele
    public Boolean updateUsersPass(String name,String password,String oldPass, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        Cursor cursor = db.rawQuery("Select * from UsersBookApp where name =?",new String[] {name});
        boolean equalsPass = false;
        System.out.println("+++++++++++++ " + cursor.getCount() );
        if (cursor.getCount() > 0) {

            while(cursor.moveToNext()){

                System.out.println("+++++++++++++ " + cursor.getString(2) + " -------- "+ oldPass );

                if(cursor.getString(2).equals(oldPass)){

                    equalsPass = true;
                }
            }

            if(!equalsPass){
                Toast.makeText(context, "Špatně zadané staré heslo",Toast.LENGTH_SHORT).show();
                return false;
            }

            long result = db.update("UsersBookApp", contentValues,
                    "name=?",new String[] {name});

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {

            return false;
        }

    }

// uprava stan a knih uzivatele
    public Boolean updateUsers(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("readPage", user.getReadPage());
        contentValues.put("numberBooks", user.getNumberBooks());
        Cursor cursor = db.rawQuery("Select * from UsersBookApp where _id =?",new String[] {Long.toString(user.getId())});
        System.out.println(user.getReadPage());
        System.out.println(user.getNumberBooks());

        if (cursor.getCount() > 0) {

            long result = db.update("UsersBookApp", contentValues,
                    "_id=?",new String[] {Long.toString(user.getId())});

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {

            return false;
        }

    }

// mazani uzivatele, neuzito
    public Boolean deleteUsers(User user) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery("Select * from UsersBookApp where _id ="+user.getId()
                , null);
        if (cursor.getCount() > 0) {

            long result = db.delete("UsersBookApp", "_id="+user.getId(), null);

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {

            return false;
        }
    }

    //dostani zuivatele dle jmena objektu User
    public Cursor getUsers(User user){

        try{
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from UsersBookApp where name = ?",new String [] {user.getName()});


        return cursor;
        }catch (Exception e){

            return null;
        }

    }
    //dostani zuivatele dle jmena uzivatele
    public Cursor getUsers(String name){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("Select * from UsersBookApp where name = ?",new String [] {name});


            return cursor;
        }catch (Exception e){

            return null;
        }

    }


}



