package vttp.batch5.paf.movies.repositories;


import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.MongoMovie;

@Repository
public class MongoMovieRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public long count() {
        return mongoTemplate.getCollection("imdb").countDocuments();
    }
    
 // TODO: Task 2.3
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
    public void batchInsertMovies(List<MongoMovie> movies) {
        
        if (!movies.isEmpty()) {
            mongoTemplate.insert(movies, "imdb");
        }
    }


 

 // TODO: Task 2.4
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
    public void logError(List<String> imdbIds, String errorMessage) {

        Document errorDoc = new Document();
        errorDoc.put("imdb_ids", imdbIds);
        errorDoc.put("error", errorMessage);
        errorDoc.put("timestamp", new Date());

        mongoTemplate.insert(errorDoc, "errors"); 
        System.err.println("Logged batch insert error in MongoDB: " + errorMessage);

    }

 // TODO: Task 3
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //


}
