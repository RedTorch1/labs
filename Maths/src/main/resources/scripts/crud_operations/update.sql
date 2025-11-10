-- ОБНОВЛЕНИЕ ЮЗЕРОВ
UPDATE app_user 
SET email = 'updated_email@mail.ru' 
WHERE username = 'user1';
UPDATE app_user 
SET password_hash = 'new_secure_hash' 
WHERE id = 1;
-- ОБНОВЛЕНИЕ ФУНКЦИЙ
UPDATE func 
SET expression = 'x^2 + 3*x + 2' 
WHERE name = 'Квадратная функция';
UPDATE func 
SET name = 'Квадратичная функция' 
WHERE id = 1;
-- ОБНОВЛЕНИЕ ТОЧЕК
UPDATE point 
SET y_value = 100.0 
WHERE function_id = 1 AND x_value = 10.0;
UPDATE point 
SET y_value = y_value * 2 
WHERE function_id = 1;