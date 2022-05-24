package com.example.knihovna;

import android.database.Cursor;

public class BookItem {

    private int Page,state;
    private long ID,userID;

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    private String name, author,genre;
    private float rating;

    public BookItem(long ID, int page, int state, String name, String author, String genre, float rating) {
        this.ID = ID;
        Page = page;
        this.state = state;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.rating = rating;
    }

    public BookItem(long ID) {
        this.ID = ID;
        }

    public BookItem(int page, int state, String name, String author, String genre, float rating) {
        Page = page;
        this.state = state;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.rating = rating;
    }

// nastaveni knihy z database

    public BookItem setBookWithRes(Cursor res, BookItem book){


        while (res.moveToNext()) {
            book.setName(res.getString(1));
            book.setAuthor(res.getString(2));
            if (res.getString(3) != null) {
                book.setPage(Integer.parseInt(res.getString(3)));
            } else {
                book.setPage(0);
            }
            if (res.getString(4) != null) {
                book.setRating(Float.parseFloat(res.getString(4)));
            } else {
                book.setRating(0);
            }
            if (res.getString(5) != null) {
                book.setGenre(res.getString(5));
            } else {
                book.setGenre("");
            }
            if (res.getString(6) != null) {
                book.setState(Integer.parseInt(res.getString(6)));
            } else {
                book.setState(3);
            }
        }

        return book;

    }


    public long getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
