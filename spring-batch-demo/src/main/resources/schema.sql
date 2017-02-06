
CREATE DATABASE IF NOT EXISTS tips DEFAULT CHAR SET UTF8;

USE tips;
DROP TABLE IF EXISTS person;

CREATE TABLE person (
  id         BIGINT(10) AUTO_INCREMENT PRIMARY KEY,
  age        INT(3)       NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  email      VARCHAR(255) NOT NULL
)