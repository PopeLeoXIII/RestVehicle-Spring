DROP TABLE IF EXISTS reservations_vehicles CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS vehicles CASCADE;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS cities
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    name  VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    city_id BIGINT REFERENCES cities (id) ,
    name    VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    name    VARCHAR(200) NOT NULL ,
    surname VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    user_id        BIGINT REFERENCES users (id),
    start_datetime timestamp NOT NULL,
    end_datetime   timestamp NOT NULL,
    status         VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations_vehicles
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reservation_id BIGINT REFERENCES reservations (id),
    vehicle_id     BIGINT REFERENCES vehicles (id),
    CONSTRAINT unique_link UNIQUE (reservation_id, vehicle_id)
);


INSERT INTO cities (name)
VALUES  ('Moscow'),
        ('Saratov'),
        ('Samara');

INSERT INTO users (name, surname)
VALUES ('Иван', 'Субботин'),      -- 1
       ('Петр', 'Понедельников'), -- 2
       ('Игнат', 'Вторников'),    -- 3
       ('Иван', 'Середец'),       -- 4
       ('Максим', 'Четверкин'),   -- 5
       ('Вера', 'Пятницкая'),     -- 6
       ('Ольга', 'Воскресенская'); -- 7

INSERT INTO vehicles (name, city_id)
VALUES ('Велосипед', 3),    -- 1
       ('Самокат', 2),      -- 2
       ('Велосипед 1', 1),  -- 3
       ('Велосипед 2', 1),  -- 4
       ('Велосипед 3', 1);  -- 5

INSERT INTO reservations (user_id, start_datetime, end_datetime, status)
VALUES (1, '2016-06-22 19:10:25-07', '2016-06-23 19:10:25-07', 'ACTIVE'),   -- 1
       (1, '2016-06-22 19:10:25-07', '2016-06-23 19:10:25-07', 'ACTIVE'),   -- 2
       (2, '2016-06-22 19:10:25-07', '2016-06-22 20:10:25-07', 'CANCELED'), -- 3
       (2, '2016-06-23 19:10:25-07', '2016-06-23 20:10:25-07', 'ACTIVE');   -- 4


INSERT INTO reservations_vehicles (reservation_id, vehicle_id)
VALUES (1, 3), -- 1
       (1, 4), -- 2
       (1, 5), -- 3
       (2, 4), -- 4
       (2, 5), -- 5
       (3, 2), -- 6
       (4, 2); -- 7
