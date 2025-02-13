-- Drop database if it exists
DROP DATABASE IF EXISTS movies;

-- Create the database
CREATE DATABASE movies;

-- Select the database
USE movies;

SELECT "CREATING IMDB";
CREATE TABLE imdb (
    
    imdb_id varchar(16),
    vote_average float DEFAULT 0,
    vote_count int DEFAULT 0,
    release_date date,
    revenue decimal (15, 2) DEFAULT 1000000,
    budget decimal (15, 2) DEFAULT 1000000,
    runtime int DEFAULT 90,
    constraint pk_imdb_id primary key(imdb_id)

);

-- Grant fred access to the database
GRANT ALL PRIVILEGES ON movies.* TO 'tim'@'%';

-- Apply changes to privileges
FLUSH PRIVILEGES;