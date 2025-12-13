<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создание функции из массивов</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #4CAF50;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }

        input[type="number"], input[type="text"] {
            width: 200px;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        input[type="text"] {
            width: 300px;
        }

        button {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            margin-right: 10px;
            margin-bottom: 10px;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #45a049;
        }

        button:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }

        button.save-btn {
            background-color: #2196F3;
        }

        button.save-btn:hover {
            background-color: #1976D2;
        }

        #pointsTable {
            margin-top: 20px;
            max-height: 400px;
            overflow-y: auto;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
            position: sticky;
            top: 0;
        }

        .error-modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .modal-content {
            background: white;
            padding: 30px;
            border-radius: 8px;
            max-width: 500px;
            width: 90%;
            box-shadow: 0 4px 20px rgba(0,0,0,0.2);
        }

        .modal-content h3 {
            color: #d32f2f;
            margin-top: 0;
            margin-bottom: 15px;
        }

        .modal-content button {
            background-color: #f44336;
            margin-top: 20px;
        }

        .modal-content button:hover {
            background-color: #d32f2f;
        }

        .controls {
            margin-top: 20px;
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            align-items: center;
        }

        .loading {
            display: none;
            color: #666;
            font-style: italic;
        }

        .back-btn {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #757575;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            text-align: center;
        }

        .back-btn:hover {
            background-color: #616161;
        }

        /* Стили для сообщения об успехе как в main.jsp */
        .success-section {
            display: none;
            margin-top: 10px;
            width: 100%;
        }

        .success-info {
            display: flex;
            align-items: center;
            gap: 10px;
            background-color: #f0f9f0;
            padding: 10px 15px;
            border-radius: 4px;
            border: 1px solid #c3e6cb;
        }

        .success-name {
            font-weight: bold;
            color: #155724;
            font-size: 16px;
        }

        .success-id {
            color: #0c5460;
            font-size: 14px;
            background-color: #d1ecf1;
            padding: 3px 8px;
            border-radius: 3px;
            border: 1px solid #bee5eb;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Создание табулированной функции из массивов</h1>

        <div class="form-group">
            <label for="functionName">Название функции:</label>
            <input type="text" id="functionName" name="functionName" placeholder="Введите название функции" style="width: 300px; padding: 8px; margin-bottom: 10px;">
        </div>

        <div class="form-group">
            <label for="pointsCount">Количество точек (от 2 до 1000):</label>
            <input type="number" id="pointsCount" name="pointsCount" min="2" max="1000" value="10">
            <button onclick="generateTable()">Создать таблицу</button>
        </div>

        <div id="pointsTable">
            <!-- Таблица будет создана здесь -->
        </div>

        <!-- БЛОК С КНОПКАМИ -->
        <div class="controls">
            <div style="display: flex; flex-direction: column; gap: 10px; width: 100%;">
                <button onclick="saveToDatabase()" id="saveBtn" disabled class="save-btn"
                        style="background-color: #4CAF50; padding: 12px 24px; font-size: 16px;">
                    💾 Сохранить в базу данных
                </button>
                <button onclick="createFunction()" id="createBtn" disabled
                        style="background-color: #757575; padding: 12px 24px; font-size: 16px;">
                    📤 Создать функцию (для операций)
                </button>
            </div>

            <span id="loading" class="loading">Обработка...</span>

            <!-- Блок для сообщений -->
            <div id="successSection" class="success-section">
                <div class="success-info">
                    <span class="success-name" id="successMessage"></span>
                    <span class="success-id" id="successId"></span>
                </div>
            </div>
        </div>
        <!-- КОНЕЦ БЛОКА С КНОПКАМИ -->

        <div style="margin-top: 20px;">
            <button onclick="goBack()" class="back-btn">Назад</button>
        </div>

    <div id="errorModal" class="error-modal">
        <div class="modal-content">
            <h3 id="errorTitle">Ошибка</h3>
            <p id="errorMessage"></p>
            <button onclick="closeErrorModal()">Закрыть</button>
        </div>
    </div>

    <script>
        // ПРОВЕРКА АВТОРИЗАЦИИ
        if (localStorage.getItem('isAuthenticated') !== 'true') {
            alert('Пожалуйста, войдите в систему');
            window.location.href = '${pageContext.request.contextPath}/ui/';
        }

        // Получаем контекст приложения
        const contextPath = '<%= request.getContextPath() %>';

        // Получаем параметры возврата из URL
        const urlParams = new URLSearchParams(window.location.search);
        const returnTo = urlParams.get('returnTo') || 'main';
        const panel = urlParams.get('panel') || '1';

        console.log('Return parameters - returnTo:', returnTo, 'panel:', panel);

        let currentPointsCount = 0;
        let currentFunctionName = '';

        function generateTable() {
            console.log('=== generateTable called ===');

            const countInput = document.getElementById('pointsCount');
            const count = parseInt(countInput.value);

            console.log('Запрошено точек:', count);

            if (isNaN(count) || count <= 0) {
                showError('Ошибка', 'Введите корректное положительное число');
                return;
            }

            if (count > 1000) {
                showError('Предупреждение', 'Слишком большое количество точек. Ограничено 1000.');
                currentPointsCount = 1000;
                countInput.value = 1000;
            } else {
                currentPointsCount = count;
            }

            // Создаем таблицу с помощью DOM API вместо строки
            const tableDiv = document.getElementById('pointsTable');
            tableDiv.innerHTML = ''; // Очищаем

            const table = document.createElement('table');
            const thead = document.createElement('thead');
            const tbody = document.createElement('tbody');

            // Заголовок
            const headerRow = document.createElement('tr');
            headerRow.innerHTML = '<th>№</th><th>Значение X</th><th>Значение Y</th>';
            thead.appendChild(headerRow);

            // Создаем строки
            for (let i = 0; i < currentPointsCount; i++) {
                const row = document.createElement('tr');

                // Номер
                const tdNum = document.createElement('td');
                tdNum.textContent = (i + 1);
                row.appendChild(tdNum);

                // Поле X
                const tdX = document.createElement('td');
                const inputX = document.createElement('input');
                inputX.type = 'number';
                inputX.id = 'x' + i;
                inputX.name = 'x' + i;
                inputX.step = 'any';
                inputX.required = true;
                inputX.style.width = '90%';
                inputX.style.padding = '5px';
                tdX.appendChild(inputX);
                row.appendChild(tdX);

                // Поле Y
                const tdY = document.createElement('td');
                const inputY = document.createElement('input');
                inputY.type = 'number';
                inputY.id = 'y' + i;
                inputY.name = 'y' + i;
                inputY.step = 'any';
                inputY.required = true;
                inputY.style.width = '90%';
                inputY.style.padding = '5px';
                tdY.appendChild(inputY);
                row.appendChild(tdY);

                tbody.appendChild(row);
            }

            table.appendChild(thead);
            table.appendChild(tbody);
            tableDiv.appendChild(table);

            document.getElementById('createBtn').disabled = false;
            document.getElementById('saveBtn').disabled = false;

            console.log('Таблица создана с', currentPointsCount, 'строками');

            // Очищаем все поля
            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const yInput = document.getElementById('y' + i);

                if (xInput) xInput.value = '';
                if (yInput) yInput.value = '';
            }
        }
        // Функция для проверки уникальности названия функции
        async function checkFunctionNameUniqueness(functionName) {
            try {
                const userId = getCurrentUserId();

                // Запрашиваем все функции пользователя
                const response = await fetch(`${contextPath}/api/functions/user?userId=${userId}`, {
                    headers: {
                        'Authorization': localStorage.getItem('authToken') || ''
                    }
                });

                if (response.ok) {
                    const userFunctions = await response.json();

                    // Проверяем, есть ли функция с таким именем
                    const existingFunction = userFunctions.find(func =>
                        func.name && func.name.toLowerCase() === functionName.toLowerCase()
                    );

                    return {
                        isUnique: !existingFunction,
                        existingFunction: existingFunction
                    };
                }
                return { isUnique: true }; // Если ошибка, разрешаем создание
            } catch (error) {
                console.error('Ошибка при проверке уникальности:', error);
                return { isUnique: true }; // При ошибке разрешаем создание
            }
        }

        // Функция для сохранения в базу данных
        async function saveToDatabase() {
            console.log('=== saveToDatabase called ===');

            // Получаем название функции
            const functionNameInput = document.getElementById('functionName');
            currentFunctionName = functionNameInput.value.trim();

            if (!currentFunctionName) {
                showError('Ошибка', 'Введите название функции');
                return;
            }

            // Проверяем заполненность точек
            let hasError = false;
            const points = [];

            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const yInput = document.getElementById('y' + i);

                if (!xInput || !yInput) {
                    showError('Ошибка', `Ошибка формы для точки ${i + 1}`);
                    hasError = true;
                    break;
                }

                const xValue = xInput.value.trim();
                const yValue = yInput.value.trim();

                if (xValue === '' || yValue === '') {
                    showError('Ошибка', `Заполните все значения для точки ${i + 1}`);
                    hasError = true;
                    break;
                }

                if (isNaN(parseFloat(xValue)) || isNaN(parseFloat(yValue))) {
                    showError('Ошибка', `Введите числовые значения для точки ${i + 1}`);
                    hasError = true;
                    break;
                }

                points.push({
                    x: parseFloat(xValue),
                    y: parseFloat(yValue)
                });
            }

            if (!hasError) {
                console.log('Сохраняем функцию в БД:', currentFunctionName);

                document.getElementById('loading').style.display = 'inline';
                document.getElementById('saveBtn').disabled = true;

                // 1. Сначала проверяем уникальность названия
                try {
                    const userId = getCurrentUserId();
                    console.log('Проверяем уникальность названия для пользователя:', userId);

                    // Запрашиваем ВСЕ функции (этот endpoint работает, вы его видите)
                    const checkResponse = await fetch(`${contextPath}/api/functions`, {
                        headers: {
                            'Authorization': localStorage.getItem('authToken') || '',
                            'Content-Type': 'application/json'
                        }
                    });

                    if (checkResponse.ok) {
                        const allFunctions = await checkResponse.json();
                        console.log('✅ Получены все функции. Всего:', allFunctions.length);

                        // Логируем первые несколько функций для отладки
                        console.log('Первые 3 функции для примера:', allFunctions.slice(0, 3));

                        // Фильтруем функции текущего пользователя
                        const userFunctions = allFunctions.filter(func => {
                            // Проверяем структуру объекта
                            console.log('Проверяем функцию:', func);

                            // Вариант 1: если userId есть как поле
                            if (func.userId !== undefined) {
                                return func.userId.toString() === userId.toString();
                            }

                            // Вариант 2: если userId есть как user_id
                            if (func.user_id !== undefined) {
                                return func.user_id.toString() === userId.toString();
                            }

                            // Вариант 3: если поле называется иначе
                            if (func.user !== undefined) {
                                return func.user.toString() === userId.toString();
                            }

                            return false;
                        });

                        console.log(`✅ Найдено ${userFunctions.length} функций у пользователя ${userId}`);

                        // Проверяем, есть ли функция с таким именем
                        const existingFunction = userFunctions.find(func => {
                            // Проверяем разные возможные названия полей
                            const funcName = func.name || func.functionName || func.func_name || '';
                            return funcName.toLowerCase() === currentFunctionName.toLowerCase();
                        });

                        if (existingFunction) {
                            // Безопасное получение данных
                            const existingId = existingFunction.id || existingFunction.functionId || 'N/A';
                            const existingName = existingFunction.name || existingFunction.functionName || currentFunctionName;

                            // Показываем ошибку В successSection (а не в модальном окне)
                            const successSection = document.getElementById('successSection');
                            const successMessage = document.getElementById('successMessage');
                            const successId = document.getElementById('successId');

                            // ПРЯМОЕ присвоение для ошибки
                            successMessage.textContent = '❌ Функция "' + String(existingName) + '" уже существует!';
                            successId.textContent = 'ID: ' + String(existingId);

                            // Стилизуем как ошибку
                            successSection.style.display = 'block';
                            successSection.querySelector('.success-info').style.backgroundColor = '#f8d7da'; // Красный фон
                            successSection.querySelector('.success-info').style.borderColor = '#f5c6cb';
                            successMessage.style.color = '#721c24'; // Темно-красный текст

                            // Автоматическое скрытие через 5 секунд
                            setTimeout(() => {
                                successSection.style.display = 'none';
                                // Восстанавливаем стандартные стили
                                successSection.querySelector('.success-info').style.backgroundColor = '';
                                successSection.querySelector('.success-info').style.borderColor = '';
                                successMessage.style.color = '';
                            }, 5000);

                            document.getElementById('loading').style.display = 'none';
                            document.getElementById('saveBtn').disabled = false;
                            return;
                        }

                        console.log('✅ Название уникально! Продолжаем сохранение...');

                    } else {
                        const errorText = await checkResponse.text();
                        console.warn('❌ Не удалось получить список функций. Статус:', checkResponse.status, 'Текст:', errorText);
                        // Продолжаем без проверки уникальности
                    }
                } catch (error) {
                    console.warn('⚠️ Ошибка при проверке уникальности:', error);
                    // Продолжаем создание даже если проверка не удалась
                }

                // 2. Если название уникально или проверка не удалась, сохраняем функцию
                const functionData = {
                    name: currentFunctionName,
                    expression: 'Создано из массивов',
                    points: points,
                    userId: getCurrentUserId()
                };

                console.log('Отправляемые данные:', functionData);

                // Отправляем запрос на API для сохранения
                fetch(contextPath + '/api/functions/save-from-arrays', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': localStorage.getItem('authToken') || ''
                    },
                    body: JSON.stringify(functionData)
                })
                .then(response => {
                    console.log('Ответ от сервера:', response.status);

                    if (!response.ok) {
                        return response.text().then(text => {
                            try {
                                const error = JSON.parse(text);
                                throw new Error(error.error || 'Ошибка сервера');
                            } catch {
                                throw new Error(text || `HTTP ${response.status}`);
                            }
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Функция сохранена:', data);

                    // Используем данные из ответа сервера
                    const functionName = data.name || currentFunctionName || 'Функция из массивов';
                    const functionId = data.id;
                    const pointsCount = data.pointsCount || points.length;

                    console.log('Отображаем:', { functionName, functionId, pointsCount });

                    // 1. Показываем сообщение в successSection
                    const successSection = document.getElementById('successSection');
                    const successMessage = document.getElementById('successMessage');
                    const successId = document.getElementById('successId');

                    // ПРЯМОЕ присвоение
                    successMessage.textContent = '✅Функция ' + String(functionName) + ' успешно создана! ';
                    successId.textContent = 'ID: ' + String(functionId);
                    successSection.style.display = 'block';

                    // Автоматическое скрытие через 5 секунд
                    setTimeout(() => {
                        successSection.style.display = 'none';
                    }, 5000);

                    // 2. Очищаем форму
                    document.getElementById('functionName').value = '';
                    document.getElementById('pointsCount').value = '10';

                    // 3. Обновляем таблицу
                    generateTable();

                    console.log('✅ Функция сохранена! ID:', functionId, 'Название:', functionName);
                })
                .catch(error => {
                    console.error('Ошибка при сохранении:', error);
                    showError('Ошибка сохранения', error.message);
                })
                .finally(() => {
                    document.getElementById('loading').style.display = 'none';
                    document.getElementById('saveBtn').disabled = false;
                });
            }
        }

        // Функция для получения ID текущего пользователя
        function getCurrentUserId() {
            // Здесь нужно реализовать получение ID пользователя
            // Можно через API или из localStorage
            // Временно возвращаем тестовое значение
            return 333290; // Замените на реальный ID пользователя
        }

        // Функция для создания функции (передача данных в родительское окно)
        async function createFunction() {
            console.log('=== createFunction called ===');

            // Получаем название функции
            const functionNameInput = document.getElementById('functionName');
            const currentFunctionName = functionNameInput.value.trim();

            if (!currentFunctionName) {
                showError('Ошибка', 'Введите название функции');
                return;
            }

            // Проверяем заполненность точек
            let hasError = false;
            const points = [];
            const xValues = [];
            const yValues = [];

            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const yInput = document.getElementById('y' + i);

                if (!xInput || !yInput) {
                    showError('Ошибка', `Ошибка формы для точки ${i + 1}`);
                    hasError = true;
                    break;
                }

                const xValue = xInput.value.trim();
                const yValue = yInput.value.trim();

                if (xValue === '' || yValue === '') {
                    showError('Ошибка', `Заполните все значения для точки ${i + 1}`);
                    hasError = true;
                    break;
                }

                if (isNaN(parseFloat(xValue)) || isNaN(parseFloat(yValue))) {
                    showError('Ошибка', `Введите числовые значения для точки ${i + 1}`);
                    hasError = true;
                    break;
                }

                const xNum = parseFloat(xValue);
                const yNum = parseFloat(yValue);

                points.push({ x: xNum, y: yNum });
                xValues.push(xNum);
                yValues.push(yNum);
            }

            if (hasError) {
                return;
            }

            document.getElementById('loading').style.display = 'block';
            document.getElementById('createBtn').disabled = true;

            try {
                // Создаем объект с данными функции
                const functionData = {
                    name: currentFunctionName,
                    expression: 'Создано из массивов',
                    xValues: xValues,
                    yValues: yValues,
                    points: points,
                    pointsCount: points.length,
                    timestamp: Date.now()
                };

                console.log('Создана функция:', functionData);

                // Пытаемся передать данные в родительское окно
                const transferred = returnFunctionData(functionData);

                if (!transferred) {
                    // Если не удалось передать через opener
                    // Показываем сообщение об успехе
                    const successSection = document.getElementById('successSection');
                    const successMessage = document.getElementById('successMessage');
                    const successId = document.getElementById('successId');

                    successMessage.textContent = '✅ Функция создана! Используется в текущем окне.';
                    successId.textContent = 'Точек: ' + points.length;
                    successSection.style.display = 'block';

                    // Автоматическое скрытие через 5 секунд
                    setTimeout(() => {
                        successSection.style.display = 'none';
                    }, 5000);

                    // Сохраняем в localStorage для использования на этой же странице
                    localStorage.setItem('currentFunctionData', JSON.stringify(functionData));

                    console.log('Функция сохранена в localStorage для использования на этой странице');
                }

            } catch (error) {
                console.error('Ошибка создания функции:', error);
                showError('Ошибка создания функции', error.message);
            } finally {
                document.getElementById('loading').style.display = 'none';
                document.getElementById('createBtn').disabled = false;
            }
        }

        // Универсальная функция возврата данных
        function returnFunctionData(data) {
            console.log('Возвращаем данные для:', returnTo, 'panel:', panel);
            console.log('Данные функции:', data);

            // Создаем объект с данными для передачи
            const result = {
                ...data,
                returnTo: returnTo,
                panel: panel,
                timestamp: Date.now()
            };

            // Пытаемся передать через window.opener
            if (window.opener && !window.opener.closed) {
                try {
                    console.log('Попытка передачи данных через opener...');

                    // Универсальный метод
                    if (window.opener.handleFunctionData) {
                        console.log('Используем handleFunctionData');
                        window.opener.handleFunctionData(result);
                        window.close();
                        return true;
                    }
                    // Для обратной совместимости с operations
                    else if (returnTo === 'operations' && window.opener.receiveFunctionData) {
                        console.log('Используем receiveFunctionData для operations');
                        window.opener.receiveFunctionData(parseInt(panel), data);
                        window.close();
                        return true;
                    }
                    // Для обратной совместимости с differentiation
                    else if (returnTo === 'differentiation' && window.opener.receiveFunctionData) {
                        console.log('Используем receiveFunctionData для differentiation');
                        window.opener.receiveFunctionData(data);
                        window.close();
                        return true;
                    }
                    // Для обратной совместимости с study
                    else if (returnTo === 'study' && window.opener.receiveFunctionData) {
                        console.log('Используем receiveFunctionData для study');
                        window.opener.receiveFunctionData(data);
                        window.close();
                        return true;
                    }
                    else {
                        console.log('Функция приема данных не найдена в opener');
                        return false;
                    }
                } catch (e) {
                    console.warn('Ошибка передачи данных через opener:', e);
                    return false;
                }
            }

            // Если не удалось через opener, используем localStorage
            console.log('Используем localStorage для передачи данных');
            localStorage.setItem('createdFunctionData', JSON.stringify(result));

            // Не закрываем окно, если нет opener
            return false;
        }

        // Проверяем, было ли окно открыто другой страницей
        function isWindowOpenedByParent() {
            return returnTo !== 'main' && returnTo !== '';
        }

        // Обновляем отображение кнопок в зависимости от контекста
        function updateUIForContext() {
            const saveBtn = document.getElementById('saveBtn');
            const createBtn = document.getElementById('createBtn');

            if (isWindowOpenedByParent()) {
                // Если окно открыто родительской страницей
                createBtn.style.backgroundColor = '#2196F3';
                createBtn.innerHTML = '📤 Передать функцию в ' + returnTo;
                createBtn.title = 'Вернет данные в родительское окно и закроет это окно';

                saveBtn.innerHTML = '💾 Также сохранить в БД';
            } else {
                // Если открыто напрямую
                saveBtn.style.backgroundColor = '#4CAF50';
                saveBtn.innerHTML = '💾 Сохранить в базу данных';

                createBtn.innerHTML = '📤 Создать функцию (для операций)';
                createBtn.style.backgroundColor = '#757575';
            }
        }

        function showError(title, message) {
            const errorTitle = document.getElementById('errorTitle');
            const errorMessage = document.getElementById('errorMessage');
            const errorModal = document.getElementById('errorModal');

            if (errorTitle && errorMessage && errorModal) {
                errorTitle.textContent = title;
                errorMessage.textContent = message;
                errorModal.style.display = 'flex';
            } else {
                console.error('Элементы модального окна не найдены');
                alert(title + ': ' + message);
            }
        }

        function closeErrorModal() {
            const errorModal = document.getElementById('errorModal');
            if (errorModal) {
                errorModal.style.display = 'none';
            }
        }

        function goBack() {
            if (returnTo === 'operations' || returnTo === 'differentiation') {
                window.close();
            } else {
                window.location.href = contextPath + '/ui';
            }
        }

        // Генерируем таблицу при загрузке
        window.onload = function() {
            console.log('Page loaded, generating initial table...');
            generateTable();
            updateUIForContext();
        };
    </script>
</body>
</html>