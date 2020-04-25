package com.example.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM favourite_movies")  // movies => favourite_movies
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    @Query("SELECT * FROM movies WHERE  id == :movieId")// vovrashaet exmaple filmov po ego id
    Movie getMovieById(int movieId);



    @Query("SELECT * FROM favourite_movies WHERE  id == :movieId")// vovrashaet exmaple filmov po ego id
    FavouriteMovie getFavouriteMovieById(int movieId);



    @Query("DELETE FROM movies")
    void  deleteAllMovies();

    @Insert
    void  insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);


    @Insert
    void  insertFavouriteMovie(FavouriteMovie favouriteMovie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie favouriteMovie);
}
