package com.example.mymovies.utils;

import androidx.appcompat.view.menu.MenuView;

import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {   // json to object
 /*   private int id;
    private int voteCount;
    private String title;
    private String orginalTitle;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double voteAverage;
    private String releaseDate;
*/
    //1 poluchit massiv json po klyuchu result
    private  static  final String KEY_RESULTS = "results";
    //-------------------------MASSIVE REWVIEWS--------------------------------------//
/* "id": 335983,
    "page": 1,
    "results": [
        {
            "author": "Gimly",
            "content": "I honestly don't know what everyone's  if Tom Hardy wasn't
            ve this one.\r\n\r\n_Final rating:★★★ - I liked it. Would personally
            recommend you give it a go._",
            "id": "5bd28c050e0a2616cf00459a",
            "url": "https://www.themoviedb.org/review/5bd28c050e0a2616cf00459a"
        },*/


    private  static  final String KEY_AUTHOR = "author";
    private  static  final String KEY_CONTENT= "content";
    //-------------------------MASSIVE VIDEOS--------------------------------------//

/*{
    "id": 537061,
    "results": [
        {
            "id": "5d079138c3a3680a5723d02e",
            "iso_639_1": "en",
            "iso_3166_1": "US",
            "key": "TlmfPB20Ln4",
            "name": "Steven Universe | Steven Universe The Movie Official Teaser | Cartoon Network",
            "site": "YouTube",
            "size": 1080,
            "type": "Teaser"
        },*/


    private  static  final String KEY_KEY_OF_VIDEO = "key";
    private  static  final String KEY_NAME = "name";


    private  static  final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v="; // YOUTUBE URL
    //-------------------------ALL INFORMATION OF FILMS--------------------------------------//

    private  static  final String KEY_VOTE_COUNT = "vote_count";
    private  static  final String KEY_ID = "id";
    private  static  final String KEY_TITLE = "title";
    private  static  final String KEY_ORIGINAL_TITLE = "original_title";
    private  static  final String KEY_OVERVIEW = "overview";
    private  static  final String KEY_POSTER_PATH= "poster_path";
    private  static  final String KEY_BACKDROP_PATH= "backdrop_path";
    private  static  final String KEY_VOTE_AVERAGE = "vote_average";
    private  static  final String KEY_RELEASE_DATE = "release_date";

    public static final String BASE_URL_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";


    //------------------------------------GET Reviews FROM JSON--------------------------------//
    public static ArrayList<Review> getReviewsFromJson(JSONObject jsonObject){
        ArrayList<Review> result = new ArrayList<>();
        if (jsonObject == null){
            return result;
        }
        try {
            JSONArray jsonArray =  jsonObject.getJSONArray(KEY_RESULTS);//poluchim jsonARRAY
            for (int  i  = 0; i< jsonArray.length();i++) { //poluchim filmi
                JSONObject objectReview = jsonArray.getJSONObject(i);
                String author = objectReview.getString(KEY_AUTHOR);
                String content = objectReview.getString(KEY_CONTENT);
                Review review = new Review(author, content);
                result.add(review);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    //------------------------------------GET TRAILER VIDEOS FROM JSON--------------------------------//

    public static ArrayList<Trailer> getTrailerFromJson(JSONObject jsonObject){
        ArrayList<Trailer> result = new ArrayList<>();
        if (jsonObject == null){
            return result;
        }
        try {
            JSONArray jsonArray =  jsonObject.getJSONArray(KEY_RESULTS);//poluchim jsonARRAY
            for (int  i  = 0; i< jsonArray.length();i++) { //poluchim filmi
                JSONObject jsonObjectTrailers = jsonArray.getJSONObject(i);
                String key = BASE_YOUTUBE_URL + jsonObjectTrailers.getString(KEY_KEY_OF_VIDEO); // dannim klyuchom ne mojem posmotret video => YOUTUBE URL
                String name = jsonObjectTrailers.getString(KEY_NAME);
                Trailer trailer = new Trailer(key,name);
                result.add(trailer);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //------------------------------------GET MOVIES FROM JSON--------------------------------//


    //zapros k bazse poluchim massiv   filmami
    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject){
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null){
             return result;
        }
        try {
            JSONArray jsonArray =  jsonObject.getJSONArray(KEY_RESULTS);//poluchim jsonARRAY
            for (int  i  = 0; i< jsonArray.length();i++) { //poluchim filmi
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id  = objectMovie.getInt(KEY_ID);
                int voteCount = objectMovie.getInt(KEY_VOTE_COUNT);
                 String title = objectMovie.getString(KEY_TITLE);
                 String originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE);
                 String overview = objectMovie.getString(KEY_OVERVIEW);
                 String posterPath = BASE_URL_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                 String bigPosterPath = BASE_URL_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                 String backdropPath= objectMovie.getString(KEY_BACKDROP_PATH);
                 double voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE);
                 String releaseDate= objectMovie.getString(KEY_VOTE_AVERAGE);

                 // poluchli vse dannie sozdayom object MOVIE
                Movie movie = new  Movie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage,releaseDate);
                result.add(movie);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
