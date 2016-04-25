package mno.mohamed_youssef.movie1;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import data.MoviesDatabase;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainMoviesFragment extends Fragment {


    GridView gridView;
    PostersAdapter mMovieAdepter;
    ArrayList<String> movies_id ;
    private Movie [] movies ;
    private MoviesDatabase moviesDatabase;
    private MovieDetailFragment movieDetailFragment;
    private JsonMoviesConnection jsonMoviesConnection;
    private static int indexSelectedMovie = 0;
    private  String sort_by;
    private  SharedPreferences sharedPreferences;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState != null) {

            indexSelectedMovie = savedInstanceState.getInt("indexSelectedmovie");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View veiw  = inflater.inflate(R.layout.main_movies_fragment, container, false);
        movies_id = new ArrayList<>();

        gridView = (GridView) veiw.findViewById(R.id.gridView);

         gridView.setAdapter(mMovieAdepter);
         //itemSelected();


        return veiw;
    }



    @Override
    public void onStart() {
        super.onStart();



        movieDetailFragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_detail);

        movieUpdate();



    }

    @Override
    public void onSaveInstanceState(Bundle bundleSaveIndexMovie) {
        super.onSaveInstanceState(bundleSaveIndexMovie);


        bundleSaveIndexMovie.putInt("indexSelectedmovie", indexSelectedMovie);

    }



    public void itemMovieSelected(){

          //  Log.d("tag",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+movies_id.size());

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (!movies_id.isEmpty()) {
                        if (movieDetailFragment != null && movieDetailFragment.isVisible()) {

                            if (indexSelectedMovie >= movies_id.size()) {
                                indexSelectedMovie = 0;
                            } else {
                                indexSelectedMovie = position;

                            }

                            movieDetailFragment.replaceContent(movies_id.get(indexSelectedMovie));

                        } else {
                            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT, movies_id.get(position));
                            startActivity(intent);
                        }
                    }

                }
            });


            if (movieDetailFragment != null && !movies_id.isEmpty()) {


                if (indexSelectedMovie >= movies_id.size())
                    indexSelectedMovie = 0;

                movieDetailFragment.replaceContent(movies_id.get(indexSelectedMovie));
            }



    }




    //Movie Update


    public void movieUpdate()
    {
        movies_id.clear();

        FetchMoviesTask  mMonieTask = new FetchMoviesTask();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = sharedPreferences.getString(getString(R.string.key_sort_by),getString(R.string.pref_popular));


        if(sort.equals(getString(R.string.pref_favourite))) {
            moviesDatabase = new MoviesDatabase(getActivity());
            moviesDatabase.open();
            movies = moviesDatabase.getAllMovies();
            ArrayList<String> posters = new ArrayList<>();
            for (Movie movie : movies){
                String poster =movie.getPoster();
                posters.add(poster);
                movies_id.add(movie.getId());
            }


                mMovieAdepter = new PostersAdapter(getActivity(), posters.toArray(new String[posters.size()]));
                gridView.setAdapter(mMovieAdepter);


            moviesDatabase.close();


            itemMovieSelected();

            // first poster in detail

           // if (movieDetailFragment != null) movieDetailFragment.replaceContent(movies_id.get(0));

        }
        else{
            mMonieTask.execute(sort);
        }


    }



    // background process

    public class FetchMoviesTask extends AsyncTask<String, Void, String []> {


        @Override
        protected String[] doInBackground(String... params) {

            try {

                Uri buildUri = Uri.parse("https://api.themoviedb.org/3/movie").buildUpon().appendPath(params[0]).build();

                jsonMoviesConnection = new  JsonMoviesConnection(buildUri);



                return getMovieFromJson(jsonMoviesConnection.getStirngJson());

            } catch (Exception e) {
                e.printStackTrace();
               /*  Toast.makeText(getActivity()," No Network Connection ", Toast.LENGTH_SHORT).show();*/
               // Log.d("tag", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> bbjh" + e.getMessage());
                return null;

            }

        }


        @Override
        protected void onPostExecute(String[] posters) {
            super.onPostExecute(posters);

            if(posters != null)
            {
               // Log.d("tag", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + result);


                mMovieAdepter =new PostersAdapter(getActivity(),posters);
                gridView.setAdapter(mMovieAdepter);

                // Selected Movie in detail


                itemMovieSelected();

            }


        }


        //data from json

        private String[] getMovieFromJson(String moviesJsonStr)
                throws JSONException {

            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            String[] posters = new String[movieArray.length()];
            for(int i =0 ;i < movieArray.length() ;++i)
            {

                 JSONObject movieItem =movieArray.getJSONObject(i);
                 posters[i]=movieItem.getString("poster_path");
                 movies_id.add(movieItem.getString("id"));

            }


            //String[] resultStrs = new String[];

            return posters;

        }




    }

    public static void setIndexSelectedMovie(int indexSelectedMovie) {
        MainMoviesFragment.indexSelectedMovie = indexSelectedMovie;
    }



}
