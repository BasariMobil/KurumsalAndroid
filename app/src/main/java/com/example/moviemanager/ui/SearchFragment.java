package com.example.moviemanager.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviemanager.MainActivity;
import com.example.moviemanager.R;
import com.example.moviemanager.api.MovieApiRx;
import com.example.moviemanager.databinding.FragmentSearchBinding;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MovieViewModel viewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        MovieAdapter adapter = new MovieAdapter(movie -> {
            ((MainActivity) requireActivity()).setCurrentMovie(movie);
            ((MainActivity) requireActivity()).collapseSearchView();
        }, () -> ((MainActivity) requireActivity()).searchMovieFromApi(MainActivity.lastQuery, MainActivity.currentSearchPage + 1));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setAdapter(adapter);
        viewModel.getSearchMovieList().observe(getViewLifecycleOwner(), adapter::setMovieList);
    }
}