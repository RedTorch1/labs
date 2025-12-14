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

        /* БАЗОВЫЕ СТИЛИ - ОБЩИЕ ДЛЯ ВСЕХ ТЕМ */
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
            color: #333;
            background-color: white;
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
            color: #333;
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
            color: #333;
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

        /* Стили для сообщения об успехе */
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

        /* Стили для ошибок валидации */
        .error-input {
            border-color: #f44336 !important;
            background-color: #ffebee !important;
        }

        .error-message {
            color: #f44336;
            font-size: 12px;
            margin-top: 3px;
            display: none;
        }

        .error-row {
            background-color: #ffebee !important;
        }
        /* Стили для кнопки Отмена */
        .back-btn.cancel-btn {
            background-color: #f44336 !important;
        }

        .back-btn.cancel-btn:hover {
            background-color: #d32f2f !important;
        }

        /* ========== ТЕМНАЯ ТЕМА ========== */
        body.dark-theme {
            background-color: #1a1a1a !important;
            color: #f0f0f0 !important;
        }

        /* Текст в темной теме */
        .dark-theme,
        .dark-theme h1,
        .dark-theme label,
        .dark-theme .modal-content,
        .dark-theme table,
        .dark-theme th,
        .dark-theme td {
            color: #f0f0f0 !important;
        }

        /* Фоны в темной теме */
        .dark-theme .container,
        .dark-theme .modal-content {
            background-color: #2d2d2d !important;
            border: 1px solid #444 !important;
        }

        .dark-theme #pointsTable,
        .dark-theme table {
            background-color: #2d2d2d !important;
            border: 1px solid #555 !important;
        }

        .dark-theme th {
            background-color: #3d3d3d !important;
            border-color: #555 !important;
        }

        .dark-theme td {
            border-color: #555 !important;
        }

        .dark-theme input[type="number"],
        .dark-theme input[type="text"] {
            background-color: #3d3d3d !important;
            color: #f0f0f0 !important;
            border: 1px solid #555 !important;
        }

        .dark-theme input[type="number"]:focus,
        .dark-theme input[type="text"]:focus {
            border-color: #2196F3 !important;
            outline: none !important;
        }

        /* Кнопки в темной теме */
        .dark-theme button {
            background-color: #666 !important;
        }

        .dark-theme button:hover {
            background-color: #777 !important;
        }

        .dark-theme button.save-btn {
            background-color: #1565c0 !important;
        }

        .dark-theme button.save-btn:hover {
            background-color: #1976D2 !important;
        }

        .dark-theme .back-btn {
            background-color: #666 !important;
        }

        .dark-theme .back-btn:hover {
            background-color: #777 !important;
        }

        /* Сообщения в темной теме */
        .dark-theme .success-info {
            background-color: #1b5e20 !important;
            border-color: #2e7d32 !important;
        }

        .dark-theme .success-name {
            color: #a5d6a7 !important;
        }

        .dark-theme .success-id {
            color: #b2ebf2 !important;
            background-color: #006064 !important;
            border-color: #00838f !important;
        }

        /* Ошибки в темной теме */
        .dark-theme .modal-content h3 {
            color: #ef9a9a !important;
        }

        .dark-theme .modal-content button {
            background-color: #c62828 !important;
        }

        .dark-theme .modal-content button:hover {
            background-color: #d32f2f !important;
        }

        /* Стили для ошибок в темной теме */
        .dark-theme .error-input {
            border-color: #c62828 !important;
            background-color: #4a0000 !important;
        }

        .dark-theme .error-message {
            color: #ff5252 !important;
        }

        .dark-theme .error-row {
            background-color: #4a0000 !important;
        }

        /* Загрузка */
        .dark-theme .loading {
            color: #aaa !important;
        }
        /* Для темной темы */
        .dark-theme .back-btn.cancel-btn {
            background-color: #c62828 !important;
        }

        .dark-theme .back-btn.cancel-btn:hover {
            background-color: #d32f2f !important;
        }
    </style>
