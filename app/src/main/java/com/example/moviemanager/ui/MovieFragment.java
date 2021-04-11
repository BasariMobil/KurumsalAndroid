package com.example.moviemanager.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moviemanager.R;
import com.example.moviemanager.databinding.FragmentMovieBinding;
import com.example.moviemanager.db.MovieService;
import com.example.moviemanager.model.Movie;
import com.google.android.material.button.MaterialButton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MovieFragment extends Fragment {

    private FragmentMovieBinding binding;
    private MovieService movieService;
    private static final String TAG = "MovieFragment";
    private CompositeDisposable compositeDisposable;
    private UiHandler handler;
    private MovieViewModel viewModel;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new UiHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        binding.setHandler(handler);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieService = new MovieService(requireActivity().getApplication());
        compositeDisposable = new CompositeDisposable();
        viewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        viewModel.getCurrentMovie().observe(getViewLifecycleOwner(), binding::setMovie);
    }

    @BindingAdapter("android:movieRate")
    public static void getMovieRate(TextView textView, Movie movie) {
        if (movie == null) return;
        textView.setText(String.valueOf(movie.getVote_average()));
    }

    @BindingAdapter("android:specButtonText")
    public static void getSpecButtonText(MaterialButton materialButton, Movie movie) {
        if (movie == null) return;
        if (materialButton.getId() == R.id.buttonFavorite) {
            materialButton.setText(movie.isFavorite() ? R.string.remove_from_favorites : R.string.add_to_favorites);
        } else {
            materialButton.setText(movie.isWatchlist() ? R.string.remove_from_watchlist : R.string.add_to_watchlist);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    public class UiHandler {
        public void syncMovie(Movie movie, MovieService.Specs specs, boolean enable) {
            Disposable disposable = movieService.syncMovie(movie, specs, enable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movie1 -> viewModel.setCurrentMovie(movie1),
                            error -> Log.e(TAG, "setFavorite: ", error));
            compositeDisposable.add(disposable);
        }
    }
}