USE eathub_db;

DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS user_room;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS restaurants;

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


CREATE TABLE meals
(
    meal_id       BIGINT  NOT NULL AUTO_INCREMENT,
    meal_name     VARCHAR(100) NOT NULL,
    meal_price    DECIMAL NOT NULL,
    cooking_time  TIME    NOT NULL,
    restaurant_id BIGINT  NOT NULL,
    PRIMARY KEY (meal_id)
);


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

CREATE TABLE rooms
(
    room_id       BIGINT  NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT  NOT NULL,
    active     BOOLEAN NOT NULL,
    PRIMARY KEY (room_id)
);

ALTER TABLE rooms
    ADD CONSTRAINT FK_restaurants_TO_rooms
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (restaurant_id);

CREATE TABLE orders
(
    order_id BIGINT  NOT NULL AUTO_INCREMENT,
    user_id  BIGINT  NOT NULL,
    meal_id  BIGINT  NOT NULL,
    room_id  BIGINT  NOT NULL,
    quantity NUMERIC NOT NULL DEFAULT 1,
    PRIMARY KEY (order_id)
);

ALTER TABLE orders
    ADD CONSTRAINT FK_users_TO_orders
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE orders
    ADD CONSTRAINT FK_meals_TO_orders
        FOREIGN KEY (meal_id)
            REFERENCES meals (meal_id);

ALTER TABLE orders
    ADD CONSTRAINT FK_rooms_TO_orders
        FOREIGN KEY (room_id)
            REFERENCES rooms (room_id);

ALTER TABLE orders
    ADD CONSTRAINT UQ_order_id UNIQUE (order_id);

CREATE TABLE user_room
(
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

ALTER TABLE user_room
    ADD CONSTRAINT FK_rooms_TO_user_room
        FOREIGN KEY (room_id)
            REFERENCES rooms (room_id);

ALTER TABLE user_room
    ADD CONSTRAINT FK_users_TO_user_room
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

INSERT INTO users(username, password,
                  email, balance, role)
VALUES ('admin', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'eathub.freeuni@gmail.com', 1000, 'ADMIN');

INSERT INTO restaurants(restaurant_name, location, max_limit, Balance)
VALUES ("Test1", "Tbilisi", 1000, 1000);

INSERT INTO restaurants(restaurant_name, location, max_limit, Balance)
VALUES ("Test2", "Telavi", 2000, 2000);

INSERT INTO restaurants(restaurant_name, location, max_limit, Balance)
VALUES ("Test3", "Kutaisi", 1000, 1000);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Mtsvadi", 12.0, 10, 1);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Kababi", 16.0, 12, 1);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Khachapuri", 8.0, 17, 1);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Mtsvadi", 12.0, 10, 2);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Torti", 16.0, 12, 2);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Telavis Khachapuri", 9.0, 17, 2);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Adjafsandali", 12.0, 10, 3);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Kartopili", 7.0, 12, 3);

INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id)
VALUES ("Kitris Mtsnili", 5.0, 5, 3);


