package com.example.knihovna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class Uvod extends AppCompatActivity {
    private DbUsers db;
    private User user;
    private Button out;
    private TextView userName, numberReadOfBooks, numberOfPage;
// vytvoreni uvodu
    @Override
    protected void onStart() {
        super.onStart();
    // vytvoreni uzivatele
        Cursor res = db.getUsers(getIntent().getStringExtra("userName"));

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

        userName.setText(user.getName());
        numberReadOfBooks.setText(Integer.toString(user.getNumberBooks()));
        numberOfPage.setText(Integer.toString(user.getReadPage()));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvod);

        userName = findViewById(R.id.userName);
        numberReadOfBooks = findViewById(R.id.numberReadOfBooks);
        numberOfPage = findViewById(R.id.numberOfPage);
        out = findViewById(R.id.buttonOut);

        db = new DbUsers(this);


        // incicializace navigace
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //vybrane
        bottomNavigationView.setSelectedItemId(R.id.homeMenu);

        //vyber
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bookMenu:
                        Intent book = new Intent(getApplicationContext(), ListBook.class);
                        book.putExtra("userName",user.getName());
                        startActivity(book);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settingMenu:
                        Intent setting = new Intent(getApplicationContext(),Setting.class);
                        setting.putExtra("userName",user.getName());
                        startActivity(setting);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.searchBookOnline:
                        Intent search = new Intent(getApplicationContext(), SearchBook.class);
                        search.putExtra("userName",user.getName());
                        startActivity(search);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.homeMenu:
                        return true;

                }
                return false;
            }
        });


        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Uvod.this, MainActivity.class);
                startActivity(home);
            }
        });

    }

    @Override
    public void onBackPressed(){

    }


}