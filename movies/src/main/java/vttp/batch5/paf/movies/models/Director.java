package vttp.batch5.paf.movies.models;

public class Director {

    private String directorName;
    private Number moviesCount;
    private Number totalRevenue;
    private Number totalBudget;
    private Number profitLoss;

    
    public Director() {
    }

    
    public Director(String directorName, Number moviesCount, Number totalRevenue, Number totalBudget,
            Number profitLoss) {
        this.directorName = directorName;
        this.moviesCount = moviesCount;
        this.totalRevenue = totalRevenue;
        this.totalBudget = totalBudget;
        this.profitLoss = profitLoss;
    }


    public String getDirectorName() {
        return directorName;
    }
    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
    public Number getMoviesCount() {
        return moviesCount;
    }
    public void setMoviesCount(Number moviesCount) {
        this.moviesCount = moviesCount;
    }
    public Number getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(Number totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public Number getTotalBudget() {
        return totalBudget;
    }
    public void setTotalBudget(Number totalBudget) {
        this.totalBudget = totalBudget;
    }
    public Number getProfitLoss() {
        return profitLoss;
    }
    public void setProfitLoss(Number profitLoss) {
        this.profitLoss = profitLoss;
    }

    
    
}
