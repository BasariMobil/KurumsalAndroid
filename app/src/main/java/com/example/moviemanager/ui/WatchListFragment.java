package com.example.moviemanager.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviemanager.R;
import com.example.moviemanager.databinding.FragmentFavoritesBinding;
import com.example.moviemanager.databinding.FragmentWatchListBinding;
import com.example.moviemanager.db.MovieService;

import org.jetbrains.annotations.NotNull;


public class WatchListFragment extends Fragment {

    private FragmentWatchListBinding binding;

    public WatchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWatchListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MovieViewModel viewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        MovieAdapter adapter = new MovieAdapter(movie -> {
            viewModel.setCurrentMovie(movie);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.movieFragment);
        }, () -> {
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setAdapter(adapter);
        viewModel.getWatchList().observe(getViewLifecycleOwner(), movies -> {
            adapter.setMovieList(movies);
            binding.noItem.setVisibility(movies.size() == 0 ? View.VISIBLE : View.GONE);
        });
    }
}