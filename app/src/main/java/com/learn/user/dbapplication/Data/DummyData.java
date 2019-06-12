package com.learn.user.dbapplication.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class DummyData {

    SQLiteDatabase mDb;

    public void insertFakeData(SQLiteDatabase db){

        mDb=db;
        List<ContentValues> listBooks=new ArrayList<>();

        if(db==null) {
            Log.i("MainActivity","Inside If");
            return;
        }

        insertGenre();
        insertBooks();

    }

    private void insertBooks() {

        Cursor cursor=mDb.query(PLibraryContracts.Books.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()) {
            Log.i("MainActivity","Inside insertBook() Method If");
            return;
        }


        List<ContentValues> listBooks=new ArrayList<>();
        ContentValues cv;
        int g;
        for(int i=1;i<=25;i++){
            cv=new ContentValues();
            g=i%5+1;
            cv.put(PLibraryContracts.Books.COLUMN_BOOK_NAME,"Book Name "+i);
            cv.put(PLibraryContracts.Books.COLUMN_GENRE_ID,g);
            listBooks.add(cv);
        }

        try {
            mDb.beginTransaction();
            mDb.delete(PLibraryContracts.Books.TABLE_NAME, null, null);
            for (ContentValues c : listBooks)
                mDb.insert(PLibraryContracts.Books.TABLE_NAME, null, c);
            mDb.setTransactionSuccessful();
        }catch (SQLException e){
            Log.e("MainActivity", String.valueOf(e));
        }finally {
            mDb.endTransaction();
        }

    }

    private void insertGenre(){

        Cursor cursor=mDb.query(PLibraryContracts.Genres.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()) {
            Log.i("MainActivity","Inside insertGenre() Method If");
            return;
        }


        List<ContentValues> listGenre=new ArrayList<>();
        ContentValues cv;
        for(int i=1;i<=5;i++){
            cv=new ContentValues();
            cv.put(PLibraryContracts.Genres.COLUMN_GENRE_NAME,"Genre Name "+i);
            listGenre.add(cv);
        }

        try {
            mDb.beginTransaction();
            mDb.delete(PLibraryContracts.Genres.TABLE_NAME, null, null);
            for (ContentValues c : listGenre)
                mDb.insert(PLibraryContracts.Genres.TABLE_NAME, null, c);
            mDb.setTransactionSuccessful();
        }catch (SQLException e){
            Log.e("MainActivity", String.valueOf(e));
        }finally {
            mDb.endTransaction();
        }
    }
}
