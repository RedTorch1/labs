-- =========================
-- Таблица пользователей
-- =========================
DROP TABLE IF EXISTS app_user CASCADE;

CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- =========================
-- Таблица функций
-- =========================
DROP TABLE IF EXISTS function CASCADE;

CREATE TABLE function (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    expression VARCHAR(1000) NOT NULL,
    user_id INT NOT NULL,
    CONSTRAINT fk_function_user FOREIGN KEY(user_id) REFERENCES app_user(id)
);

-- =========================
-- Таблица точек
-- =========================
DROP TABLE IF EXISTS point CASCADE;

CREATE TABLE point (
    id SERIAL PRIMARY KEY,
    function_id INT NOT NULL,
    x_value NUMERIC(20,10) NOT NULL,
    y_value NUMERIC(20,10) NOT NULL,
    CONSTRAINT fk_point_function FOREIGN KEY(function_id) REFERENCES function(id),
    CONSTRAINT unique_point UNIQUE (function_id, x_value)
);
