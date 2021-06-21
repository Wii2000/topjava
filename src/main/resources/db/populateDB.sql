DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2021-06-14 10:00', 'Пользователь завтрак', 500),
       (100000, '2021-06-14 13:00', 'Пользователь обед', 1000),
       (100000, '2021-06-14 20:00', 'Пользователь ужин', 500),
       (100000, '2021-06-15 00:00', 'Пользователь еда на граничном значении', 100),
       (100000, '2021-06-15 10:00', 'Пользователь завтрак', 1000),
       (100000, '2021-06-15 13:00', 'Пользователь обед', 500),
       (100000, '2021-06-15 20:00', 'Пользователь ужин', 410),
       (100001, '2021-06-14 14:00', 'Админ ланч', 510),
       (100001, '2021-06-14 21:00', 'Админ ужин', 1500);