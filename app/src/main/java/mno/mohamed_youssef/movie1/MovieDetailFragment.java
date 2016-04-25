package mno.mohamed_youssef.movie1;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import data.MoviesDatabase;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {


    private String movie_id;
    private Map<String , String> trailersdata;

    private TextView titleView;
    private TextView dateView ;
    private TextView runtimeView;
    private TextView  voteView;
    private TextView  overviewView;
    private ImageView posterView;
    private Button favouriteButton;
    private LinearListView trailersView;
    private LinearListView reviewsListViewView;
    private MoviesDatabase moviesDatabase;
    private Movie movie;
    private boolean  isMovieFound;
    private JsonMoviesConnection jsonMoviesConnection;


    public  void initialization(View view){


        titleView = (TextView)view.findViewById(R.id.title_label);
        dateView = (TextView)view.findViewById(R.id.date);
        runtimeView = (TextView)view.findViewById(R.id.runtime);
        voteView = (TextView)view.findViewById(R.id.vote);
        overviewView = (TextView)view.findViewById(R.id.overview);
        posterView = (ImageView)view.findViewById(R.id.poster);
        favouriteButton = (Button)view.findViewById(R.id.favourite);
        trailersView =(LinearListView)view.findViewById(R.id.trailers);
        reviewsListViewView=(LinearListView)view.findViewById(R.id.reviews);


        moviesDatabase = new MoviesDatabase(getActivity());



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.movies_detail_fragment, container, false);

        initialization(view);

        moviesDatabase.open();

        final Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            movie_id = intent.getExtras().getString(Intent.EXTRA_TEXT);



        }


        trailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {

                String[] uriTrailers = trailersdata.values().toArray(new String[trailersdata.size()]);

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch")
                        .buildUpon().appendQueryParameter("v", uriTrailers[position]).build()));
            }
        });




        favouriteButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (movie != null) {


                    if (isMovieFound) {


                        moviesDatabase.removeMovie(movie);
                        favouriteButton.setText("Make As \nFavourite");
                        favouriteButton.setBackgroundColor(Color.GRAY);
                        isMovieFound = false;



                    } else {


                        moviesDatabase.insertMovie(movie);
                        favouriteButton.setText("Remove From \nFavourite ");
                        favouriteButton.setBackgroundColor(Color.YELLOW);

                        isMovieFound = true;


                    }


                }


            }
        });


        return view;
    }





    @Override
    public void onStart() {
        super.onStart();


        findFavouriteMovie();
        movieUpdate();

    }


    public void movieUpdate()
    {

        FetchMovieTask  mMonieTask = new FetchMovieTask();
        mMonieTask.execute(movie_id);

    }



    public class FetchMovieTask extends AsyncTask<String , Void, Movie> {


        @Override
        protected Movie doInBackground(String... params) {




            String movieJsondetail = null;
            String movieJsonReview=null;
            String movieJsonTrailers=null;

            Uri buildUri = Uri.parse("https://api.themoviedb.org/3/movie").buildUpon().appendPath(params[0]).build();

            jsonMoviesConnection = new  JsonMoviesConnection(buildUri);




            movieJsondetail =  jsonMoviesConnection.getStirngJson();



            //fetch reviews

            Uri buildUriReview = buildUri.buildUpon().appendPath("reviews").build();

            jsonMoviesConnection = new  JsonMoviesConnection(buildUriReview);
            movieJsonReview = jsonMoviesConnection.getStirngJson();


            //fetch trailers

            Uri buildUriVideos = buildUri.buildUpon().appendPath("videos").build();
            jsonMoviesConnection = new  JsonMoviesConnection(buildUriVideos);
            movieJsonTrailers = jsonMoviesConnection.getStirngJson();


            try {



                return getMovieFromJson(movieJsondetail, movieJsonTrailers, movieJsonReview);

            }catch (Exception e){


               // Toast.makeText(getActivity(), " No Network Connection ", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);

            if(movie != null)
            {



                trailersdata = movie.getTrailers();
                Set trailersdataSet = trailersdata.keySet();
                String [] trailersdatastring =  ( String []) trailersdataSet.toArray(new String[trailersdata.size()]);

                ArrayAdapter<String> trailersAdapter = new ArrayAdapter<String>(getActivity(),R.layout.trailers,R.id.trailer,
                        trailersdatastring);


                titleView.setText(movie.getTitle());
                dateView.setText(movie.getRelease_date().substring(0, 4));
                overviewView.setText(movie.getOverview());
                voteView.setText(movie.getVote_average());
                runtimeView.setText(movie.getRuntime()+" min");

                Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w130" + movie.getPoster()).into(posterView);
                trailersView.setAdapter(trailersAdapter);
                reviewsListViewView.setAdapter(new ReviewsAdapter(getActivity(),movie.getReview()));





           }


        }


        // movie from json

        private Movie getMovieFromJson(String movieJsondetail, String movieJsonTrailers, String movieJsonReview)
                throws JSONException {

            JSONObject movieJsondetailObject = new JSONObject(movieJsondetail);
            JSONObject movieJsonTrailersObject = new JSONObject(movieJsonTrailers);
            JSONObject movieJsonReviewobject = new JSONObject(movieJsonReview);



            //trailers

            JSONArray movieJsonTrailersArray = movieJsonTrailersObject.getJSONArray("results");

            Map<String,String> trailersjsondata = new LinkedHashMap<>();
           // String[] trailers = new String[movieJsonTrailersArray.length()];
            for(int i =0 ;i < movieJsonTrailersArray.length() ;++i)
            {

                JSONObject trailer =movieJsonTrailersArray.getJSONObject(i);
                trailersjsondata.put(trailer.getString("name"), trailer.getString("key"));

            }



            //reviews

            JSONArray movieJsonReviewArray = movieJsonReviewobject.getJSONArray("results");

            Map<String,String> reviewsdata = new LinkedHashMap<>();
            for(int i = 0 ;i < movieJsonReviewArray.length() ;++i)
            {

                JSONObject review =movieJsonReviewArray.getJSONObject(i);
                reviewsdata.put(review.getString("author"), review.getString("content"));

            }




            movie = new Movie(movieJsondetailObject.getString("title"),movieJsondetailObject.getString("release_date"),
                    movieJsondetailObject.getString("poster_path"),movieJsondetailObject.getString("vote_average"),
                    movieJsondetailObject.getString("overview"),movieJsondetailObject.getString("runtime"),
                   trailersjsondata,reviewsdata , movieJsondetailObject.getString("id"));


            return movie;

        }




    }

    public void findFavouriteMovie()
    {

        isMovieFound =false;

        for( Movie movie: moviesDatabase.getAllMovies())
        {
            if(movie.getId().equals(movie_id)) isMovieFound =true;

        }


        if(isMovieFound){


            favouriteButton.setText("Remove From \n Favourite ");
            favouriteButton.setBackgroundColor(Color.YELLOW);

        }
        else{
        favouriteButton.setText("Make As \nFavourite");
            favouriteButton.setBackgroundColor(Color.GRAY);

        }


    }


    public void replaceContent(String movie_id){


        this.movie_id = movie_id;
        findFavouriteMovie();
        movieUpdate();


    }



}
