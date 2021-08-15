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

INSERT INTO users(username, password,
                  email, balance, role, confirmed)
VALUES ('admin', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'eathub.freeuni@gmail.com', 1000, 'ADMIN', TRUE),
       ('vano', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'vganj18@freeuni.edu.ge', 1000, 'CUSTOMER', TRUE),
       ('alex', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'ainau18@freeuni.edu.ge', 1000, 'CUSTOMER', TRUE),
       ('botko', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'gbotk18@freeuni.edu.ge', 1000, 'CUSTOMER', TRUE),
       ('nika', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'nshug18@freeuni.edu.ge', 1000, 'CUSTOMER', TRUE),
       ('test', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'vanoganjelashvili@gmail.com', 1000, 'ADMIN', FALSE),
       ('leo', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'lirem18@freeuni.edu.ge', 1000, 'CUSTOMER', TRUE),
       ('tornike', '$2a$10$.gSvGvCf5I85vxP4dklNSuqkwnFGNZxE4S04Dy6aZX76btLZic6Wm',
        'ttotl18@freeuni.edu.ge', 1000, 'CUSTOMER', TRUE);
;

INSERT INTO restaurants(restaurant_name, location, max_limit, Balance, url)
VALUES ('თბილისურა', 'Tbilisi', 1000, 1000, '1.jpg'),
       ('ალაზანი', 'Telavi', 2000, 2000, '2.jpg'),
       ('სტალინსკი', 'Gori', 2000, 2600, '3.jpg'),
       ('ქუთეისური', 'Kutaisi', 1000, 1000, '4.jpg'),
       ('Tiflis Veranda Restaurant', 'Tbilisi', 1000, 1000, '5.jpg'),
       ('ბიკენტიას საქაბაბე', 'Kutaisi', 1000, 1000, '6.jpg');


INSERT INTO meals(meal_name, meal_price, cooking_time, restaurant_id, url)
VALUES ('მწვადი', 12.0, '00:30:00', 1, '1.jpg'),
       ('ქაბაბი', 16.0, '00:20:00', 1, '2.jpg'),
       ('ხაჭაპური', 8.0, '00:17:00', 1, '3.jpg'),
       ('მწვადი', 12.0, '00:30:00', 2, '4.jpg'),
       ('ტორტი', 16.0, '01:00:00', 2, '5.jpg'),
       ('ხაჭაპური', 9.0, '00:22:00', 2, '6.jpg'),
       ('აჩმა', 12.0, '00:25:00', 3, '7.jpg'),
       ('ხინკალი', 7.0, '00:30:00', 3, '8.jpg'),
       ('გორული კოტლეტი', 2.0, '00:16:00', 3, '9.jpg'),
       ('კიტრის მწნილი', 5.0, '00:03:00', 3, '10.jpg'),
       ('იმერული ხაჭაპური', 8.0, '00:17:00', 4, '11.jpg'),
       ('აჭარული ხაჭაპური', 9.0, '00:22:00', 4, '12.jpg'),
       ('მწვადი', 12.0, '00:30:00', 4, '13.jpg'),
       ('ტორტი', 16.0, '01:05:00', 4, '14.jpg'),
       ('Omlete', 15.0, '00:05:00', 5, '15.jpg'),
       ('Chicken Massaman', 17.0, '00:25:00', 5, '16.jpg'),
       ('khinkali', 8.0, '00:20:00', 5, '17.jpg'),
       ('Pizza', 14.0, '00:25:00', 5, '18.jpg'),
       ('ქაბაბი', 7.0, '00:20:00', 6, '19.jpg'),
       ('ლუდი', 4.0, '00:03:00', 6, '20.jpg'),
       ('კუპატი', 8.0, '00:25:00', 6, '21.jpg'),
       ('შქმერული', 17.0, '00:45:00', 6, '22.jpg');

INSERT
INTO rooms(restaurant_id, active)
VALUES (3, true);

INSERT INTO user_room(room_id, user_id)
VALUES (1, 2),
       (1, 3),
       (1, 4)
