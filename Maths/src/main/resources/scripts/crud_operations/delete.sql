-- УДАЛЕНИЕ ЮЗЕРОВ
DELETE FROM app_user 
WHERE id = 3;
DELETE FROM app_user 
WHERE username = 'user4';
-- УДАЛЕНИЕ ФУНКЦИЙ
DELETE FROM func 
WHERE id = 3;
DELETE FROM func 
WHERE user_id = 2;
-- УДАЛЕНИЕ ТОЧЕК
DELETE FROM point 
WHERE function_id = 1 AND x_value = 5.0;
DELETE FROM point 
WHERE function_id = 2;
DELETE FROM point 
WHERE x_value BETWEEN 10.0 AND 20.0;