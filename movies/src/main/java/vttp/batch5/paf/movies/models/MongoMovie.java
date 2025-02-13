package vttp.batch5.paf.movies.models;

import java.util.List;

public class MongoMovie {
    private String imdbId;
    private String title;
    private List<String> directors; // Can be a list
    private String overview;
    private String tagline;
    private List<String> genres;
    private Number imdbRating;
    private Number imdbVotes;

    
    
    public MongoMovie() {
    }

    public MongoMovie(String imdbId, String title, List<String> directors, String overview, String tagline,
            List<String> genres, Number imdbRating, Number imdbVotes) {
        this.imdbId = imdbId;
        this.title = title;
        this.directors = directors;
        this.overview = overview;
        this.tagline = tagline;
        this.genres = genres;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Number getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Number imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Number getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(Number imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    

    
}
