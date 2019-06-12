package com.learn.user.dbapplication.Data;

import android.provider.BaseColumns;

public class PLibraryContracts {


    public static final class Genres{
        public static final String TABLE_NAME="genres";
        public static final String COLUMN_GENRE_ID="g_id";
        public static final String COLUMN_GENRE_NAME="g_name";

    }

    public static final class Books{
        public static final String TABLE_NAME="books";
        public static final String COLUMN_BOOK_ID="b_id";
        public static final String COLUMN_BOOK_NAME="b_name";
        public static final String COLUMN_GENRE_ID=Genres.COLUMN_GENRE_ID;
    }

    public static final class Borrowed{
        public static final String TABLE_NAME="borrowed";
        public static final String COLUMN_BORROWED_ID="bw_id";
        public static final String COLUMN_PERSON_NAME_ID="p_name";
        public static final String COLUMN_TIMESTAMP_ID="timestamp";
        public static final String COLUMN_RECEIVED_ID="received";
        public static final String COLUMN_BOOK_ID=Books.COLUMN_BOOK_ID;
    }
}
