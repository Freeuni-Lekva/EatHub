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
