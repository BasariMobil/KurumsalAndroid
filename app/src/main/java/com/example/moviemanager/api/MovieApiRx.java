package com.example.moviemanager.api;

import com.example.moviemanager.model.Movie;
import com.example.moviemanager.model.SearchResponse;

import java.util.List;

import io.reactivex.Single;

public class MovieApiRx {

    public static Single<Movie> getMovie(int movieId) {
        return MovieApi.getInstance().getMovie(movieId, ApiUtils.API_KEY);
    }

    public static Single<SearchResponse> searchMovie(String query, int page) {
        return MovieApi.getInstance().searchMovie(query, ApiUtils.API_KEY, page);
    }

    public static Single<SearchResponse> discoverMovie(int page) {
        return MovieApi.getInstance().discoverMovie(ApiUtils.API_KEY, page);
    }
}
