package vttp.batch5.paf.movies.models;

import java.math.BigDecimal;
import java.sql.Date;


public class SqlMovie {

    private String imdbId;
    private Number voteAverage;
    private Number voteCount;
    private Date releaseDate;
    private BigDecimal revenue;
    private BigDecimal budget; 
    private Number runtime;
    
    public SqlMovie() {
    }

    

    public SqlMovie(String imdbId, Number voteAverage, Number voteCount, Date releaseDate, BigDecimal revenue,
            BigDecimal budget, Number runtime) {
        this.imdbId = imdbId;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.budget = budget;
        this.runtime = runtime;
    }



    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Number getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Number voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Number getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Number voteCount) {
        this.voteCount = voteCount;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public Number getRuntime() {
        return runtime;
    }

    public void setRuntime(Number runtime) {
        this.runtime = runtime;
    }

    
    
}
