DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;
DROP SEQUENCE IF EXISTS meal_global_seq;
DROP SEQUENCE IF EXISTS meals_date_time_idx;
DROP SEQUENCE IF EXISTS meals_calories_idx;
DROP SEQUENCE IF EXISTS meals_meals_date_time_and_calories_idx;
DROP SEQUENCE IF EXISTS meals_date_time_and_calories_idx;
CREATE SEQUENCE global_seq START WITH 100000;
CREATE SEQUENCE meal_global_seq START WITH 100100;
CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL,
    enabled          BOOL                DEFAULT TRUE  NOT NULL,
    calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);
CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE TABLE meals
(
    id     INTEGER PRIMARY KEY DEFAULT nextval('meal_global_seq'),
    date_time   TIMESTAMP NOT NULL,
    description VARCHAR   NOT NULL,
    calories    INTEGER   NOT NULL,
    user_id     INTEGER   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX meals_date_time_idx ON meals (user_id, date_time);
CREATE INDEX meals_calories_idx ON meals (date_time);
CREATE INDEX meals_date_time_and_calories_idx ON meals (date_time, calories);