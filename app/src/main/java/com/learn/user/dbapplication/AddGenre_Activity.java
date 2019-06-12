package com.learn.user.dbapplication;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learn.user.dbapplication.Data.PLibraryContracts;
import com.learn.user.dbapplication.Data.PLibraryDbHelper;

import static android.widget.Toast.LENGTH_LONG;

public class AddGenre_Activity extends AppCompatActivity {

    EditText mGenreNameEditText;
    Button mSaveButton;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_genre_);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar4));
        getSupportActionBar().setTitle("Add Genre");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGenreNameEditText=findViewById(R.id.et_add_genre_name);
        mSaveButton=findViewById(R.id.bt_save_genre);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
    }

    private void insert(){
        if(TextUtils.isEmpty(mGenreNameEditText.getText())){
            if(mToast!=null)
                mToast.cancel();
            mToast= Toast.makeText(AddGenre_Activity.this,"Fill the Fields Properly", LENGTH_LONG);
            mToast.show();
            return;
        }
        PLibraryDbHelper pLibraryDbHelper=new PLibraryDbHelper(this);
        SQLiteDatabase mDb=pLibraryDbHelper.getWritableDatabase();
        try {
            mDb.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(PLibraryContracts.Genres.COLUMN_GENRE_NAME, String.valueOf(mGenreNameEditText.getText()));
            mDb.insert(PLibraryContracts.Genres.TABLE_NAME,
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
