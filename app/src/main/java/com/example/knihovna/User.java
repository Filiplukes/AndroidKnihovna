package com.example.knihovna;

import android.database.Cursor;

public class User {
    private long id;
    private int readPage,numberBooks;
    private String password,name;



//    public User setUserRes(Cursor re,User user){
//        Cursor res = re;
//
//        while (res.moveToNext()) {
//
//
//            user = new User(res.getString(1), res.getString(2));
//            user.setId(Integer.parseInt(res.getString(0)));
//            if (res.getString(3) != null) {
//                user.setReadPage(Integer.parseInt(res.getString(3)));
//            } else {
//                user.setReadPage(0);
//            }
//            if (res.getString(4) != null) {
//                user.setNumberBooks(Integer.parseInt(res.getString(4)));
//            } else {
//                user.setNumberBooks(0);
//            }
//
//        }
//        System.out.println("hello system out");
//        System.out.println(user.getName());
//        return user;
//    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getReadPage() {
        return readPage;
    }

    public void setReadPage(int readPage) {
        this.readPage = readPage;
    }

    public int getNumberBooks() {
        return numberBooks;
    }

    public void setNumberBooks(int numberBooks) {
        this.numberBooks = numberBooks;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name, String password) {
      this.name = name;
        this.password = password;
    }
}