</head>
<body>
    <script>
        // Применяем тему при загрузке страницы
        document.addEventListener('DOMContentLoaded', function() {
            const savedTheme = localStorage.getItem('theme') || 'light';
            applyTheme(savedTheme);
        });

        function applyTheme(theme) {
            document.body.classList.remove('light-theme', 'dark-theme');
            if (theme === 'dark') {
                document.body.classList.add('dark-theme');
            } else {
                document.body.classList.add('light-theme');
            }
        }
    </script>

    <div class="container">
        <h1>Создание табулированной функции из массивов</h1>

        <div class="form-group">
            <label for="functionName">Название функции:</label>
            <input type="text" id="functionName" name="functionName" placeholder="Введите название функции" style="width: 300px; padding: 8px; margin-bottom: 10px;">
        </div>

        <div class="form-group">
            <label for="pointsCount">Количество точек (от 2 до 10000):</label>
            <input type="number" id="pointsCount" name="pointsCount" min="2" max="10000" value="10">
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
            <button onclick="goBack()" class="back-btn" id="backButton">Назад</button>
        </div>
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

        // Переменные для отслеживания состояния
        let currentPointsCount = 0;
        let currentFunctionName = '';
        let validationErrors = {};

        function generateTable() {
            console.log('=== generateTable called ===');

            const countInput = document.getElementById('pointsCount');
            let count = parseInt(countInput.value);

            console.log('Запрошено точек:', count);

            if (isNaN(count) || count <= 0) {
                showError('Ошибка', 'Введите корректное положительное число');
                return;
            }

            // Проверяем минимальное количество точек
            if (count < 2) {
                showError('Ошибка', 'Минимальное количество точек - 2');
                count = 2;
                countInput.value = 2;
            }

            // Проверяем максимальное количество точек
            if (count > 10000) {
                showError('Ошибка', 'Максимальное количество точек - 10000');
                count = 10000;
                countInput.value = 10000;
            }

            currentPointsCount = count;

            // Создаем таблицу с помощью DOM API вместо строки
            const tableDiv = document.getElementById('pointsTable');
            tableDiv.innerHTML = ''; // Очищаем

            const table = document.createElement('table');
            const thead = document.createElement('thead');
            const tbody = document.createElement('tbody');

            // Заголовок
            const headerRow = document.createElement('tr');
            headerRow.innerHTML = '<th>№</th><th>Значение X</th><th>Значение Y</th><th style="width: 150px;">Ошибки</th>';
            thead.appendChild(headerRow);

            // Создаем строки
            for (let i = 0; i < currentPointsCount; i++) {
                const row = document.createElement('tr');
                row.id = 'row-' + i;

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
                inputX.addEventListener('input', () => validateXValues());
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

                // Ячейка для сообщений об ошибках
                const tdError = document.createElement('td');
                const errorSpan = document.createElement('span');
                errorSpan.className = 'error-message';
                errorSpan.id = 'error-' + i;
                tdError.appendChild(errorSpan);
                row.appendChild(tdError);

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

            // Очищаем ошибки валидации
            validationErrors = {};
        }

        // Функция для проверки уникальности X значений
        function validateXValues() {
            const xValues = [];
            const duplicates = [];

            // Собираем все X значения
            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                if (xInput && xInput.value.trim() !== '') {
                    const xValue = parseFloat(xInput.value);
                    if (!isNaN(xValue)) {
                        xValues.push({index: i, value: xValue});
                    }
                }
            }

            // Проверяем на дубликаты
            const seen = {};
            xValues.forEach(item => {
                if (seen[item.value] !== undefined) {
                    duplicates.push({index: item.index, duplicateOf: seen[item.value]});
                } else {
                    seen[item.value] = item.index;
                }
            });

            // Очищаем предыдущие ошибки
            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const errorSpan = document.getElementById('error-' + i);
                const row = document.getElementById('row-' + i);

                if (xInput) {
                    xInput.classList.remove('error-input');
                }
                if (errorSpan) {
                    errorSpan.style.display = 'none';
                    errorSpan.textContent = '';
                }
                if (row) {
                    row.classList.remove('error-row');
                }
            }

            // Показываем ошибки для дубликатов
            duplicates.forEach(dup => {
                const xInput = document.getElementById('x' + dup.index);
                const errorSpan = document.getElementById('error-' + dup.index);
                const row = document.getElementById('row-' + dup.index);

                if (xInput && errorSpan && row) {
                    xInput.classList.add('error-input');
                    errorSpan.textContent = `X совпадает со строкой ${dup.duplicateOf + 1}`;
                    errorSpan.style.display = 'block';
                    row.classList.add('error-row');
                }
            });

            return duplicates.length === 0;
        }

        // Функция для проверки валидности всех данных
        function validateAllData() {
            const errors = [];
            const xValuesSet = new Set();

            // Проверяем каждую точку
            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const yInput = document.getElementById('y' + i);

                if (!xInput || !yInput) {
                    errors.push(`Ошибка формы для точки ${i + 1}`);
                    continue;
                }

                const xValue = xInput.value.trim();
                const yValue = yInput.value.trim();

                // Проверяем, что поля не пустые
                if (xValue === '' || yValue === '') {
                    errors.push(`Заполните все значения для точки ${i + 1}`);
                    continue;
                }

                // Проверяем, что значения - числа
                if (isNaN(parseFloat(xValue)) || isNaN(parseFloat(yValue))) {
                    errors.push(`Введите числовые значения для точки ${i + 1}`);
                    continue;
                }

                // Проверяем уникальность X
                const xNum = parseFloat(xValue);
                if (xValuesSet.has(xNum)) {
                    errors.push(`X значение в точке ${i + 1} уже используется`);
                    continue;
                }
                xValuesSet.add(xNum);
            }

            // Проверяем минимальное количество точек
            if (xValuesSet.size < 2) {
                errors.push('Необходимо как минимум 2 точки с различными X значениями');
            }

            return errors;
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

            // Проверяем уникальность X
            if (!validateXValues()) {
                showError('Ошибка', 'Имеются повторяющиеся значения X. Убедитесь, что все X уникальны.');
                return;
            }

            // Проверяем валидность всех данных
            const validationErrors = validateAllData();
            if (validationErrors.length > 0) {
                showError('Ошибка валидации', validationErrors.join('\n'));
                return;
            }

            // Собираем данные точек
            const points = [];
            const xValuesSet = new Set();

            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const yInput = document.getElementById('y' + i);

                const xValue = parseFloat(xInput.value);
                const yValue = parseFloat(yInput.value);

                // Проверяем уникальность X еще раз
                if (xValuesSet.has(xValue)) {
                    showError('Ошибка', `X значение ${xValue} встречается более одного раза`);
                    return;
                }
                xValuesSet.add(xValue);

                points.push({
                    x: xValue,
                    y: yValue
                });
            }

            console.log('Сохраняем функцию в БД:', currentFunctionName);

            document.getElementById('loading').style.display = 'inline';
            document.getElementById('saveBtn').disabled = true;

            // Сохраняем функцию
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

                // Показываем сообщение в successSection
                const successSection = document.getElementById('successSection');
                const successMessage = document.getElementById('successMessage');
                const successId = document.getElementById('successId');

                successMessage.textContent = '✅Функция ' + String(functionName) + ' успешно создана! ';
                successId.textContent = 'ID: ' + String(functionId);
                successSection.style.display = 'block';

                // Автоматическое скрытие через 5 секунд
                setTimeout(() => {
                    successSection.style.display = 'none';
                }, 5000);

                // Очищаем форму
                document.getElementById('functionName').value = '';
                document.getElementById('pointsCount').value = '10';

                // Обновляем таблицу
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

        // Функция для получения ID текущего пользователя
        function getCurrentUserId() {
            const storedUserId = localStorage.getItem('userId');
            if (storedUserId) {
                return parseInt(storedUserId);
            }
            return 333290; // Тестовое значение
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

            // Проверяем уникальность X
            if (!validateXValues()) {
                showError('Ошибка', 'Имеются повторяющиеся значения X. Убедитесь, что все X уникальны.');
                return;
            }

            // Проверяем валидность всех данных
            const validationErrors = validateAllData();
            if (validationErrors.length > 0) {
                showError('Ошибка валидации', validationErrors.join('\n'));
                return;
            }

            // Собираем данные точек
            const points = [];
            const xValues = [];
            const yValues = [];
            const xValuesSet = new Set();

            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const yInput = document.getElementById('y' + i);

                const xValue = parseFloat(xInput.value);
                const yValue = parseFloat(yInput.value);

                // Проверяем уникальность X еще раз
                if (xValuesSet.has(xValue)) {
                    showError('Ошибка', `X значение ${xValue} встречается более одного раза`);
                    return;
                }
                xValuesSet.add(xValue);

                points.push({ x: xValue, y: yValue });
                xValues.push(xValue);
                yValues.push(yValue);
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

        function goBack() {
            // Определяем, была ли страница открыта из другого окна
            if (returnTo !== 'main') {
                // Если открыта из другой страницы (operations, differentiation, study)
                window.close();
            } else {
                // Если открыта напрямую
                window.location.href = contextPath + '/ui';
            }
        }
        // Обновляем кнопку при загрузке страницы
        function updateBackButton() {
            const backButton = document.getElementById('backButton');
            if (backButton) {
                if (returnTo !== 'main') {
                    // Меняем текст и добавляем класс для стилизации
                    backButton.textContent = 'Отмена';
                    backButton.className = 'back-btn cancel-btn';
                }
            }
        }

        // Обновляем функцию showError для поддержки темной темы
        function showError(title, message) {
            const errorTitle = document.getElementById('errorTitle');
            const errorMessage = document.getElementById('errorMessage');
            const errorModal = document.getElementById('errorModal');

            if (errorTitle && errorMessage && errorModal) {
                errorTitle.textContent = title;
                errorMessage.textContent = message;
                errorModal.style.display = 'flex';

                // Устанавливаем цвет кнопки в модальном окне
                const modalButton = errorModal.querySelector('button');
                if (modalButton) {
                    modalButton.style.backgroundColor = '#f44336';
                    if (document.body.classList.contains('dark-theme')) {
                        modalButton.style.backgroundColor = '#c62828';
                    }
                }
            } else {
                console.error('Элементы модального окна не найдены');
                alert(title + ': ' + message);
            }
        }

        // Добавляем обновление при загрузке
        window.onload = function() {
            console.log('Page loaded, generating initial table...');
            generateTable();
            updateBackButton(); // Обновляем кнопку

            // Применяем тему еще раз для модального окна
            const savedTheme = localStorage.getItem('theme') || 'light';
            applyTheme(savedTheme);
        };
    </script>
</body>
</html>