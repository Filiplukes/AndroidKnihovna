package com.example.knihovna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Setting extends AppCompatActivity {
    private  String username;
    private Button saveButton;
    private EditText oldPassword,newPasswordInput,newPasswordCheckInput;
    private DbUsers db;
    private User user;

    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        saveButton = findViewById(R.id.saveButton);
        oldPassword = findViewById(R.id.oldPassword);
        newPasswordCheckInput = findViewById(R.id.newPasswordCheckInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);

        System.out.println("hello system out");

        if(newPasswordInput.getText().toString().equals(newPasswordCheckInput.getText().toString())){
        username = getIntent().getStringExtra("userName");
        db = new DbUsers(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(db.updateUsersPass(username,newPasswordInput.getText().toString(),oldPassword.getText().toString(),Setting.this)){
                    Toast.makeText(Setting.this, "Změna prošla!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Setting.this, "Chyba databáze",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }

        });}else{

            Toast.makeText(Setting.this, "Hesla jsou různá!",Toast.LENGTH_SHORT).show();

        }






        // incicializace navigace
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //vybrane
        bottomNavigationView.setSelectedItemId(R.id.settingMenu);

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