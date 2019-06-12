package com.learn.user.dbapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.user.dbapplication.Data.PLibraryContracts;
import com.learn.user.dbapplication.Data.PLibraryDbHelper;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class InformationActivity extends AppCompatActivity {

    EditText mNameEditText;
    Button mSaveButton;
    Spinner spinner;
    boolean update=false;
    int mBookId;
    Cursor mGenreCursor;
    SQLiteDatabase mDb;
    PLibraryDbHelper pLibraryDbHelper;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar2));
        getSupportActionBar().setTitle("Book Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner=findViewById(R.id.spinner2);
        mNameEditText=findViewById(R.id.et_book_info_name);
        mSaveButton=findViewById(R.id.bt_save);

        pLibraryDbHelper=new PLibraryDbHelper(this);
        mDb=pLibraryDbHelper.getWritableDatabase();

        Intent receivedIntent=getIntent();

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,getSpinnerData());
        spinner.setAdapter(arrayAdapter);


        if(receivedIntent.hasExtra("book-id")){
            mBookId=receivedIntent.getIntExtra("book-id",0);
            update=true;
            mSaveButton.setText("Update");
            Cursor cursor=mDb.query(PLibraryContracts.Books.TABLE_NAME,
                    null,
                    PLibraryContracts.Books.COLUMN_BOOK_ID+"=?",
                    new String[]{String.valueOf(mBookId)},
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            mNameEditText.setText(cursor.getString(1));
            mGenreCursor.moveToFirst();
            int i;
            for(i=0;i<mGenreCursor.getCount();i++){
                if(mGenreCursor.getInt(0)==cursor.getInt(2))
                    break;
                mGenreCursor.moveToNext();
            }
            spinner.setSelection(i+1);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (update)
                        updateData();

                    else
                        insetData();

            }
        });
    }

    private List<String> getSpinnerData(){

        List<String > l=new ArrayList<>();
        l.add("Select Genre");
        mGenreCursor=mDb.query(PLibraryContracts.Genres.TABLE_NAME,
                null,
                null,
                null,
                PLibraryContracts.Genres.COLUMN_GENRE_ID,
                null,
                null);

        while(mGenreCursor.moveToNext())
            l.add(mGenreCursor.getString(1));

        return l;

    }


    private void updateData(){

        if(TextUtils.isEmpty(mNameEditText.getText()) || spinner.getSelectedItemId()==0){
            if(mToast!=null)
                mToast.cancel();
            mToast= Toast.makeText(InformationActivity.this,"Fill the Fields Properly", LENGTH_LONG);
            mToast.show();
            return;
        }
        try {
            mDb.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(PLibraryContracts.Books.COLUMN_BOOK_NAME, String.valueOf(mNameEditText.getText()));
            int c = spinner.getSelectedItemPosition();
            mGenreCursor.moveToPosition(c - 1);
            cv.put(PLibraryContracts.Books.COLUMN_GENRE_ID, mGenreCursor.getInt(0));
            mDb.update(PLibraryContracts.Books.TABLE_NAME, cv, PLibraryContracts.Books.COLUMN_BOOK_ID + "=?",
                    new String[]{String.valueOf(mBookId)});
            mDb.setTransactionSuccessful();
        }catch (SQLException e){
            Log.e("InformationActivity","Error In update"+e);
        }finally {
            mDb.endTransaction();
        }

        finish();
    }


    private void insetData(){
        if(TextUtils.isEmpty(mNameEditText.getText()) || spinner.getSelectedItemId()==0){
            if(mToast!=null)
                mToast.cancel();
            mToast= Toast.makeText(InformationActivity.this,"Fill the Fields Properly", LENGTH_LONG);
            mToast.show();
            return;
        }
        try {
            mDb.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(PLibraryContracts.Books.COLUMN_BOOK_NAME, String.valueOf(mNameEditText.getText()));
            int c = spinner.getSelectedItemPosition();
            mGenreCursor.moveToPosition(c - 1);
            cv.put(PLibraryContracts.Books.COLUMN_GENRE_ID, mGenreCursor.getInt(0));
            mDb.insert(PLibraryContracts.Books.TABLE_NAME,
                    null,
                    cv);
            mDb.setTransactionSuccessful();
        }catch (SQLException e){
            Log.e("InformationActivity","Error In update"+e);
        }finally {
            mDb.endTransaction();
        }

        finish();
    }
}
