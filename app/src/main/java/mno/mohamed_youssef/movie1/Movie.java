package mno.mohamed_youssef.movie1;


import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mohamed Yossif on 16/04/2016.
 */
public final class Movie {
    private String id;
    private String title;
    private String release_date;
    private String poster;
    private String vote_average;
    private String overview;
    private Map<String,String> trailers;
    private String runtime;
    private Map<String,String> reviews;


    public Movie(String title, String release_date,
          String poster, String vote_average,
          String overview, String runtime ,
          Map<String , String> trailers,Map<String ,String> reviews , String id) {




        this.title = title;
        this.overview = overview;
        this.poster = poster;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.trailers =trailers;
        this.runtime = runtime;
        this.reviews = reviews;
        this.id = id;


    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster() {
        return poster;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public Map<String,String> getTrailers() {
        return trailers;
    }

    public String getRuntime() {
        return runtime;
    }

    public Map<String, String> getReview() {
        return reviews;
    }

    public String getId() {
        return id;
    }
}
