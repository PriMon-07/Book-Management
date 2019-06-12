package com.learn.user.dbapplication.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PLibraryDbHelper extends SQLiteOpenHelper {



    private static final String  DATABASE_NAME="plibrary.db";
    private static final int DATABASE_VERSION=1;

    private static final String CREATE_TABLE_GENRES="CREATE TABLE " + PLibraryContracts.Genres.TABLE_NAME+ "("
            + PLibraryContracts.Genres.COLUMN_GENRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PLibraryContracts.Genres.COLUMN_GENRE_NAME + " TEXT NOT NULL"
            + ");";

    private static final String CREATE_TABLE_BOOKS="CREATE TABLE " + PLibraryContracts.Books.TABLE_NAME + "("
            + PLibraryContracts.Books.COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PLibraryContracts.Books.COLUMN_BOOK_NAME + " TEXT NOT NULL,"
            + PLibraryContracts.Books.COLUMN_GENRE_ID + " INTEGER,"
            +"FOREIGN KEY("+ PLibraryContracts.Books.COLUMN_GENRE_ID+") REFERENCES "+ PLibraryContracts.Genres.TABLE_NAME+"("+ PLibraryContracts.Genres.COLUMN_GENRE_ID+")"
            + ");";

    public PLibraryDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_GENRES);
        db.execSQL(CREATE_TABLE_BOOKS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + PLibraryContracts.Genres.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLibraryContracts.Books.TABLE_NAME);
        onCreate(db);

    }
}
