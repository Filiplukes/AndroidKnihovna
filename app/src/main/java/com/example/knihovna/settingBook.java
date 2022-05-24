package com.example.knihovna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class settingBook extends AppCompatActivity {

    private EditText name, author, page;
    private Button insert, delete;
    private DBBooks db;
    private DbUsers dbUser;
    private User user;
    //promenne k choice menu
    private TextView genre;
    private RatingBar ratingBar;
    private boolean[] selectGenre;
    private ArrayList<Integer> genreList = new ArrayList<>();
    private String[] genreArray = {"Biografie", "Cestopis", "Detektivky", "Dětské knih", "Fantasy", "Horor", "Klasická literature", "Komiks (a Manga)", "Literatura faktu", "Osobní rozvoj", "Poezie", "Populárně naučná", "Román", "Sci-fi", "Thrillery", "Young adult"};
    private String type,username;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private BookItem book;
    private long idBook;


    private void setUser(){
        // nastaveni uzivatele
        Cursor resUser = dbUser.getUsers(getIntent().getStringExtra("userName"));
        while (resUser.moveToNext()) {

            user = new User(resUser.getString(1), resUser.getString(2));
            user.setId(Integer.parseInt(resUser.getString(0)));
            if (resUser.getString(3) != null) {
                user.setReadPage(Integer.parseInt(resUser.getString(3)));
            } else {
                user.setReadPage(0);
            }
            if (resUser.getString(4) != null) {
                user.setNumberBooks(Integer.parseInt(resUser.getString(4)));
            } else {
                user.setNumberBooks(0);
            }
        }
        dbUser.close();
        resUser.close();
    }

    // vytvoreni okna
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_book);

        name = findViewById(R.id.editTextPersonName);
        author = findViewById(R.id.editTextAuthor);
        page = findViewById(R.id.editPage);
        ratingBar = findViewById(R.id.ratingBar);

        radioGroup = findViewById(R.id.radioGroup);


        genre = findViewById(R.id.genre);

        insert = findViewById(R.id.saveButton);
        delete = findViewById(R.id.deleteButton);
        db = new DBBooks(this);

        type = getIntent().getStringExtra("type");
        username = getIntent().getStringExtra("userName");


        dbUser = new DbUsers(this);




        if (type.equals("insert")) {
            insert.setText("Ulož");
            delete.setVisibility(View.INVISIBLE);
            radioButton = findViewById(R.id.radioButton6);
            radioButton.setChecked(true);


        } else {

            insert.setText("Oprav");
            idBook = Long.parseLong(getIntent().getStringExtra("idBook"));
            book = new BookItem(idBook);
            Cursor res = db.getBookData(book);

            book = book.setBookWithRes(res,book);


            db.close();
            res.close();


            if (book.getState() == 4) {
                radioButton = findViewById(R.id.radioButton4);
            } else if (book.getState() == 5) {
                radioButton = findViewById(R.id.radioButton5);
            } else {
                radioButton = findViewById(R.id.radioButton6);
            }
            radioButton.setChecked(true);

            name.setText(book.getName());
            author.setText(book.getAuthor());
            page.setText(Integer.toString(book.getPage()));
            ratingBar.setRating(book.getRating());

            genre.setText(book.getGenre());


        }
        // kliknuti na button uloz oprav
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("")){
                    Toast.makeText(settingBook.this, "Jméno musí být vyplňěno", Toast.LENGTH_SHORT).show();
                    return;
                }

                    radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

                int pomRadio;
                if (radioButton.getText().equals("Přečteno")) {
                    pomRadio = 4;
                } else if (radioButton.getText().equals("Rozečteno")) {
                    pomRadio = 5;
                } else pomRadio = 6;
                setUser();
                if (type.equals("insert")) {
                    int pomPage = 0;
                    if (!page.getText().toString().equals("")) {
                        pomPage = Integer.parseInt(page.getText().toString());
                    }
                    book = new BookItem(pomPage, pomRadio, name.getText().toString(),
                            author.getText().toString(), genre.getText().toString(), ratingBar.getRating());
                    book.setUserID(user.getId());
                    Boolean chcekInsertData = db.insertBookData(book);
                    if (chcekInsertData == true) {
                        Toast.makeText(settingBook.this, "pridano", Toast.LENGTH_SHORT).show();
                        user.setNumberBooks((user.getNumberBooks()+1));
                        if (pomRadio == 4) {user.setReadPage(user.getReadPage()+pomPage);}
                        dbUser.updateUsers(user);
                        dbUser.close();
                        settingBook.super.onBackPressed();

                    } else {
                        Toast.makeText(settingBook.this, "Chyba pridani", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    BookItem beforeBook = book;
                    book = new BookItem(book.getID(), Integer.parseInt(page.getText().toString()), pomRadio, name.getText().toString(),
                            author.getText().toString(), genre.getText().toString(), ratingBar.getRating());
                    book.setUserID(user.getId());

                    Boolean chcekUpdateData = db.updateBookData(book);


                    if (chcekUpdateData == true) {
                        Toast.makeText(settingBook.this, "Upraveno", Toast.LENGTH_SHORT).show();

                        // podminky upravy poctu stran
                        if((book.getPage()!=beforeBook.getPage())&& (beforeBook.getState()==4) &&((book.getState()==4))){
                            user.setReadPage((user.getReadPage()-beforeBook.getPage())+book.getPage());
                        }else if((book.getPage()!=beforeBook.getPage())&& (beforeBook.getState()!=4) &&((book.getState()==4))){
                            user.setReadPage((user.getReadPage()+book.getPage()));
                        }else if((book.getPage()!=beforeBook.getPage())&& (beforeBook.getState()==4) &&((book.getState()!=4))){
                            user.setReadPage((user.getReadPage()-beforeBook.getPage()));
                        }else if(book.getPage()==beforeBook.getPage() && book.getState()!=beforeBook.getState()){
                         if (beforeBook.getState()==4){
                             user.setReadPage(user.getReadPage()-book.getPage());
                         }
                         if (book.getState()==4){
                             user.setReadPage(user.getReadPage()+book.getPage());
                         }
                        }

                        dbUser.updateUsers(user);
                        dbUser.close();


                    } else {
                        Toast.makeText(settingBook.this, "Chyba upravy", Toast.LENGTH_SHORT).show();
                    }
                }
                db.close();
            }
        });

        //mazani knihy
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean deleteBookData = db.deleteBookData(book.getID());
                if (deleteBookData == true) {
                    Toast.makeText(settingBook.this, "Smazano", Toast.LENGTH_SHORT).show();
                    setUser();
                    user.setNumberBooks((user.getNumberBooks()-1));
                    if (book.getState() == 4) {user.setReadPage(user.getReadPage()-book.getPage());}
                    dbUser.updateUsers(user);
                    dbUser.close();
                    Intent book = new Intent(getApplicationContext(), ListBook.class);
                    book.putExtra("userName",user.getName());
                    startActivity(book);


                } else {
                    Toast.makeText(settingBook.this, "chyba mazani", Toast.LENGTH_SHORT).show();
                }


            }

        });

        //vyber zanru
        selectGenre = new boolean[genreArray.length];
        genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settingBook.this);
                builder.setTitle("Vyber žánr");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(genreArray, selectGenre, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            genreList.add(which);
                            Collections.sort(genreList);
                        } else {
                            genreList.remove(which);

                        }
                    }
                });
                //vyber zanru
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        //vyber zanru
                        for (int i = 0; i < genreList.size(); i++) {
                            stringBuilder.append(genreArray[genreList.get(i)]);
                            if (i != genreList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        genre.setText(stringBuilder.toString());
                    }
                });
                //zruseni vyberu
                builder.setNegativeButton("Zruš", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                //vycisteni vyberu
                builder.setNeutralButton("Smazat vše", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mazani vybranych
                        for (int i = 0; i < selectGenre.length; i++) {
                            selectGenre[i] = false;
                            genreList.clear();
                            genre.setText("");

                        }
                    }
                });

                //zobrazeni dialogu

                builder.show();


            }
        });

    }

}