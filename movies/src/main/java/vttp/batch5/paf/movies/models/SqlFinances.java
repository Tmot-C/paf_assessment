package vttp.batch5.paf.movies.models;

import java.math.BigDecimal;

public class SqlFinances {

    private String imdbId;
    private BigDecimal revenue;
    private BigDecimal budget;
    
    public SqlFinances() {
    }

    
    public SqlFinances(String imdbId, BigDecimal revenue, BigDecimal budget) {
        this.imdbId = imdbId;
        this.revenue = revenue;
        this.budget = budget;
    }


    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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
    
    
    
}
