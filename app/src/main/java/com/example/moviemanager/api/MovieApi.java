package com.example.moviemanager.api;

import com.example.moviemanager.model.Movie;
import com.example.moviemanager.model.SearchResponse;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/{movie_id}")
    Single<Movie> getMovie(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("search/movie")
    Single<SearchResponse> searchMovie(@Query("query") String query, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("discover/movie")
    Single<SearchResponse> discoverMovie(@Query("api_key") String apiKey, @Query("page") int page);

    static MovieApi getInstance() {
        return ApiUtils.buildApi(ApiUtils.BASE_URL, MovieApi.class);
    }
}
