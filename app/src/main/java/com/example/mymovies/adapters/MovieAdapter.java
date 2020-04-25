package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private OnReachEndListener onReachEndListener;
    private OnPosterClickListener onPosterClickListener;
    private List<Movie> movies;

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public interface OnPosterClickListener{
        void  onPosterClick(int position);
    }

    public interface OnReachEndListener{
        void onReachEnd();            // spiskani oxiriga borganda
  }


    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }


    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {  // position = 0
        if (movies.size() >= 20 && position > movies.size() -4 && onReachEndListener != null){// kogda mi dostigli konsa spiska | dobavili uvicheniya stranitsi "movies.size() >= 20"
            onReachEndListener.onReachEnd();
        }
        // beryom imaveView i ustanavim  photo iz filmov
        //yesli v api ne polniy address

        //example https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg
        //                      base url   / razmer/ put k faylu
        Movie movie = movies.get(position);
     //   ImageView imageView = holder.imageViewSmallPoster
        Picasso.get().load(movie.getPosterPath()).into(holder.imageViewSmallPoster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewSmallPoster;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {    //har bir itemga kirish uchun
                @Override
                public void onClick(View view) {
                    if (onPosterClickListener != null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;   // ustanavili
        notifyDataSetChanged();
    }


    public void addMovies(List<Movie> movies) { // pagination ne udalyaya stariy massiv list
        this.movies.addAll(movies);    //dobavim vse spiske
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
