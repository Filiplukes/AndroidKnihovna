package com.example.knihovna;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

// dostani knih z googlu

public class FetchBook extends AsyncTask<String, Void, String> {
    private TextView mTitleText,mAuthorText;
    private Button addBook;

    public FetchBook(TextView mTitleText, TextView mAuthorText,Button addBook){
        this.mTitleText = mTitleText;
        this.mAuthorText = mAuthorText;
        this.addBook = addBook;
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }


    // parsovani jsonu, vizualizace dat
    @Override
    protected void onPostExecute(String s) {

        super.onPreExecute();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                String title = null;
                String author = null;
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                try {
                    title = volumeInfo.getString("title");
                    author = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (title != null && author != null) {
                    mTitleText.setText(title);
                    mAuthorText.setText(author);
                    addBook.setVisibility(View.VISIBLE);
                    return;
                }
                mTitleText.setText("žádné výsledky");
                mAuthorText.setText("");
                addBook.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            mTitleText.setText("žádné výsledky");
            mAuthorText.setText("");
        }
    }

}
