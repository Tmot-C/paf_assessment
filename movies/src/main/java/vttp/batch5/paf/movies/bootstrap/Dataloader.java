package vttp.batch5.paf.movies.bootstrap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import vttp.batch5.paf.movies.models.MongoMovie;
import vttp.batch5.paf.movies.models.SqlMovie;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;
import vttp.batch5.paf.movies.services.MovieService;

@Component
public class Dataloader {

  //TODO: Task 2

  @Autowired
  MySQLMovieRepository sqlRepository;

  @Autowired
  MongoMovieRepository mongoRepository;
  
  @Autowired
  MovieService movieService;

  @Value("${myapp.datafile:C:\\Users\\timot\\Documents\\paf_b5_assessment_template\\data\\movies_post_2010.zip}")
  private String zipFilePath;

  @Transactional
  public void runDataloader(){
    
    if (!movieService.isSQLAlreadyLoaded() || !movieService.isMongoAlreadyLoaded()) {
      List<Document> movies = loadDataFromZip(zipFilePath);
      List<SqlMovie> sqlMovies = movieService.convertDocumentsToSqlMovie(movies);
      List<MongoMovie> mongoMovies = movieService.convertDocumentsToMongoMovie(movies);
      try {
        movieService.processBatchInsert(sqlMovies, mongoMovies);
      } catch (Exception e) {
        // Log batch failure in MongoDB
        List<String> imdbIds = sqlMovies.stream().map(SqlMovie::getImdbId).toList();
        mongoRepository.logError(imdbIds, "Batch Insert Failed: " + e.getMessage());
      }
    }

  };


  private List<Document> loadDataFromZip(String zipFilePath) {
    List<Document> movieList = new ArrayList<>(){     
    };
    try (ZipInputStream zis = new ZipInputStream(
      new FileInputStream(zipFilePath))) {
  
      ZipEntry zipEntry;
      while ((zipEntry = zis.getNextEntry()) != null) {
            // if the entry is a JSON file
        if (zipEntry.getName().endsWith(".json")) {
                // read and parse it
          List<Document> movies = parseJsonStream(zis);
          movies = filterDocuments(movies);
          System.out.println(movies.size());
          movieList = movies;
        }
        zis.closeEntry();
          
      }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return movieList;
  }

  private List<Document> parseJsonStream(InputStream input) {
    List<Document> results = new ArrayList<>();

    BufferedReader br = new BufferedReader(new InputStreamReader(input));
    String line;
    try {
        while ((line = br.readLine()) != null) {
            String trimmed = line.trim();
            // Skip empty lines
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                // Convert the raw JSON line to a Mongo Document
                Document doc = Document.parse(trimmed);
                results.add(doc);
            } catch (Exception ex) {
                System.err.println("Failed to parse line as Document: " + line);
                ex.printStackTrace();
                // If you want to stop the entire parse on error, throw an exception
                throw new RuntimeException("Failed to parse JSON line", ex);
            }
        }
    } catch (IOException e) {
        // Handle I/O errors
        e.printStackTrace();
    }

    
    return results;
  }

  public List<Document> filterDocuments(List<Document> rawDocuments) {
    List<Document> filteredDocuments = new ArrayList<>();
    Set<String> uniqueImdbIds = new HashSet<>();

    for (Document doc : rawDocuments) {
        try {
            // 1) Extract and parse release_date
            java.sql.Date releaseDate = null;
            if (doc.containsKey("release_date") && doc.getString("release_date") != null) {
                try {
                    releaseDate = java.sql.Date.valueOf(doc.getString("release_date")); // "YYYY-MM-DD"
                } catch (IllegalArgumentException e) {
                    // If the date is malformed, treat it as missing
                }
            }

            // 2) If release_date is missing or before 2018, SKIP
            if (releaseDate == null || releaseDate.toLocalDate().getYear() < 2018) {
                continue; // Skip this document
            }
            // 3) Ensure uniqueness using imdb_id
            String imdbId = doc.getString("imdb_id");
            if (imdbId == null || imdbId.isEmpty() || uniqueImdbIds.contains(imdbId)) {
                continue; // Skip duplicates
            }
            uniqueImdbIds.add(imdbId); 

            // 4) Impute all missing/erroneous fields
            Document cleanedDoc = new Document(doc); // Copy the original

            // Numeric fields (default to 0 if missing or invalid)
            imputeNumeric(cleanedDoc, "vote_average", 0);
            imputeNumeric(cleanedDoc, "vote_count", 0);
            imputeNumeric(cleanedDoc, "revenue", 0);
            imputeNumeric(cleanedDoc, "runtime", 0);
            imputeNumeric(cleanedDoc, "budget", 0);
            imputeNumeric(cleanedDoc, "popularity", 0);
            imputeNumeric(cleanedDoc, "imdb_rating", 0);
            imputeNumeric(cleanedDoc, "imdb_votes", 0);

            // String fields (default to "" if missing)
            imputeString(cleanedDoc, "imdb_id");
            imputeString(cleanedDoc, "title");
            imputeString(cleanedDoc, "status");
            imputeString(cleanedDoc, "original_language");
            imputeString(cleanedDoc, "overview");
            imputeString(cleanedDoc, "tagline");
            imputeString(cleanedDoc, "genres");
            imputeString(cleanedDoc, "spoken_languages");
            imputeString(cleanedDoc, "casts");
            imputeString(cleanedDoc, "director");
            imputeString(cleanedDoc, "poster_path");

            // Boolean fields (default to false if missing)
            imputeBoolean(cleanedDoc, "adult");
            imputeBoolean(cleanedDoc, "video");

            // Set the release_date back as java.sql.Date
            cleanedDoc.put("release_date", releaseDate);

            // 4) Add the cleaned document to the new list
            filteredDocuments.add(cleanedDoc);

        } catch (Exception e) {
            System.err.println("Failed to process document: " + doc.toJson());
            e.printStackTrace();
            // Skip this document if it causes an error
        }
    }

    return filteredDocuments;
  }

  // Helper method to ensure a numeric field is valid, otherwise default to `defaultValue`
  private void imputeNumeric(Document doc, String fieldName, Number defaultValue) {
    if (!doc.containsKey(fieldName) || doc.get(fieldName) == null) {
        doc.put(fieldName, defaultValue);
    } else {
        try {
            // Try to retrieve and ensure it's a valid number
            Object value = doc.get(fieldName);

            if (defaultValue instanceof Integer) {
                doc.put(fieldName, Math.max(((Number) value).intValue(), (Integer) defaultValue));
            } else if (defaultValue instanceof Long) {
                doc.put(fieldName, Math.max(((Number) value).longValue(), (Long) defaultValue));
            } else if (defaultValue instanceof Double) {
                doc.put(fieldName, Math.max(((Number) value).doubleValue(), (Double) defaultValue));
            }
        } catch (Exception e) {
            doc.put(fieldName, defaultValue); // If invalid, set to default
        }
    }
}

// Helper method to ensure a string field is not null, otherwise default to ""
  private void imputeString(Document doc, String fieldName) {
    if (!doc.containsKey(fieldName) || doc.get(fieldName) == null) {
        doc.put(fieldName, "");
    }
  }

  // Helper method to ensure a boolean field is valid, otherwise default to false
  private void imputeBoolean(Document doc, String fieldName) {
    if (!doc.containsKey(fieldName) || doc.get(fieldName) == null) {
        doc.put(fieldName, false);
    } else {
        try {
            doc.put(fieldName, doc.getBoolean(fieldName, false));
        } catch (Exception e) {
            doc.put(fieldName, false); // If invalid, set to false
        }
    }
  }



}








