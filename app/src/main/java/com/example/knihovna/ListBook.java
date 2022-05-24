package com.example.knihovna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;



import com.google.android.material.bottomnavigation.BottomNavigationView;



public class ListBook extends AppCompatActivity {
    private Button insertBookButton;
    private BottomNavigationView bottomNavigationView;
    private  DBBooks db;
    private String username;
    private User user;
    private DbUsers dbUser;

    // zreuseni tlacitka zpet
    @Override
    public void onBackPressed(){

    }
// funce po startu  list okna
    @Override
    protected void onStart() {
        super.onStart();
        db = new DBBooks(this);
        // seznam knih
        ListView listBooks = findViewById(R.id.listBookDatabase);
        SimpleCursorAdapter simpleCursorAdapter = db.getBookDataSimple(this,user);
        listBooks.setAdapter(simpleCursorAdapter);
        listBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) simpleCursorAdapter.getItem(position);
                Intent insertBook = new Intent(ListBook.this,Book.class);
                insertBook.putExtra("userName",username);
                insertBook.putExtra("idBook",cursor.getString(0));
                Long.getLong(cursor.getString(0));
                startActivity(insertBook);

            }
        });

        db.close();


    }

    // vytvoreni list book
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        username = getIntent().getStringExtra("userName");
        db = new DBBooks(this);

    // vytvoreni uzivatele
        dbUser = new DbUsers(this);

        Cursor res = dbUser.getUsers(getIntent().getStringExtra("userName"));

        while (res.moveToNext()) {

            user = new User(res.getString(1), res.getString(2));
            user.setId(Integer.parseInt(res.getString(0)));
            if (res.getString(3) != null) {
                user.setReadPage(Integer.parseInt(res.getString(3)));
            } else {
                user.setReadPage(0);
            }
            if (res.getString(4) != null) {
                user.setNumberBooks(Integer.parseInt(res.getString(4)));
            } else {
                user.setNumberBooks(0);
            }

        }

        db.close();
        res.close();







        //inicializace uloz knihu
        insertBookButton = findViewById(R.id.insertBookButoon);

        // uloz knuhu

        insertBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertBook = new Intent(ListBook.this,settingBook.class);
                insertBook.putExtra("type","insert");
                insertBook.putExtra("userName",username);
                startActivity(insertBook);
            }
        });

        // incicializace navigace
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //vybrane
        bottomNavigationView.setSelectedItemId(R.id.bookMenu);

        //vyber
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bookMenu:
                        return true;
                    case R.id.settingMenu:
                        Intent setting = new Intent(getApplicationContext(),Setting.class);
                        setting.putExtra("userName",username);
                        startActivity(setting);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.searchBookOnline:
                        Intent search = new Intent(getApplicationContext(), SearchBook.class);
                        search.putExtra("userName",username);
                        startActivity(search);
                        overridePendingTransition(0, 0);
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
    }
}