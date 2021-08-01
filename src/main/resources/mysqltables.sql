USE eathub_db;

DROP TABLE IF EXISTS users;


CREATE TABLE users
(
    user_id  BIGINT       NOT NULL AUTO_INCREMENT,
    username VARCHAR(64)  NOT NULL,
    password VARCHAR(256) NOT NULL,
    email    VARCHAR(64)  NOT NULL,
    balance  DECIMAL      NOT NULL,
    role     VARCHAR(64)  NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (username),
    UNIQUE (email)
);


DROP TABLE IF EXISTS meals;
CREATE TABLE meals
(
    meal_id       BIGINT  NOT NULL AUTO_INCREMENT,
    meal_name     VARCHAR(100) NOT NULL,
    meal_price    DECIMAL NOT NULL,
    cooking_time  TIME    NOT NULL,
    restaurant_id BIGINT  NOT NULL,
    PRIMARY KEY (meal_id)
);


DROP TABLE IF EXISTS restaurants;
CREATE TABLE restaurants
(
    restaurant_id   BIGINT  NOT NULL AUTO_INCREMENT,
    restaurant_name VARCHAR(100) NOT NULL,
    location        VARCHAR(100) NOT NULL,
    max_limit       NUMERIC NOT NULL,
    Rating          DECIMAL NOT NULL DEFAULT 0,
    Balance         DECIMAL NOT NULL,
    PRIMARY KEY (restaurant_id)
);

ALTER TABLE meals
    ADD CONSTRAINT FK_Restaurants_TO_meals
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (restaurant_id);


INSERT INTO users(username, password,
                  email, balance, role)
VALUES ("admin", "$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm",
        "eathub.freeuni@gmail.com", 1000, "ADMIN");

