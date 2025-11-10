-- ДОБАВЛЕНИЕ ЮЗЕРА(ов)
INSERT INTO app_user (username, password_hash, email) 
VALUES ('new_user', 'new_password_hash', 'new_user@mail.ru');
INSERT INTO app_user (username, password_hash, email) VALUES
('user3', 'hash3', 'user3@gmail.com'),
('user4', 'hash4', 'user4@yandex.ru');
INSERT INTO func (user_id, name, expression) 
VALUES (1, 'Парабола', 'x^2 + 2*x + 1');
-- ДОБАВЛЕНИЕ ФУНКЦИИ
INSERT INTO func (user_id, name, expression) 
VALUES (2, 'Косинус', 'cos(x)') 
RETURNING id;
-- ДОБАВЛЕНИЕ ТОЧКИ
INSERT INTO point (function_id, x_value, y_value) 
VALUES (1, 5.0, 25.0);
INSERT INTO point (function_id, x_value, y_value) VALUES
(1, 6.0, 36.0),
(1, 7.0, 49.0),
(2, 4.0, 9.0);