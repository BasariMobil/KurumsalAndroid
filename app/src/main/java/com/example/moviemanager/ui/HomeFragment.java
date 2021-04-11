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

import com.example.moviemanager.MainActivity;
import com.example.moviemanager.R;
import com.example.moviemanager.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MovieViewModel viewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        MovieAdapter adapter = new MovieAdapter(movie -> {
            ((MainActivity)requireActivity()).setCurrentMovie(movie);

        }, () -> ((MainActivity) requireActivity()).discoverMovieFromApi(MainActivity.currentDiscoverPage + 1));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setAdapter(adapter);
        viewModel.getDiscoverMovieList().observe(getViewLifecycleOwner(), adapter::setMovieList);
    }
}