package vttp.batch5.paf.movies.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp.batch5.paf.movies.models.MongoMovie;
import vttp.batch5.paf.movies.models.SqlMovie;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Service
public class MovieService {

  @Autowired
  MongoMovieRepository mongoRepository;

  @Autowired
  MySQLMovieRepository sqlRepository;


  // TODO: Task 2

  public boolean isSQLAlreadyLoaded() {
    long count = sqlRepository.count();
    return count > 0;
  }
  
  public boolean isMongoAlreadyLoaded() {
    long count = mongoRepository.count();
    return count > 0;
  }
  public List<SqlMovie> convertDocumentsToSqlMovie(List<Document> documents) {
    List<SqlMovie> movies = new ArrayList<>();

    for (Document doc : documents) {
      String imdbId = doc.getString("imdb_id");
      Number voteAverage = doc.containsKey("vote_average") ? doc.get("vote_average", Number.class) : 0.0;
      Number voteCount = doc.containsKey("vote_count") ? doc.get("vote_count", Number.class) : 0;
      Date releaseDate = extractSqlDate(doc, "release_date");;
      BigDecimal revenue = doc.containsKey("revenue") ? new BigDecimal(doc.get("revenue").toString()) : BigDecimal.valueOf(1_000_000);
      BigDecimal budget = doc.containsKey("budget") ? new BigDecimal(doc.get("budget").toString()) : BigDecimal.valueOf(1_000_000);
      Number runtime = doc.containsKey("runtime") ? doc.get("runtime", Number.class) : 90;

      movies.add(new SqlMovie(imdbId, voteAverage, voteCount, releaseDate, revenue, budget, runtime));
    }

    return movies;
  }

  public List<MongoMovie> convertDocumentsToMongoMovie(List<Document> documents) {
    List<MongoMovie> movies = new ArrayList<>();
    for (Document doc : documents) {
        String imdbId = doc.getString("imdb_id");
        String title = doc.getString("title");

        // Convert director field (it can be a string or a list)
        List<String> directors;
        if (doc.containsKey("director") && doc.get("director") instanceof String) {
            directors = Arrays.asList(doc.getString("director").split(", "));
        } else if (doc.containsKey("director") && doc.get("director") instanceof List) {
            directors = (List<String>) doc.get("director");
        } else {
            directors = new ArrayList<>();
        }

        String overview = doc.getString("overview");
        String tagline = doc.getString("tagline");

        // Convert genres field (must be a List<String>)
        List<String> genres;
        if (doc.containsKey("genres") && doc.get("genres") instanceof String) {
            genres = Arrays.asList(doc.getString("genres").split(", "));
        } else if (doc.containsKey("genres") && doc.get("genres") instanceof List) {
            genres = (List<String>) doc.get("genres");
        } else {
            genres = new ArrayList<>();
        }

        Number imdbRating = doc.get("imdb_rating", Number.class) != null ? doc.get("imdb_rating", Number.class) : 0.0;
        Number imdbVotes = doc.get("imdb_votes", Number.class) != null ? doc.get("imdb_votes", Number.class) : 0;

        movies.add(new MongoMovie(imdbId, title, directors, overview, tagline, genres, imdbRating, imdbVotes));
    }

    return movies;
  }
  
  private Date extractSqlDate(Document doc, String field) {
    Object value = doc.get(field);

    if (value instanceof String str) {
        return Date.valueOf(str); // Directly parse "YYYY-MM-DD"
    } else if (value instanceof java.util.Date date) {
        return new Date(date.getTime()); // Convert java.util.Date to sql.Date
    } else if (value instanceof Long timestamp) {
        return new Date(timestamp); // Convert timestamp to sql.Date
    }
    return null;
}
  
  public void batchInsertIntoMongo(List<MongoMovie> movies) {
    int batchSize = 25;
    List<MongoMovie> batch = new ArrayList<>();

    for (int i = 0; i < movies.size(); i++) {
        batch.add(movies.get(i));

        // Insert in batches of 25
        if (batch.size() == batchSize || i == movies.size() - 1) {
            mongoRepository.batchInsertMovies(batch);
            System.out.println("Inserted batch of " + batch.size() + " movies into MongoDB.");
            batch.clear();
        }
    }
  }
  
  public void batchInsertIntoMySQL(List<SqlMovie> movies) {
    int batchSize = 25;
    List<SqlMovie> batch = new ArrayList<>();

    for (int i = 0; i < movies.size(); i++) {
        batch.add(movies.get(i));

        // Insert in batches of 25
        if (batch.size() == batchSize || i == movies.size() - 1) {
            sqlRepository.batchInsertMovies(batch);;
            System.out.println("Inserted batch of " + batch.size() + " movies into MySQL.");
            batch.clear();
        }
    }

    
  }

  @Transactional
  public void processBatchInsert(List<SqlMovie> sqlMovies, List<MongoMovie> mongoMovies) {
    if (sqlMovies.isEmpty() || mongoMovies.isEmpty()) return;

    // Insert into MySQL
    batchInsertIntoMySQL(sqlMovies);
    // Insert into MongoDB
    batchInsertIntoMongo(mongoMovies);

    System.out.println("Successfully inserted batch into MySQL and MongoDB.");
  }


  

  // TODO: Task 3
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void getProlificDirectors() {
  }


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void generatePDFReport() {

  }

}
