package com.learn.user.dbapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.learn.user.dbapplication.Data.PLibraryContracts;
import com.learn.user.dbapplication.Data.PLibraryDbHelper;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,BookAdapter.ListItemClickListner{

    Spinner spinner;
    SQLiteDatabase mDb;
    Cursor mGenreCursor;
    boolean mSearchByGenre=false;
    ImageView mAddImageView;
    RecyclerView mRecylerView;
    BookAdapter bookAdapter;
    Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar detailToolBar=findViewById(R.id.my_toolbar1);
        setSupportActionBar(detailToolBar);
        PLibraryDbHelper pLibraryDbHelper=new PLibraryDbHelper(this);

        mDb=pLibraryDbHelper.getWritableDatabase();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner=findViewById(R.id.spinner);
        mRecylerView=findViewById(R.id.rv_books);
        mAddImageView=findViewById(R.id.iv_add);

        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this,InformationActivity.class);
                startActivity(intent);
            }
        });



        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,getSpinnerData());
        spinner.setOnItemSelectedListener(this);

        spinner.setAdapter(arrayAdapter);

        Intent receievedIntent=getIntent();
        if(receievedIntent.hasExtra("position")){
            int position=receievedIntent.getIntExtra("position",0);
            spinner.setSelection(position);
            mGenreCursor.moveToPosition(position);
            mSearchByGenre=true;
        }


        bookAdapter=new BookAdapter(this,cursorForRecylerView(),this);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(bookAdapter);

    }

    private List<String> getSpinnerData(){

        List<String > l=new ArrayList<>();
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mGenreCursor.moveToPosition(position);
        bookAdapter.swapCursor(cursorForRecylerView());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Cursor cursorForRecylerView(){
        Cursor cursor=mDb.query(PLibraryContracts.Books.TABLE_NAME,
                null,
                PLibraryContracts.Books.COLUMN_GENRE_ID+"=?",
                new String[]{String.valueOf(mGenreCursor.getInt(0))},
                null,
                null,
                PLibraryContracts.Books.COLUMN_BOOK_ID);

        return cursor;
    }

    @Override
    public void onListItemClick(int clickItemIndex,boolean delete,String bookName) {

        if(delete){

            try{
                mDb.beginTransaction();
                mDb.delete(PLibraryContracts.Books.TABLE_NAME,
                        PLibraryContracts.Books.COLUMN_BOOK_ID+"="+clickItemIndex,
                        null);
                mDb.setTransactionSuccessful();
            }catch (SQLException e){
                Log.e("DetailActivity","Error in Delete"+e);
            }finally {
                mDb.endTransaction();
            }
            if(mToast!=null)
                mToast.cancel();
            mToast=Toast.makeText(this,"Deleted Book "+bookName,Toast.LENGTH_LONG);
            mToast.show();
            int c=spinner.getSelectedItemPosition();
            mGenreCursor.moveToPosition(c);
            bookAdapter.swapCursor(cursorForRecylerView());

        }else {
            Intent intent = new Intent(DetailActivity.this, InformationActivity.class);
            intent.putExtra("book-id", clickItemIndex);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int c=spinner.getSelectedItemPosition();
        mGenreCursor.moveToPosition(c);
        bookAdapter.swapCursor(cursorForRecylerView());
    }
}
