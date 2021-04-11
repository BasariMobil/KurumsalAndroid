package com.example.moviemanager.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviemanager.db.MovieManagerDatabase;
import com.example.moviemanager.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieManagerDatabase database = MovieManagerDatabase.getInstance(application);
        favorites = database.movieDao().getFavorites();
        watchlist = database.movieDao().getWatchlist();
    }

    private final MutableLiveData<List<Movie>> discoverMovieList = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> searchMovieList = new MutableLiveData<>();
    private final MutableLiveData<Movie> currentMovie = new MutableLiveData<>();
    private final LiveData<List<Movie>> favorites;
    private final LiveData<List<Movie>> watchlist;

    public MutableLiveData<List<Movie>> getDiscoverMovieList() {
        return discoverMovieList;
    }


    public MutableLiveData<Movie> getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(Movie currentMovie) {
        this.currentMovie.postValue(currentMovie);
    }

    public MutableLiveData<List<Movie>> getSearchMovieList() {
        return searchMovieList;
    }

    public void setSearchMovieList(List<Movie> searchMovieList) {
        if (this.searchMovieList.getValue() != null) {
            this.searchMovieList.getValue().addAll(searchMovieList);
            this.searchMovieList.postValue(this.searchMovieList.getValue());

        } else {
            this.searchMovieList.postValue(searchMovieList);
        }
    }

    public void setDiscoverMovieList(List<Movie> discoverMovieList) {
        if (this.discoverMovieList.getValue() != null) {
            this.discoverMovieList.getValue().addAll(discoverMovieList);
            this.discoverMovieList.postValue(this.discoverMovieList.getValue());

        } else {
            this.discoverMovieList.postValue(discoverMovieList);
        }
    }

    public void clearSearchList() {
        searchMovieList.postValue(new ArrayList<>());
    }

    public LiveData<List<Movie>> getFavorites() {
        return favorites;
    }

    public LiveData<List<Movie>> getWatchList() {
        return watchlist;
    }
}
