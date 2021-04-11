package com.example.moviemanager.model;

import java.util.List;

public class SearchResponse {
    private int page;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }
}
