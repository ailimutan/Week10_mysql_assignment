-- if database not yet existed, create one.
CREATE DATABASE IF NOT EXISTS moviemanager;

-- then make sure to use it before creating tables.
USE moviemanager;


-- create table for the Movies
CREATE TABLE if not exists Movies (
movie_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE,
genre VARCHAR(50),
year_released INT,
country_origin VARCHAR(50)
);