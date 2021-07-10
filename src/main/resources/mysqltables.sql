USE eathub_db;

DROP TABLE IF EXISTS users;


CREATE TABLE users
(
    user_id  BIGINT  NOT NULL AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(256) NOT NULL,
    email    VARCHAR(64) NOT NULL,
    balance  DECIMAL NOT NULL,
    role     VARCHAR(64) NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (user_id),
    UNIQUE (username),
    UNIQUE (email)
);


CREATE TABLE meals
(
    meal_id       BIGINT  NOT NULL AUTO_INCREMENT,
    meal_name     VARCHAR(100) NOT NULL,
    cooking_time  TIME    NOT NULL,
    restaurant_id BIGINT  NOT NULL,
    PRIMARY KEY (meal_id),
    UNIQUE (meal_id)
);



CREATE TABLE Restaurants
(
    restaurant_id   BIGINT  NOT NULL AUTO_INCREMENT,
    restaurant_name VARCHAR(100) NOT NULL,
    location        VARCHAR(100) NOT NULL,
    max_limit       NUMERIC NOT NULL,
    Rating          DECIMAL NOT NULL DEFAULT 0,
    Balance         DECIMAL NOT NULL,
    PRIMARY KEY (restaurant_id),
    UNIQUE (restaurant_id)
);

ALTER TABLE meals
    ADD CONSTRAINT FK_Restaurants_TO_meals
        FOREIGN KEY (restaurant_id)
            REFERENCES Restaurants (restaurant_id);

