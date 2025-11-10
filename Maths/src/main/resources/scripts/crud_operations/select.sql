-- РАБОТА С ЮЗЕРАМИ (поиск)
SELECT * FROM app_user;
SELECT * FROM app_user WHERE username = 'user1';
SELECT * FROM app_user WHERE email LIKE '%@mail.ru';
SELECT * FROM app_user WHERE created_at >= NOW() - INTERVAL '7 days';
SELECT id, username, email, created_at 
FROM app_user 
ORDER BY created_at DESC;
-- РАБОТА С ФУНКЦИЯМИ (поиск)
SELECT * FROM func;

SELECT f.*, u.username 
FROM func f 
JOIN app_user u ON f.user_id = u.id 
WHERE u.username = 'user1';

SELECT * FROM func WHERE name LIKE '%квадрат%';
SELECT * FROM func ORDER BY created_at DESC;
SELECT u.username, COUNT(f.id) as function_count
FROM app_user u
LEFT JOIN func f ON u.id = f.user_id
GROUP BY u.id, u.username;

-- РАБОТА С ТОЧКАМИ(поиск)
SELECT * FROM point;
SELECT p.*, f.name as function_name
FROM point p
JOIN func f ON p.function_id = f.id
WHERE f.name = 'Квадратная функция';
SELECT * FROM point WHERE x_value BETWEEN 1.0 AND 5.0;
SELECT * FROM point ORDER BY function_id, x_value;
SELECT u.username, f.name as function_name, p.x_value, p.y_value
FROM point p
JOIN func f ON p.function_id = f.id
JOIN app_user u ON f.user_id = u.id;