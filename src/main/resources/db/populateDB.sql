DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(date_time, description, calories, user_id)
VALUES (to_timestamp('2024-11-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'завтрак', 300, 100000);
INSERT INTO meals(date_time, description, calories, user_id)
VALUES (to_timestamp('2024-11-01 12:15:00', 'YYYY-MM-DD HH24:MI:SS'), 'обед', 1200, 100000);
INSERT INTO meals(date_time, description, calories, user_id)
VALUES (to_timestamp('2024-11-01 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'перекус', 200, 100000);
INSERT INTO meals(date_time, description, calories, user_id)
VALUES (to_timestamp('2024-11-01 17:10:00', 'YYYY-MM-DD HH24:MI:SS'), 'ужин', 800, 100000);
INSERT INTO meals(date_time, description, calories, user_id)
VALUES (to_timestamp('2024-11-02 8:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'завтрак', 800, 100001);
INSERT INTO meals(date_time, description, calories, user_id)
VALUES (to_timestamp('2024-11-02 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'обед', 1500, 100000);