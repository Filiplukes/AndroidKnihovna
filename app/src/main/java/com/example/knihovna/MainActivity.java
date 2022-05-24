package com.example.knihovna;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText password;
    private EditText name;
    private Button login;
    private Button register;
    private TextView infoLogin;

    private int counter = 5;


    private  DbUsers db;
    private User user;


    @Override
    protected void onStart() {
        super.onStart();

        name.setText("");
        password.setText("");
        name.setHint("Jméno");
        password.setHint("Heslo");

    }

    // vychozi okno
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.ID);
        password = findViewById(R.id.Heslo);
        login = findViewById(R.id.prihlas);
        register = findViewById(R.id.registration);
        infoLogin = findViewById(R.id.upozorneniSpatneHeslo);

        name.requestFocus();

        db = new DbUsers(this);
    // registrace
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = name.getText().toString();
                String inputPassword = password.getText().toString();
                if(inputName.isEmpty() || inputPassword.isEmpty()){

                Toast.makeText(MainActivity.this, "Vložte všechy údaje!",Toast.LENGTH_SHORT).show();


                }else{

                    user = new User(inputName,inputPassword);

                    Cursor res = db.getUsers(user);

                    if(res.getCount()==0) {

                        long chcekInsertData = db.insertUsers(user);

                        if (chcekInsertData != -1) {

                            Toast.makeText(MainActivity.this, "Registraceproběhla", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(MainActivity.this, Uvod.class);
//                            name.setText("");
//                            password.setText("");
//                            name.setHint("Jméno");
//                            password.setHint("Heslo");
//                            user.setId(chcekInsertData);
//                            user.setName(inputName);
//                            user.setPassword(inputPassword);
//                            user.setReadPage(0);
//                            user.setNumberBooks(0);

                            home=setUser(home);

                            startActivity(home);

                        } else {
                            Toast.makeText(MainActivity.this, "Chyba uložiště", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(MainActivity.this, "Účet již existuje", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    res.close();
                }

            }
        });
// prihlasovani
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputName = name.getText().toString();
                String inputPassword = password.getText().toString();


                if(inputName.isEmpty() || inputPassword.isEmpty()){

                    Toast.makeText(MainActivity.this, "Vložte všechy údaje!",Toast.LENGTH_SHORT).show();

                }else{

                    user = new User(inputName,inputPassword);
                    Cursor res = db.getUsers(user);

                    String warning = "špatné jméno nebo heslo";

                    if(res.getCount()==0){
                        Toast.makeText(MainActivity.this, warning,Toast.LENGTH_SHORT).show();
                        res.close();
                        return;

                    }

                //kontrola hesla, prihlaseni uzivatele
                    if(!validateAccont(res,inputPassword)){
                        counter--;
                        Toast.makeText(MainActivity.this,"Špatně zadané údaje!",Toast.LENGTH_SHORT).show();
                        infoLogin.setVisibility(View.VISIBLE);
                        infoLogin.setText("Zbývající pokusy: " + counter +"!");
                        if(counter == 0) {
                            login.setEnabled(false);
                        }
                        res.close();
                    }else{
                        res.close();
                        Intent home = new Intent(MainActivity.this,Uvod.class);

                        home=setUser(home);
                        startActivity(home);
                    }

                }
            }
        });
    }


// validace uzivatele a kontrola hesla
    private boolean validateAccont(Cursor res, String inputPassword){

        while(res.moveToNext()){
            if(res.getString(2).equals(inputPassword)){

                user.setId(Integer.parseInt(res.getString(0)));
                user.setName(res.getString(1));
                user.setPassword(res.getString(2));

                if (res.getString(3)!= null){
                user.setReadPage(Integer.parseInt(res.getString(3)));
                }else{
                    user.setReadPage(0);
                }
                if (res.getString(4)!= null) {
                    user.setNumberBooks(Integer.parseInt(res.getString(4)));
                }else{
                    user.setNumberBooks(0);
                }
                return true;
            }
        }

        return false;

    }

    private Intent setUser(Intent home){
        home.putExtra("userName",user.getName());
        return home;
    }


}
