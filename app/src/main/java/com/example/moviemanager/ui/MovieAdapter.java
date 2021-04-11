package com.example.moviemanager.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviemanager.R;
import com.example.moviemanager.api.ApiUtils;
import com.example.moviemanager.databinding.ItemMovieBinding;
import com.example.moviemanager.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList = new ArrayList<>();
    private final MovieListener listener;
    private final Runnable getMore;

    public MovieAdapter(MovieListener listener, Runnable getMore) {
        this.listener = listener;
        this.getMore = getMore;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_movie, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.binding.setMovie(movie);
        holder.binding.movieItem.setOnClickListener(v -> listener.onClick(movie));
        if (movieList.size() - 5 == position) {
            getMore.run();
        }
    }

    @BindingAdapter(value = {"context", "movieImage"})
    public static void getMovieImage(ImageView imageView, Context context, Movie movie) {

        if (movie != null && movie.getBackdrop_path() != null && !movie.getBackdrop_path().isEmpty()) {
            Glide.with(context).load(ApiUtils.IMAGE_URL + movie.getBackdrop_path()).into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ItemMovieBinding binding;

        public MovieViewHolder(@NonNull ItemMovieBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
