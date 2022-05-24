package com.example.knihovna;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getCanonicalName();
    //web google book api
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q"; //hledani stringu
    private static final String MAX_RESULTS = "maxResults"; //omezeni vysledku
    private static final String PRINT_TYPE = "printType"; //filter zobrazeni


    static String getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try{
            //dotaz na knihy
            Uri builtUri= Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS,"10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestURL = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //nacteni odpovedi
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                return null;
            }
            reader = new BufferedReader( new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) !=null){
                buffer.append(line + "\n");
            }
            if(buffer.length() == 0){
                //prazdny string
                return null;
            }
            bookJSONString = buffer.toString();

        }catch(Exception ex){

            ex.printStackTrace();
            return null;


        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }

            }
            Log.d(LOG_TAG,bookJSONString);
            return bookJSONString;
        }

    };

}
