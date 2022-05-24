package com.example.knihovna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class Book extends AppCompatActivity {
    private Button updateBook;
    private BookItem book;
    private DBBooks db;
    private TextView name, author, page,textStav,genre;
    private RatingBar ratingBar;
    private String username;

    @Override
    protected void onStart(){
        super.onStart();



        Cursor res = db.getBookData(book);
        book = book.setBookWithRes(res,book);
        db.close();
        res.close();



        if (book.getState() == 4) {
            textStav.setText(R.string.read);
        } else if (book.getState() == 5) {
            textStav.setText(R.string.inread);
        } else {
            textStav.setText(R.string.notRead);
        }


        name.setText(book.getName());
        author.setText(book.getAuthor());
        page.setText(Integer.toString(book.getPage()));
        ratingBar.setRating(book.getRating());
        genre.setText(book.getGenre());



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        book = new BookItem(Long.parseLong(getIntent().getStringExtra("idBook")));
        username = getIntent().getStringExtra("userName");

        db = new DBBooks(this);


        name = findViewById(R.id.bookName);
        author = findViewById(R.id.textAuthor);
        page = findViewById(R.id.textPage);
        genre = findViewById(R.id.textGenre);
        textStav = findViewById(R.id.textStav);
        ratingBar = findViewById(R.id.ratingBar);
        updateBook = findViewById(R.id.updateBook);


    //otevreni nastaveni knihy

        updateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertBook = new Intent(Book.this,settingBook.class);
                insertBook.putExtra("type","update");
                insertBook.putExtra("idBook",Long.toString(book.getID()));
                insertBook.putExtra("userName",username);
                startActivity(insertBook);

            }
        });


    }
}