package com.example.moviemanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviemanager.model.Movie;

@Database(entities = {Movie.class}, version = 10, exportSchema = false)
public abstract class MovieManagerDatabase extends RoomDatabase {
    public static synchronized MovieManagerDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieManagerDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), MovieManagerDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    private static MovieManagerDatabase instance;

    public abstract Movie.Dao movieDao();
}
