package com.example.moviemanager.db;

import android.app.Application;

import com.example.moviemanager.model.Movie;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class MovieService {

    private final Movie.Dao movieDao;

    public enum Specs {
        FAVORITE, WATCHLIST
    }

    public MovieService(Application application) {
        MovieManagerDatabase database = MovieManagerDatabase.getInstance(application);
        movieDao = database.movieDao();
    }

    public Maybe<Movie> syncMovie(Movie movie, Specs specs, boolean enable) {
        return movieDao.getMaybeMovie(movie.getId())
                .flatMap(movie1 -> Completable.fromAction(() -> {
                    if (specs.equals(Specs.FAVORITE)) {
                        movie1.setFavorite(enable);
                    } else {
                        movie1.setWatchlist(enable);
                    }
                    movieDao.update(movie1);
                }).andThen(Maybe.just(movie1)))
                .isEmpty()
                .flatMapCompletable(isEmpty -> isEmpty ? Completable.fromAction(() -> {
                    if (specs.equals(Specs.FAVORITE)) {
                        movie.setFavorite(enable);
                    } else {
                        movie.setWatchlist(enable);
                    }
                    movieDao.insert(movie);
                }) : Completable.complete())
                .andThen(movieDao.getMaybeMovie(movie.getId()));

    }

    public Maybe<Movie> getMovie(int movieId) {
        return movieDao.getMaybeMovie(movieId);
    }

    private Completable deleteReduntant(Movie movie) {
        return movieDao.getMaybeMovie(movie.getId())
                .flatMapCompletable(movie1 -> Completable.fromAction(() -> {
                    if (!movie1.isFavorite() && !movie1.isWatchlist()) {
                        movieDao.delete(movie1);
                    }
                }));
    }
}
