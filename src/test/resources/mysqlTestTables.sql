USE eathub_db;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS user_room;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS restaurants;

CREATE TABLE users
(
    user_id   BIGINT       NOT NULL AUTO_INCREMENT,
    username  VARCHAR(64)  NOT NULL,
    password  VARCHAR(256) NOT NULL,
    email     VARCHAR(64)  NOT NULL,
    balance   DECIMAL      NOT NULL,
    role      VARCHAR(64)  NOT NULL,
    confirmed BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id),
    UNIQUE (username),
    UNIQUE (email)
);


CREATE TABLE meals
(
    meal_id       BIGINT       NOT NULL AUTO_INCREMENT,
    meal_name     VARCHAR(100) NOT NULL,
    meal_price    DECIMAL      NOT NULL,
    cooking_time  TIME         NOT NULL,
    restaurant_id BIGINT       NOT NULL,
    url           VARCHAR(100) NOT NULL,
    PRIMARY KEY (meal_id),
    CONSTRAINT DUPLICATE_NAME UNIQUE (meal_name, restaurant_id)
);


CREATE TABLE restaurants
(
    restaurant_id   BIGINT       NOT NULL AUTO_INCREMENT,
    restaurant_name VARCHAR(100) NOT NULL,
    location        VARCHAR(100) NOT NULL,
    max_limit       NUMERIC      NOT NULL,
    Rating          DECIMAL      NOT NULL DEFAULT 0,
    Balance         DECIMAL      NOT NULL,
    url             VARCHAR(100) NOT NULL,
    PRIMARY KEY (restaurant_id)
);


CREATE TABLE messages
(
    message_id BIGINT         NOT NULL AUTO_INCREMENT,
    user_id    BIGINT         NOT NULL,
    room_id    BIGINT         NOT NULL,
    send_time  VARCHAR(40)    NOT NULL,
    type       VARCHAR(30)    NOT NULL,
    content    VARCHAR(10000) NOT NULL,
    PRIMARY KEY (message_id)
);

ALTER TABLE meals
    ADD CONSTRAINT FK_Restaurants_TO_meals
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (restaurant_id);

CREATE TABLE rooms
(
    room_id       BIGINT  NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT  NOT NULL,
    active        BOOLEAN NOT NULL,
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

CREATE TABLE transactions
(
    transaction_id BIGINT   NOT NULL AUTO_INCREMENT,
    user_id        BIGINT   NOT NULL,
    restaurant_id  BIGINT   NOT NULL,
    room_id        BIGINT   NOT NULL,
    amount         DECIMAL  NOT NULL,
    time           DATETIME NOT NULL DEFAULT current_timestamp,
    PRIMARY KEY (transaction_id)
);

ALTER TABLE transactions
    ADD CONSTRAINT FK_users_TO_transactions
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_restaurants_TO_transactions
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (restaurant_id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_rooms_TO_transactions
        FOREIGN KEY (room_id)
            REFERENCES rooms (room_id);


CREATE TABLE user_room
(
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT DUPLICATE_ROOM_USER UNIQUE (room_id, user_id)
);

ALTER TABLE user_room
    ADD CONSTRAINT FK_rooms_TO_user_room
        FOREIGN KEY (room_id)
            REFERENCES rooms (room_id);

ALTER TABLE user_room
    ADD CONSTRAINT FK_users_TO_user_room
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE messages
    ADD CONSTRAINT FK_users_TO_messages
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE messages
    ADD CONSTRAINT FK_rooms_TO_messages
        FOREIGN KEY (room_id)
            REFERENCES rooms (room_id);

