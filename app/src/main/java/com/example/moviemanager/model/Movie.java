package com.example.moviemanager.model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Entity
public class Movie {

    @PrimaryKey
    private int id;
    private String overview;
    private String release_date;
    private String title;
    private double vote_average;
    private String backdrop_path;
    private boolean favorite = false;
    private boolean watchlist = false;

    public Movie() {
    }


    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }

    @androidx.room.Dao
    public interface Dao {

        @Insert
        void insert(Movie movie);

        @Update
        void update(Movie movie);

        @Delete
        void delete(Movie movie);

        @Query("SELECT * FROM Movie WHERE id = :id")
        Maybe<Movie> getMaybeMovie(int id);

        @Query("SELECT * FROM Movie WHERE id = :id")
        Movie getMovie(int id);

        @Query("SELECT * FROM Movie WHERE favorite=1")
        LiveData<List<Movie>> getFavorites();

        @Query("SELECT * FROM Movie WHERE watchlist=1")
        LiveData<List<Movie>> getWatchlist();
    }


}
