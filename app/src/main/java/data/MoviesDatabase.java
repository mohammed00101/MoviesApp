package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mno.mohamed_youssef.movie1.Movie;

/**
 * Created by Mohamed Yossif on 19/03/2016.
 */
public class MoviesDatabase {


    private MovieDBHelper mDbHelper;
    private SQLiteDatabase db;
    private final Context mCtx;

    public MoviesDatabase(Context ctx) {
        this.mCtx = ctx;
    }


    public static final class MovieTable {


        public static final String _ID="id";

        public static final String TABLE_NAME = "movies";


        //---------- icon id

        public static final String MOVIE_ID_KEY = "movie_id";
        public static final String POSTER_KEY = "poster";


        // create Table------------------------------

        public static final String SQL_CREATE_MOVIE_TABLE =
                "create table " + TABLE_NAME + " ( "  +
                        _ID + " integer primary key autoincrement," +
                        MOVIE_ID_KEY + " text not null," +
                        POSTER_KEY +" text not null," +
                  "unique (" +MOVIE_ID_KEY  + "," +POSTER_KEY + ")on conflict replace );";



    }



    private static class MovieDBHelper extends SQLiteOpenHelper {


        private static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "movies.db";
        SQLiteDatabase db;

        public MovieDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(MovieTable.SQL_CREATE_MOVIE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("drop table if exists " + MovieTable.TABLE_NAME);
            onCreate(db);
        }
    }


    public MoviesDatabase open() {
            mDbHelper = new MovieDBHelper(mCtx);
            db = mDbHelper.getReadableDatabase();
            return this;
        }


    public void close() {
        if (db != null) {
            db.close();
        }
    }


    public long insertMovie(Movie movie){

        ContentValues values = new ContentValues();
        values.put(MovieTable.POSTER_KEY, movie.getPoster());
        values.put(MovieTable.MOVIE_ID_KEY, movie.getId());

        return  db.insert(MovieTable.TABLE_NAME,null,values);
    }




    public long removeMovie(Movie movie){



        return db.delete(MovieTable.TABLE_NAME,MovieTable.MOVIE_ID_KEY + "=?",new String []{movie.getId()});
    }

    public Movie []  getAllMovies(){


        Cursor cursor =  db.query(MovieTable.TABLE_NAME ,
                new String []{MovieTable.MOVIE_ID_KEY,MovieTable.POSTER_KEY}
                ,null,null,null,null,null);

           Movie [] movies =new Movie[cursor.getCount()];

        if(cursor.moveToFirst()){

            int index=0;
            do {

                movies[index] = new Movie(null,null,cursor.getString(1),null,null,null,null,null,cursor.getString(0));


                ++index;
            }while(cursor.moveToNext());

        }

        return movies;

    }




}
