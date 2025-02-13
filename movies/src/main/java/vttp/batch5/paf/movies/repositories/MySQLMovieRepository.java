package vttp.batch5.paf.movies.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.SqlMovie;

@Repository
public class MySQLMovieRepository {

  final String batchInsertQuery =
  "INSERT INTO imdb (imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "vote_average = VALUES(vote_average), " +
                     "vote_count = VALUES(vote_count), " +
                     "release_date = VALUES(release_date), " +
                     "revenue = VALUES(revenue), " +
                     "budget = VALUES(budget), " +
                     "runtime = VALUES(runtime)";
                     
  @Autowired
  JdbcTemplate jdbcTemplate;

  public long count() {
    String sql = "SELECT COUNT(*) FROM imdb";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
  public void batchInsertMovies(List<SqlMovie> movies) {
    jdbcTemplate.batchUpdate(batchInsertQuery, movies, movies.size(), (ps, movie) -> {
        ps.setString(1, movie.getImdbId());
        ps.setObject(2, movie.getVoteAverage());  // Let MySQL handle Number
        ps.setObject(3, movie.getVoteCount());
        ps.setDate(4, movie.getReleaseDate());
        ps.setBigDecimal(5, movie.getRevenue());
        ps.setBigDecimal(6, movie.getBudget());
        ps.setObject(7, movie.getRuntime());
    });
  }

    // TODO: Task 3
   
}
  



