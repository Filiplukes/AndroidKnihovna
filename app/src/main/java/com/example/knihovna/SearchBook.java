package com.example.knihovna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchBook extends AppCompatActivity {
    private static final String TAG = SearchBook.class.getSimpleName();
    private EditText mBookInput;
    private TextView mAuthor, mTitle;
    private Button mSearchBooks,addBook;
    private BookItem book;
    private  DBBooks db;
    private DbUsers dbUser;
    private String username;
    private User user;


    private void setUser(){

        System.out.println("************");
        Cursor resUser = dbUser.getUsers(getIntent().getStringExtra("userName"));
        System.out.println("----");
        System.out.println(resUser.getCount());


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





    @Override
    public void onBackPressed(){

    }

    public void fSearchBooks(View view) {
        String queryString = mBookInput.getText().toString();
        //zmizeni klavesnice
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // kotrolovani site
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // Log.i(TAG, "Hledání: " + queryString);

        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {

            new FetchBook(mTitle, mAuthor, addBook).execute(queryString);
            mAuthor.setText("");
            mTitle.setText(R.string.load);
        } else {
            mTitle.setText("Zkontrolujte připojení k internetu a zkuste znovu");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        mBookInput = findViewById(R.id.bookInput);
        mAuthor = findViewById(R.id.authorText);
        mTitle = findViewById(R.id.titleText);
        mSearchBooks = findViewById(R.id.searchBooks);

        addBook = findViewById(R.id.addBook);
        addBook.setVisibility(View.INVISIBLE);

        username = getIntent().getStringExtra("userName");
        dbUser = new DbUsers(this);


        // incicializace navigace
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //vybrane
        bottomNavigationView.setSelectedItemId(R.id.searchBookOnline);

        //vyber
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bookMenu:
                        Intent book = new Intent(getApplicationContext(), ListBook.class);
                        book.putExtra("userName",username);
                        startActivity(book);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settingMenu:
                        Intent setting = new Intent(getApplicationContext(),Setting.class);
                        setting.putExtra("userName",username);
                        startActivity(setting);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.searchBookOnline:
                        return true;
                    case R.id.homeMenu:
                        Intent home = new Intent(getApplicationContext(),Uvod.class);
                        home.putExtra("userName",username);
                        startActivity(home);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        db = new DBBooks(this);

        mSearchBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fSearchBooks(v);
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("************");
               setUser();

               book = new BookItem(0,0,mTitle.getText().toString(),mAuthor.getText().toString(),"",0);
                System.out.println("************");
               book.setUserID(user.getId());

                System.out.println("************");
               Boolean chcekInsertData = db.insertBookData(book);
                if (chcekInsertData == true) {
                    Toast.makeText(SearchBook.this, "pridano", Toast.LENGTH_SHORT).show();

                    System.out.println("************");
                    user.setNumberBooks((user.getNumberBooks()+1));
                    dbUser.updateUsers(user);
                    dbUser.close();



                } else {
                    Toast.makeText(SearchBook.this, "Chyba pridani", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });
    }

}