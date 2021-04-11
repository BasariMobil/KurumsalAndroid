package com.example.moviemanager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.moviemanager.api.MovieApiRx;
import com.example.moviemanager.db.MovieService;
import com.example.moviemanager.model.Movie;
import com.example.moviemanager.ui.MovieViewModel;
import com.example.moviemanager.ui.RxSearchObservable;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private NavController navController;
    private NavDestination destination;
    private MovieViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private MovieService movieService;
    public static int currentSearchPage = 1;
    public static int currentDiscoverPage = 1;
    public static String lastQuery = "";
    private final Set<Integer> topLevelDestinations = new HashSet<Integer>() {{
        add(R.id.homeFragment);
        add(R.id.watchListFragment);
        add(R.id.favoritesFragment);
    }};
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(findViewById(R.id.fragment));
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieService = new MovieService(getApplication());
        compositeDisposable = new CompositeDisposable();
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            this.destination = destination;
            toolbar.getMenu().setGroupVisible(R.id.search_group, destination.getId() == R.id.homeFragment || destination.getId() == R.id.searchFragment);
            if (destination.getId() == R.id.movieFragment) {
                viewModel.getCurrentMovie().observe(MainActivity.this, movie -> toolbar.setTitle(movie.getTitle()));
            }
        });
        discoverMovieFromApi(currentDiscoverPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (destination.getId() == R.id.searchFragment) {
                    navController.popBackStack(R.id.homeFragment, false);
                }
                if (destination.getId() == R.id.movieFragment) {
                    navController.popBackStack();
                }
                return true;
            }
        });
        Disposable disposable = RxSearchObservable.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::searchMovie,
                        error -> Log.e(TAG, "onCreateOptionsMenu: ", error));
        compositeDisposable.add(disposable);
        return true;
    }

    public void collapseSearchView() {
        item.collapseActionView();
    }


    public void searchMovie(String query) {
        if (destination.getId() == R.id.homeFragment && !query.isEmpty()) {
            navController.navigate(R.id.searchFragment);
            currentSearchPage = 1;
        }
        searchMovieFromApi(query, currentSearchPage);
    }

    public void discoverMovieFromApi(int page) {
        currentDiscoverPage++;
        Disposable disposable = MovieApiRx.discoverMovie(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResponse -> viewModel.setDiscoverMovieList(searchResponse.getResults()),
                        error -> Log.e(TAG, "discoverMovieFromApi: ", error));
        compositeDisposable.add(disposable);
    }

    public void searchMovieFromApi(String query, int page) {
        if (!lastQuery.equals(query)) {
            viewModel.clearSearchList();
        }
        lastQuery = query;
        if (query.isEmpty()) {
            currentSearchPage = 1;
            return;
        }
        currentSearchPage++;
        Disposable disposable = MovieApiRx.searchMovie(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResponse -> viewModel.setSearchMovieList(searchResponse.getResults()),
                        error -> Log.e(TAG, "searchMovie: ", error));
        compositeDisposable.add(disposable);
    }

    public void setCurrentMovie(Movie movie) {
        Disposable disposable = movieService.getMovie(movie.getId())
                .doAfterSuccess(movie1 -> viewModel.setCurrentMovie(movie1))
                .isEmpty()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isEmpty -> {
                    if (isEmpty) viewModel.setCurrentMovie(movie);
                    navController.navigate(R.id.movieFragment);
                }, error -> Log.e(TAG, "setCurrentMovie: ", error));
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}