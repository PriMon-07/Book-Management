package com.learn.user.dbapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.user.dbapplication.Data.DummyData;
import com.learn.user.dbapplication.Data.PLibraryContracts;
import com.learn.user.dbapplication.Data.PLibraryDbHelper;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements GenreAdapter.ListItemClickListner{

    TextView mGenreIdTextView,mGenreNameTextView,mGenreId2TextView;

    SQLiteDatabase mDb;
    GenreAdapter mGenreAdapter;
    RecyclerView mRecyclerView;
    Cursor mCursor;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        getSupportActionBar().setIcon(R.drawable.ic_action_name);


        mRecyclerView=findViewById(R.id.rv_genre);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        PLibraryDbHelper pLibraryDbHelper=new PLibraryDbHelper(this);
        mDb=pLibraryDbHelper.getWritableDatabase();
        DummyData dummyData=new DummyData();
        dummyData.insertFakeData(mDb);

//        mCursor=mDb.rawQuery("SELECT "+ PLibraryContracts.Genres.TABLE_NAME+"."+PLibraryContracts.Genres.COLUMN_GENRE_ID+","
//                        + PLibraryContracts.Genres.COLUMN_GENRE_NAME+"," +
//                "COUNT(*) " +" AS 'no_of_books' "+
//                "FROM "+ PLibraryContracts.Genres.TABLE_NAME
//                +" LEFT JOIN "+ PLibraryContracts.Books.TABLE_NAME
//                +" ON "+ PLibraryContracts.Books.TABLE_NAME+"."+ PLibraryContracts.Books.COLUMN_GENRE_ID+"="+ PLibraryContracts.Genres.TABLE_NAME+"."+ PLibraryContracts.Genres.COLUMN_GENRE_ID
//                +" GROUP BY "+ PLibraryContracts.Genres.TABLE_NAME+"."+PLibraryContracts.Genres.COLUMN_GENRE_ID+","
//                        + PLibraryContracts.Genres.COLUMN_GENRE_NAME,
//                null);

        mCursor=getCursorForRecyclerView();

        //Cursor cursor=mDb.rawQuery("Select count(*) from genres",null);
//        cursor.moveToFirst();
//        int id=cursor.getInt(0);
//        String name=cursor.getString(1);
//        int count=cursor.getInt(2);
//        Log.i("MainActivity",id+" "+name+" "+count);


        //cursor.moveToFirst();
//        while (cursor.moveToNext()){
//            mGenreIdTextView.append(String.valueOf(cursor.getInt(cursor.getColumnIndex(PLibraryContracts.Books.COLUMN_BOOK_ID)))+"\n\n");
//            mGenreNameTextView.append(cursor.getString(cursor.getColumnIndex(PLibraryContracts.Books.COLUMN_BOOK_NAME))+"\n\n");
//            mGenreId2TextView.append(String.valueOf(cursor.getInt(cursor.getColumnIndex(PLibraryContracts.Books.COLUMN_GENRE_ID)))+"\n\n");
//        }


        mGenreAdapter=new GenreAdapter(this,mCursor,this);

        mRecyclerView.setAdapter(mGenreAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // COMPLETED (9) Within onCreateOptionsMenu, use getMenuInflater().inflate to inflate the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // COMPLETED (10) Return true to display your menu
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_add_genre) {
            Intent intent=new Intent(MainActivity.this,AddGenre_Activity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickItemIndex,boolean onLongClick) {
        mCursor.moveToPosition(clickItemIndex);
        Log.i("MainActivity",mCursor.getString(1)+"");
        if(onLongClick){
            if(mCursor.getInt(2)==0){
                try{
                    mDb.beginTransaction();
                    mDb.delete(PLibraryContracts.Genres.TABLE_NAME,
                            PLibraryContracts.Genres.COLUMN_GENRE_ID+"="+mCursor.getInt(0),
                            null);
                    mDb.setTransactionSuccessful();
                }catch (SQLException e){
                    Log.e("DetailActivity","Error in Delete"+e);
                }finally {
                    mDb.endTransaction();
                }

                if(mToast!=null)
                    mToast.cancel();
                mToast=Toast.makeText(this,"Deleted "+mCursor.getString(1),Toast.LENGTH_LONG);
                mToast.show();

                mCursor=getCursorForRecyclerView();
                mGenreAdapter.swapCursor(mCursor);

            }else{
                if(mToast!=null)
                    mToast.cancel();
                mToast=Toast.makeText(this,"Book Available "+mCursor.getInt(2),Toast.LENGTH_LONG);
                mToast.show();
            }

        }else {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("position", clickItemIndex);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
         mCursor=getCursorForRecyclerView();
         mGenreAdapter.swapCursor(mCursor);

    }

    private Cursor getCursorForRecyclerView(){

        Cursor cursor=mDb.rawQuery("SELECT "+ PLibraryContracts.Genres.TABLE_NAME+"."+PLibraryContracts.Genres.COLUMN_GENRE_ID+","
                        + PLibraryContracts.Genres.COLUMN_GENRE_NAME+"," +
                        "(SELECT COUNT("+ PLibraryContracts.Books.COLUMN_GENRE_ID+") FROM "+ PLibraryContracts.Books.TABLE_NAME +" WHERE "+ PLibraryContracts.Books.TABLE_NAME+"." + PLibraryContracts.Genres.COLUMN_GENRE_ID+"="+ PLibraryContracts.Genres.TABLE_NAME+"." + PLibraryContracts.Books.COLUMN_GENRE_ID+") AS 'no_of_books' "+
                        "FROM "+ PLibraryContracts.Genres.TABLE_NAME
                        +" GROUP BY "+ PLibraryContracts.Genres.TABLE_NAME+"."+PLibraryContracts.Genres.COLUMN_GENRE_ID+","
                        + PLibraryContracts.Genres.COLUMN_GENRE_NAME,
                null);
        return cursor;
    }
}
