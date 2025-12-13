<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создание функции из математической функции</title>
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
            max-width: 600px;
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
            border-bottom: 2px solid #2196F3;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        select, input[type="number"], input[type="text"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }

        select {
            background-color: white;
            cursor: pointer;
        }

        .input-group {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .input-group input {
            flex: 1;
        }

        button {
            padding: 12px 24px;
            background-color: #2196F3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
            margin-top: 10px;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #1976D2;
        }

        button:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }

        button.save-btn {
            background-color: #4CAF50;
        }

        button.save-btn:hover {
            background-color: #45a049;
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
            width: auto;
        }

        .modal-content button:hover {
            background-color: #d32f2f;
        }

        .loading {
            display: none;
            text-align: center;
            color: #666;
            font-style: italic;
            margin-top: 10px;
        }

        .function-description {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 4px;
            margin-top: 10px;
            border-left: 4px solid #2196F3;
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
            width: 100%;
            border: none;
            cursor: pointer;
            font-size: 16px;
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

        /* Стили для ошибки */
        .error-style {
            background-color: #f8d7da !important;
            border-color: #f5c6cb !important;
        }

        .error-style .success-name {
            color: #721c24 !important;
        }

        .error-style .success-id {
            color: #856404 !important;
            background-color: #fff3cd !important;
            border-color: #ffeaa7 !important;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Создание функции из математической функции</h1>

        <form id="createFunctionForm">
            <div class="form-group">
                <label for="functionNameInput">Название функции:</label>
                <input type="text" id="functionNameInput" name="functionNameInput"
                       placeholder="Введите уникальное название функции" required>
            </div>

            <div class="form-group">
                <label for="functionSelect">Выберите математическую функцию:</label>
                <select id="functionSelect" name="functionSelect" required>
                    <option value="">-- Выберите функцию --</option>
                </select>
                <div id="functionDescription" class="function-description" style="display: none;">
                    <strong>Описание:</strong> <span id="descriptionText"></span>
                </div>
            </div>

            <div class="form-group">
                <label>Интервал разбиения:</label>
                <div class="input-group">
                    <input type="number" id="xFrom" name="xFrom" step="any" placeholder="От" required>
                    <span>до</span>
                    <input type="number" id="xTo" name="xTo" step="any" placeholder="До" required>
                </div>
            </div>

            <div class="form-group">
                <label for="pointsCount">Количество точек разбиения (от 2 до 10000):</label>
                <input type="number" id="pointsCount" name="pointsCount" min="2" max="10000" value="100" required>
            </div>

            <!-- БЛОК С КНОПКАМИ - ТОЛЬКО ОДИН РАЗ -->
            <div class="controls">
                <div style="display: flex; flex-direction: column; gap: 10px; width: 100%;">
                    <button type="button" onclick="saveToDatabase()" id="saveBtn" class="save-btn"
                            style="background-color: #4CAF50; padding: 12px 24px; font-size: 16px;">
                        💾 Сохранить в базу данных
                    </button>
                    <button type="button" onclick="createFunction()" id="createBtn"
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
                <button type="button" onclick="goBack()" class="back-btn">Назад</button>
            </div>
        </form>
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

        // Получаем параметры возврата
        const returnTo = '<%= request.getParameter("returnTo") != null ? request.getParameter("returnTo") : "main" %>';
        const panel = '<%= request.getParameter("panel") != null ? request.getParameter("panel") : "1" %>';

        console.log('Return parameters - returnTo:', returnTo, 'panel:', panel);

        // Функция для вычисления значения функции
        function calculateFunctionValue(functionName, x) {
            switch(functionName) {
                case 'Квадратичная функция':
                    return x * x;
                case 'Тождественная функция':
                    return x;
                case 'Постоянная функция (0)':
                    return 0;
                case 'Постоянная функция (1)':
                    return 1;
                case 'Синусоида':
                    return Math.sin(x);
                default:
                    return x;
            }
        }

        // Функция для получения ID текущего пользователя
        function getCurrentUserId() {
            const storedUserId = localStorage.getItem('userId');
            if (storedUserId) {
                return parseInt(storedUserId);
            }
            return 333290; // Тестовое значение
        }

        // Функция для проверки уникальности названия
        async function checkFunctionNameUniqueness(functionName) {
            try {
                const userId = getCurrentUserId();

                // Запрашиваем все функции
                const response = await fetch(`${contextPath}/api/functions`, {
                    headers: {
                        'Authorization': localStorage.getItem('authToken') || '',
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const allFunctions = await response.json();

                    // Фильтруем функции текущего пользователя
                    const userFunctions = allFunctions.filter(func =>
                        func.userId && func.userId.toString() === userId.toString()
                    );

                    // Проверяем, есть ли функция с таким именем
                    const existingFunction = userFunctions.find(func =>
                        func.name && func.name.toLowerCase() === functionName.toLowerCase()
                    );

                    return {
                        isUnique: !existingFunction,
                        existingFunction: existingFunction
                    };
                }
                return { isUnique: true };
            } catch (error) {
                console.error('Ошибка при проверке уникальности:', error);
                return { isUnique: true };
            }
        }

        // Функция для сохранения в базу данных
        async function saveToDatabase() {
            console.log('=== saveToDatabase called ===');

            const functionNameInput = document.getElementById('functionNameInput');
            const currentFunctionName = functionNameInput.value.trim();

            if (!currentFunctionName) {
                showError('Ошибка', 'Введите название функции');
                return;
            }

            // Проверяем уникальность названия
            document.getElementById('loading').style.display = 'block';
            document.getElementById('saveBtn').disabled = true;

            try {
                const uniquenessCheck = await checkFunctionNameUniqueness(currentFunctionName);

                if (!uniquenessCheck.isUnique) {
                    const existingFunction = uniquenessCheck.existingFunction;
                    const existingId = existingFunction.id || existingFunction.functionId || 'N/A';
                    const existingName = existingFunction.name || existingFunction.functionName || currentFunctionName;

                    // Показываем ошибку в successSection
                    const successSection = document.getElementById('successSection');
                    const successMessage = document.getElementById('successMessage');
                    const successId = document.getElementById('successId');

                    successMessage.textContent = '❌ Функция "' + existingName + '" уже существует!';
                    successId.textContent = 'ID: ' + existingId;

                    // Стилизуем как ошибку
                    successSection.style.display = 'block';
                    const successInfo = successSection.querySelector('.success-info');
                    successInfo.classList.add('error-style');

                    // Автоматическое скрытие через 5 секунд
                    setTimeout(() => {
                        successSection.style.display = 'none';
                        successInfo.classList.remove('error-style');
                    }, 5000);

                    document.getElementById('loading').style.display = 'none';
                    document.getElementById('saveBtn').disabled = false;
                    return;
                }
            } catch (error) {
                console.warn('Не удалось проверить уникальность:', error);
                // Продолжаем, даже если проверка не удалась
            }

            // Проверяем остальные поля
            const form = document.getElementById('createFunctionForm');
            if (!form.checkValidity()) {
                form.reportValidity();
                document.getElementById('loading').style.display = 'none';
                document.getElementById('saveBtn').disabled = false;
                return;
            }

            const functionSelect = document.getElementById('functionSelect').value;
            const xFrom = parseFloat(document.getElementById('xFrom').value);
            const xTo = parseFloat(document.getElementById('xTo').value);
            const pointsCount = parseInt(document.getElementById('pointsCount').value);

            if (isNaN(xFrom) || isNaN(xTo)) {
                showError('Ошибка', 'Введите корректные числовые значения для интервала');
                document.getElementById('loading').style.display = 'none';
                document.getElementById('saveBtn').disabled = false;
                return;
            }

            if (xFrom >= xTo) {
                showError('Ошибка', 'Начало интервала должно быть меньше конца');
                document.getElementById('loading').style.display = 'none';
                document.getElementById('saveBtn').disabled = false;
                return;
            }

            if (isNaN(pointsCount) || pointsCount < 2 || pointsCount > 10000) {
                showError('Ошибка', 'Количество точек должно быть от 2 до 10000');
                document.getElementById('loading').style.display = 'none';
                document.getElementById('saveBtn').disabled = false;
                return;
            }

            // Генерируем точки
            const step = (xTo - xFrom) / (pointsCount - 1);
            const points = [];

            for (let i = 0; i < pointsCount; i++) {
                const x = xFrom + (i * step);
                const y = calculateFunctionValue(functionSelect, x);
                points.push({ x: x, y: y });
            }

            // Подготавливаем данные для отправки
            const functionData = {
                name: currentFunctionName,
                expression: functionSelect,
                points: points,
                userId: getCurrentUserId()
            };

            console.log('Отправляемые данные:', functionData);

            // Отправляем запрос на API
            fetch(contextPath + '/api/functions/save-from-function', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem('authToken') || ''
                },
                body: JSON.stringify(functionData)
            })
            .then(response => {
                document.getElementById('loading').style.display = 'none';
                document.getElementById('saveBtn').disabled = false;

                if (!response.ok) {
                    return response.json().then(error => {
                        throw new Error(`${error.error || 'Ошибка'}: ${error.details || error.message || 'Неизвестная ошибка'}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                console.log('Функция создана:', data);

                // Показываем сообщение об успехе
                const successSection = document.getElementById('successSection');
                const successMessage = document.getElementById('successMessage');
                const successId = document.getElementById('successId');

                successMessage.textContent = '✅ Функция ' + String(data.name || currentFunctionName) + ' успешно создана!';
                successId.textContent = 'ID: ' + String(data.id);
                successSection.style.display = 'block';

                // Убираем стили ошибки если были
                const successInfo = successSection.querySelector('.success-info');
                successInfo.classList.remove('error-style');

                // Автоматическое скрытие через 5 секунд
                setTimeout(() => {
                    successSection.style.display = 'none';
                }, 5000);

                // Очищаем форму
                functionNameInput.value = '';
                document.getElementById('functionSelect').value = '';
                document.getElementById('xFrom').value = '';
                document.getElementById('xTo').value = '';
                document.getElementById('pointsCount').value = '100';

            })
            .catch(error => {
                console.error('Ошибка создания функции:', error);
                showError('Ошибка создания функции', error.message);
            });
        }

        // Функция для создания функции (передача данных в родительское окно)
        async function createFunction() {
            console.log('=== createFunction called ===');

            const form = document.getElementById('createFunctionForm');

            // Базовая валидация
            if (!form.checkValidity()) {
                form.reportValidity();
                return;
            }

            const functionSelect = document.getElementById('functionSelect').value;
            const xFrom = parseFloat(document.getElementById('xFrom').value);
            const xTo = parseFloat(document.getElementById('xTo').value);
            const pointsCount = parseInt(document.getElementById('pointsCount').value);

            // Валидация данных
            if (!functionSelect) {
                showError('Ошибка', 'Выберите математическую функцию');
                return;
            }

            if (isNaN(xFrom) || isNaN(xTo)) {
                showError('Ошибка', 'Введите корректные числовые значения для интервала');
                return;
            }

            if (xFrom >= xTo) {
                showError('Ошибка', 'Начало интервала должно быть меньше конца');
                return;
            }

            if (isNaN(pointsCount) || pointsCount < 2 || pointsCount > 10000) {
                showError('Ошибка', 'Количество точек должно быть от 2 до 10000');
                return;
            }

            document.getElementById('loading').style.display = 'block';
            document.getElementById('createBtn').disabled = true;

            try {
                // Генерируем точки функции
                const step = (xTo - xFrom) / (pointsCount - 1);
                const points = [];
                const xValues = [];
                const yValues = [];

                for (let i = 0; i < pointsCount; i++) {
                    const x = xFrom + (i * step);
                    const y = calculateFunctionValue(functionSelect, x);
                    points.push({ x: x, y: y });
                    xValues.push(x);
                    yValues.push(y);
                }

                // Создаем объект с данными функции
                const functionData = {
                    name: functionSelect + ' [' + xFrom + ', ' + xTo + ']',
                    expression: functionSelect,
                    xValues: xValues,
                    yValues: yValues,
                    points: points,
                    xFrom: xFrom,
                    xTo: xTo,
                    pointsCount: pointsCount,
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
                    successId.textContent = 'Точек: ' + pointsCount;
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

        // Можем обновить отображение кнопок в зависимости от контекста
        function updateUIForContext() {
            const saveBtn = document.getElementById('saveBtn');
            const createBtn = document.getElementById('createBtn');

            if (isWindowOpenedByParent()) {
                // Если окно открыто родительской страницей, делаем кнопку createFunction более заметной
                createBtn.style.backgroundColor = '#2196F3';
                createBtn.innerHTML = '📤 Передать функцию в ' + returnTo;
                createBtn.title = 'Вернет данные в родительское окно и закроет это окно';
            } else {
                // Если открыто напрямую, делаем кнопку saveToDatabase более заметной
                saveBtn.style.backgroundColor = '#4CAF50';
                saveBtn.innerHTML = '💾 Сохранить в базу данных';
                createBtn.innerHTML = '🧪 Создать функцию (для теста)';
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

        // Загрузка списка функций при загрузке страницы
        window.onload = function() {
            updateUIForContext();
            fetch(contextPath + '/ui/functions')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Ошибка загрузки: ' + response.status);
                    }
                    return response.json();
                })
                .then(functions => {
                    const select = document.getElementById('functionSelect');
                    functions.forEach(funcName => {
                        const option = document.createElement('option');
                        option.value = funcName;
                        option.textContent = funcName;
                        select.appendChild(option);
                    });

                    select.addEventListener('change', function() {
                        const descriptionDiv = document.getElementById('functionDescription');
                        const descriptionText = document.getElementById('descriptionText');

                        if (this.value) {
                            switch(this.value) {
                                case 'Квадратичная функция':
                                    descriptionText.textContent = 'f(x) = x²';
                                    break;
                                case 'Тождественная функция':
                                    descriptionText.textContent = 'f(x) = x';
                                    break;
                                case 'Постоянная функция (0)':
                                    descriptionText.textContent = 'f(x) = 0';
                                    break;
                                case 'Постоянная функция (1)':
                                    descriptionText.textContent = 'f(x) = 1';
                                    break;
                                case 'Синусоида':
                                    descriptionText.textContent = 'f(x) = sin(x)';
                                    break;
                                default:
                                    descriptionText.textContent = this.value;
                            }
                            descriptionDiv.style.display = 'block';
                        } else {
                            descriptionDiv.style.display = 'none';
                        }
                    });
                })
                .catch(error => {
                    showError('Ошибка', 'Не удалось загрузить список функций: ' + error.message);
                });
        };
    </script>
</body>
</html>