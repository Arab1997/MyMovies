package com.example.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.adapters.MovieAdapter;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.utils.JsonUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private Switch switchSort;
    private RecyclerView recyclerViewPoster;
    private MovieAdapter movieAdapter;
    private TextView textViewTopRated, textViewPopularity;
    private ProgressBar progressBarLoading;


    private MainViewModel viewModel;

    private static final int LOADER_ID = 133; // lyuboy chislo
    private LoaderManager loaderManager;

    private static int page = 1;
    private static int methodOfSort;
    private static boolean isLoading = false;  // metod ne vizivatsta neskolko ras poka dannie pogruitsia
    private static String lang;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentToFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pattern Singleton mi poluchaem exemplyar zagruzchika katoriy otvechaet za vse zagruzke katoriy prisxodet na prilojenie
        loaderManager = LoaderManager.getInstance(this);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        progressBarLoading = findViewById(R.id.progressBArLoading);
        switchSort = findViewById(R.id.switch1);
        recyclerViewPoster = findViewById(R.id.recyclerViewPosters);
        recyclerViewPoster.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter();

        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY,1,lang);
        ArrayList<Movie> movies = JsonUtils.getMoviesFromJSON(jsonObject);
        movieAdapter.setMovies(movies);

        recyclerViewPoster.setAdapter(movieAdapter);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) { // sortirovka
                page = 1;
                setMethodOfSort(b);
            }
        });
        switchSort.setChecked(false);


        //--------------------------// detail activity //----------------------------------------//
        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
                //  Toast.makeText(MainActivity.this, "Clicked: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        //--------------------------//  PAGE 2 //-----------------------------------------//

        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {// konse spiske vizivaetsya i budet proveriro yesli zagruzka ne nachilos
            @Override
            // vizivaetsya downloadData(methodOfSort, page);
            public void onReachEnd() {
                if (!isLoading) { // yesli pogruzka ne nachilos togda mi vipolnyayem  vse deystvie
                    downloadData(methodOfSort, page);  // sortirofka i page
                    // Toast.makeText(MainActivity.this, "Конец списка", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //movieAdapter.setMovies(movies);
                if (page == 1) {
                    movieAdapter.setMovies(movies);  // yesli start app ili izmenili metod sartirofka to nacheniya peremennoe page ustanavitsya rvno 1 i mi start download data
                    //yesli interneta net podgruzga ne budet danniy vozmutsya iz baza dannix
                }
            }
        });

    }

    public void onClickSetPopularity(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    public void onClickSetTopRated(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }

    private void setMethodOfSort(boolean isTopRated) {
        int methodOfSort;
        // mi vibrali sposob sartirofki
        if (isTopRated) {
            textViewTopRated.setTextColor(getResources().getColor(R.color.colorAccent));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white_color));
            methodOfSort = NetworkUtils.TOP_RATED; // ustnavili eyo methodOfSort
        } else {
            methodOfSort = NetworkUtils.POPULARITY;
            textViewPopularity.setTextColor(getResources().getColor(R.color.colorAccent));
            textViewTopRated.setTextColor(getResources().getColor(R.color.white_color));
        }
       // downloadData(methodOfSort, page);// page =1  //zapustili zagruzka dannix  stranitsu 1 page 1
        downloadData(methodOfSort, page);// page =1  //zapustili zagruzka dannix  stranitsu 1 page 1
    }


    private void downloadData(int methodOfSort, int page) {
        URL url = NetworkUtils.buildURL(methodOfSort, page, lang);//formiruyem Url
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);//zapustit agruzchik

    }

    // -----------------------------------Zagruzchik---------------------------------------------//
    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle bundle) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, bundle); // nash zagruzchik
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {// pri nachali zagruzki  vizivaem etot metod
                progressBarLoading.setVisibility(View.VISIBLE);
               isLoading = true;// peremennoe prisvoet znacheniya  true
            }
        });
        return jsonLoader; // kogda zagrujena vse filmi  vizivaetsya metod "onLoadFinished"
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject jsonObject) {
        ArrayList<Movie> movies = JsonUtils.getMoviesFromJSON(jsonObject);
        if (movies != null && !movies.isEmpty()) { // all movies insert to database and adapter
            if (page == 1) {
               // Log.i("MyResult", String.valueOf(page));
                viewModel.deleteAllMovies();//delete last data
               movieAdapter.clear();
            }
            for (Movie movie : movies) {  // paste data
                viewModel.insertMovie(movie);
            //    Log.i("MyResult", String.valueOf(movie));
            }
            movieAdapter.addMovies(movies);// add new films
            page++;
        }
       isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}
