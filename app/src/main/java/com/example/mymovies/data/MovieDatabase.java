package com.example.mymovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities ={Movie.class, FavouriteMovie.class}, version = 3,  exportSchema = false) //peredayom parametri // add too db 2nd table
public abstract class MovieDatabase extends RoomDatabase {
    private static final String DB_NAME= "movies";
    private static MovieDatabase database;
    private static final Object LOCK = new Object();

    public static MovieDatabase getInstance(Context context){
        synchronized (LOCK){
        if (database == null){
            database= Room.databaseBuilder(context , MovieDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()// versiya izminili dobavili shtobi stariy udalyat i dobavit novie
                    .build();
        }
        return database;
     }
    }



    public abstract MovieDao movieDao();  //vozvrashaet Dao
}
