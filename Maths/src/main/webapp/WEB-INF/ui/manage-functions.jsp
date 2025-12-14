<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление функциями</title>
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
            max-width: 1200px;
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

        .functions-list {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .function-card {
            background: white;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }

        .function-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 10px rgba(0,0,0,0.15);
        }

        .function-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }

        .function-name {
            font-size: 18px;
            font-weight: bold;
            color: #2196F3;
        }

        .function-id {
            background: #f0f0f0;
            padding: 3px 8px;
            border-radius: 4px;
            font-size: 12px;
            color: #666;
        }

        .function-info {
            margin-bottom: 15px;
        }

        .function-info div {
            margin-bottom: 5px;
            font-size: 14px;
        }

        .function-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
            flex-wrap: wrap;
        }

        button {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.3s;
        }

        .add-point-btn {
            background-color: #FF9800;
            color: white;
        }

        .add-point-btn:hover {
            background-color: #F57C00;
        }
        .delete-point-btn {
             background-color: #9C27B0;
             color: white;
         }
         .delete-point-btn:hover {
             background-color: #7B1FA2;
         }

        .delete-btn {
            background-color: #f44336;
            color: white;
        }

        .delete-btn:hover {
            background-color: #d32f2f;
        }

        .view-btn {
            background-color: #2196F3;
            color: white;
        }

        .view-btn:hover {
            background-color: #1976D2;
        }

        .back-btn {
            background-color: #757575;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            margin-top: 20px;
        }

        .back-btn:hover {
            background-color: #616161;
        }

        .loading {
            display: none;
            text-align: center;
            color: #666;
            font-style: italic;
            margin: 20px 0;
        }

        .success-message, .error-message {
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
            display: none;
        }

        .success-message {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background: white;
            padding: 30px;
            border-radius: 8px;
            max-width: 800px;
            width: 90%;
            max-height: 90vh;
            overflow-y: auto;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .modal-header h3 {
            margin: 0;
            color: #333;
        }

        .close-btn {
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
            color: #666;
        }

        .points-table {
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

        .add-point-form {
            margin-top: 20px;
            padding: 15px;
            background: #f9f9f9;
            border-radius: 4px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .form-actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        .confirm-delete-modal {
            text-align: center;
        }

        .confirm-delete-modal p {
            margin-bottom: 20px;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Управление функциями</h1>

        <div id="loading" class="loading">Загрузка функций...</div>

        <div id="errorMessage" class="error-message"></div>
        <div id="successMessage" class="success-message"></div>

        <div id="functionsList" class="functions-list">
            <!-- Функции будут загружены сюда -->
        </div>

        <a href="${pageContext.request.contextPath}/ui" class="back-btn">Назад в главное меню</a>
    </div>

    <!-- Модальное окно для просмотра/добавления точек -->
    <div id="editModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">Редактирование функции</h3>
                <button class="close-btn" onclick="closeEditModal()">×</button>
            </div>

            <div id="modalContent">
                <!-- Содержимое будет загружено динамически -->
            </div>
        </div>
    </div>

    <!-- Модальное окно подтверждения удаления -->
    <div id="confirmDeleteModal" class="modal">
        <div class="modal-content confirm-delete-modal">
            <div class="modal-header">
                <h3>Подтверждение удаления</h3>
                <button class="close-btn" onclick="closeConfirmDeleteModal()">×</button>
            </div>
            <p id="deleteConfirmText">Вы уверены, что хотите удалить эту функцию?</p>
            <div class="form-actions">
                <button onclick="confirmDeleteFunction()" class="delete-btn">Да, удалить</button>
                <button onclick="closeConfirmDeleteModal()" style="background-color: #757575; color: white;">Отмена</button>
            </div>
        </div>
    </div>

    <script>
        // ПРОВЕРКА АВТОРИЗАЦИИ
        if (localStorage.getItem('isAuthenticated') !== 'true') {
            alert('Пожалуйста, войдите в систему');
            window.location.href = '${pageContext.request.contextPath}/ui/';
            // Останавливаем выполнение скрипта
            throw new Error('Пользователь не авторизован');
        }

        // Устанавливаем имя пользователя если есть элемент
        const username = localStorage.getItem('username');
        const userElement = document.getElementById('currentUser');
        if (username && userElement) {
            userElement.textContent = username;
        }

        // Получаем контекст приложения
        const contextPath = '${pageContext.request.contextPath}';
        console.log('Context path:', contextPath);

        // Переменные для хранения текущих данных
        let currentFunctions = [];
        let currentFunctionId = null;

        // Константы
        const MAX_POINTS_PER_FUNCTION = 10000;

        // Загрузка функций при загрузке страницы
        window.onload = function() {
            loadUserFunctions();
        };

        // Функция выхода из системы
        function logout() {
            if (confirm('Вы уверены, что хотите выйти?')) {
                // Очищаем данные авторизации
                localStorage.removeItem('isAuthenticated');
                localStorage.removeItem('authToken');
                localStorage.removeItem('userId');
                localStorage.removeItem('username');

                // Перенаправляем на главную страницу
                window.location.href = contextPath + '/ui/';
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

        // Вспомогательная функция для получения заголовков авторизации
        function getAuthHeaders(contentType = 'application/json') {
            const headers = {};

            const authToken = localStorage.getItem('authToken');
            if (authToken) {
                headers['Authorization'] = authToken;
            }

            if (contentType) {
                headers['Content-Type'] = contentType;
            }

            return headers;
        }

        // Загрузка функций пользователя
        async function loadUserFunctions() {
            document.getElementById('loading').style.display = 'block';
            document.getElementById('functionsList').innerHTML = '';

            try {
                const userId = getCurrentUserId();

                // Получаем все функции
                const response = await fetch(`${contextPath}/api/functions`, {
                    headers: getAuthHeaders()
                });

                if (response.ok) {
                    const allFunctions = await response.json();

                    // Отладочная информация
                    console.log('Все функции с сервера (первые 3):', allFunctions.slice(0, 3));
                    console.log('Всего функций:', allFunctions.length);

                    // Фильтруем функции текущего пользователя
                    currentFunctions = allFunctions.filter(func =>
                        func.userId && func.userId.toString() === userId.toString()
                    );

                    console.log('Загружено функций текущего пользователя:', currentFunctions.length);
                    console.log('Функции пользователя (первые 3):', currentFunctions.slice(0, 3));

                    if (currentFunctions.length === 0) {
                        document.getElementById('functionsList').innerHTML =
                            '<div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #666;">' +
                            '   <p>У вас пока нет сохраненных функций.</p>' +
                            '   <p>Создайте функцию на страницах "Создать из массивов" или "Создать из функции".</p>' +
                            '</div>';
                    } else {
                        renderFunctionsList(currentFunctions);
                    }

                } else {
                    showError('Ошибка загрузки', 'Не удалось загрузить список функций. Статус: ' + response.status);
                }

            } catch (error) {
                console.error('Ошибка при загрузке функций:', error);
                showError('Ошибка', 'Ошибка при загрузке функций: ' + error.message);
            } finally {
                document.getElementById('loading').style.display = 'none';
            }
        }

        // Отображение списка функций
        function renderFunctionsList(functions) {
            const container = document.getElementById('functionsList');
            container.innerHTML = '';

            console.log('Начинаем рендеринг функций:', functions.length);

            functions.forEach(func => {
                console.log('Обрабатываем функцию:', func);

                // Проверяем, что функция имеет ID
                if (func.id === undefined || func.id === null) {
                    console.warn('Функция без ID пропущена:', func);
                    return;
                }

                const functionCard = document.createElement('div');
                functionCard.className = 'function-card';

                const funcId = func.id;
                const funcName = func.name !== null && func.name !== undefined && func.name !== false
                    ? String(func.name)
                    : 'Без названия';
                const funcUserId = func.userId !== null && func.userId !== undefined
                    ? String(func.userId)
                    : 'Нет ID';

                // Создаем элементы DOM напрямую
                const header = document.createElement('div');
                header.className = 'function-header';

                const nameSpan = document.createElement('span');
                nameSpan.className = 'function-name';
                nameSpan.textContent = funcName;

                const idSpan = document.createElement('span');
                idSpan.className = 'function-id';
                idSpan.textContent = 'ID: ' + funcId;

                header.appendChild(nameSpan);
                header.appendChild(idSpan);

                const info = document.createElement('div');
                info.className = 'function-info';

                const userDiv = document.createElement('div');
                const userStrong = document.createElement('strong');
                userStrong.textContent = 'ID пользователя: ';
                userDiv.appendChild(userStrong);
                userDiv.appendChild(document.createTextNode(funcUserId));

                const pointsDiv = document.createElement('div');
                const pointsStrong = document.createElement('strong');
                pointsStrong.textContent = 'Точек: ';
                pointsDiv.appendChild(pointsStrong);

                // Создаем span для количества точек
                const pointsSpan = document.createElement('span');
                pointsSpan.className = 'points-count';
                pointsSpan.textContent = 'Загрузка...';
                pointsDiv.appendChild(pointsSpan);

                info.appendChild(userDiv);
                info.appendChild(pointsDiv);

                const actions = document.createElement('div');
                actions.className = 'function-actions';

                // Кнопка "Просмотр точек"
                const viewBtn = document.createElement('button');
                viewBtn.className = 'view-btn';
                viewBtn.textContent = 'Просмотр точек';
                viewBtn.addEventListener('click', () => viewFunctionPoints(funcId));

                // Кнопка "Добавить точку"
                const addPointBtn = document.createElement('button');
                addPointBtn.className = 'add-point-btn';
                addPointBtn.textContent = 'Добавить точку';
                addPointBtn.addEventListener('click', () => addPointToFunction(funcId));

                // Кнопка "Удалить точку"
                const deletePointBtn = document.createElement('button');
                deletePointBtn.className = 'delete-point-btn';
                deletePointBtn.textContent = 'Удалить точку';
                deletePointBtn.addEventListener('click', () => deletePointFromFunction(funcId));

                // Кнопка "Удалить функцию"
                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'delete-btn';
                deleteBtn.textContent = 'Удалить';
                deleteBtn.addEventListener('click', () => deleteFunction(funcId));

                actions.appendChild(viewBtn);
                actions.appendChild(addPointBtn);
                actions.appendChild(deletePointBtn);
                actions.appendChild(deleteBtn);

                // Собираем карточку
                functionCard.appendChild(header);
                functionCard.appendChild(info);
                functionCard.appendChild(actions);

                container.appendChild(functionCard);

                // Немедленно загружаем количество точек и обновляем span
                loadPointsCount(funcId, pointsSpan);
            });
        }

        // Загрузка количества точек для функции
        async function loadPointsCount(functionId, pointsSpanElement) {
            try {
                console.log('Загружаем точки для функции ID:', functionId);

                if (functionId === undefined || functionId === null) {
                    console.error('Неверный ID функции:', functionId);
                    if (pointsSpanElement) {
                        pointsSpanElement.textContent = 'Ошибка ID';
                    }
                    return;
                }

                // Убедимся, что functionId - число
                const numericId = Number(functionId);
                if (isNaN(numericId)) {
                    console.error('ID функции не является числом:', functionId);
                    if (pointsSpanElement) {
                        pointsSpanElement.textContent = 'Ошибка';
                    }
                    return;
                }

                // Явная конкатенация строк для URL
                const url = contextPath + '/api/points/function?functionId=' + numericId;
                console.log('URL запроса:', url);

                const response = await fetch(url, {
                    headers: getAuthHeaders(null)
                });

                console.log('Статус ответа для точек функции', numericId, ':', response.status);

                if (response.ok) {
                    const points = await response.json();
                    console.log('Получено точек для функции', numericId, ':', points.length);

                    // Прямо обновляем переданный span элемент
                    if (pointsSpanElement) {
                        pointsSpanElement.textContent = points.length;
                    }
                } else {
                    console.error('Ошибка загрузки точек, статус:', response.status);
                    if (pointsSpanElement) {
                        pointsSpanElement.textContent = 'Ошибка';
                    }
                }
            } catch (error) {
                console.error('Ошибка при загрузке количества точек:', error);
                if (pointsSpanElement) {
                    pointsSpanElement.textContent = 'Ошибка';
                }
            }
        }

        // Просмотр точек функции
        async function viewFunctionPoints(functionId) {
            console.log('Просмотр точек функции ID:', functionId);

            try {
                // Загружаем функцию
                const funcResponse = await fetch(contextPath + '/api/functions/' + functionId, {
                    headers: getAuthHeaders(null)
                });

                if (!funcResponse.ok) throw new Error('Функция не найдена');
                const func = await funcResponse.json();

                // Загружаем точки
                const pointsResponse = await fetch(contextPath + '/api/points/function?functionId=' + functionId, {
                    headers: getAuthHeaders(null)
                });

                if (!pointsResponse.ok) throw new Error('Не удалось загрузить точки');
                const points = await pointsResponse.json();

                // Безопасное получение данных
                const funcIdValue = func.id !== undefined ? String(func.id) : 'неизвестен';
                const funcNameValue = func.name !== null && func.name !== undefined && func.name !== false && String(func.name).trim() !== ''
                    ? String(func.name).trim()
                    : 'Без названия';

                console.log('Данные для отображения точек:', {
                    funcId: funcIdValue,
                    funcName: funcNameValue,
                    pointsCount: points.length
                });

                // Показываем модальное окно для просмотра точек
                document.getElementById('modalTitle').textContent = 'Точки функции: ' + funcNameValue;

                let pointsTable = '';
                if (points.length > 0) {
                    // Генерируем строки таблицы
                    let rows = '';
                    for (let i = 0; i < points.length; i++) {
                        const point = points[i];
                        const pointId = point.id || i;
                        const x = point.xValue || point.x || 0;
                        const y = point.yValue || point.y || 0;

                        // Простое строковое сложение для каждой строки
                        rows +=
                            '<tr>' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + x + '</td>' +
                            '<td>' + y + '</td>' +
                            '</tr>';
                    }

                    // Создаем таблицу с простым строковым сложением
                    pointsTable =
                        '<div class="points-table">' +
                        '<h4>Точки функции (' + points.length + '):</h4>' +
                        '<table>' +
                        '<thead>' +
                        '<tr>' +
                        '<th>№</th>' +
                        '<th>X</th>' +
                        '<th>Y</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>' +
                        rows +
                        '</tbody>' +
                        '</table>' +
                        '</div>';
                } else {
                    pointsTable = '<p>У этой функции нет точек.</p>';
                }

                // Создаем все содержимое модального окна с простым строковым сложением
                const modalContent =
                    '<div class="function-info">' +
                    '<div><strong>ID функции:</strong> ' + funcIdValue + '</div>' +
                    '<div><strong>Название:</strong> ' + funcNameValue + '</div>' +
                    '<div><strong>Всего точек:</strong> ' + points.length + '</div>' +
                    '</div>' +
                    pointsTable +
                    '<div class="form-actions" style="margin-top: 20px;">' +
                    '<button onclick="closeEditModal()" style="background-color: #757575; color: white;">Закрыть</button>' +
                    '</div>';

                document.getElementById('modalContent').innerHTML = modalContent;

                document.getElementById('editModal').style.display = 'flex';

            } catch (error) {
                showError('Ошибка', 'Не удалось загрузить данные функции: ' + error.message);
            }
        }

        // Добавление новой точки к функции
        async function addPointToFunction(functionId) {
            console.log('Добавление точки к функции ID:', functionId);

            try {
                // Загружаем функцию для отображения названия
                const funcResponse = await fetch(contextPath + '/api/functions/' + functionId, {
                    headers: getAuthHeaders(null)
                });

                if (!funcResponse.ok) throw new Error('Функция не найдена');
                const func = await funcResponse.json();

                // Загружаем существующие точки для проверки уникальности X
                const pointsResponse = await fetch(contextPath + '/api/points/function?functionId=' + functionId, {
                    headers: getAuthHeaders(null)
                });

                const existingPoints = pointsResponse.ok ? await pointsResponse.json() : [];
                const existingPointsCount = existingPoints.length;

                // ПРОВЕРКА НА МАКСИМАЛЬНОЕ КОЛИЧЕСТВО ТОЧЕК
                if (existingPointsCount >= MAX_POINTS_PER_FUNCTION) {
                    showError('Ошибка', 'Достигнуто максимальное количество точек (' + MAX_POINTS_PER_FUNCTION + ') для этой функции');
                    return;
                }

                // Безопасное получение данных
                const funcIdValue = func.id !== undefined ? String(func.id) : 'неизвестен';
                const funcNameValue = func.name !== null && func.name !== undefined && func.name !== false && String(func.name).trim() !== ''
                    ? String(func.name).trim()
                    : 'Без названия';

                console.log('Данные для отображения:', {
                    funcId: funcIdValue,
                    funcName: funcNameValue,
                    pointsCount: existingPointsCount,
                    maxPoints: MAX_POINTS_PER_FUNCTION
                });

                // Показываем модальное окно для добавления точки
                document.getElementById('modalTitle').textContent = 'Добавить точку к функции: ' + funcNameValue;

                document.getElementById('modalContent').innerHTML =
                    '<div class="function-info">' +
                    '<div><strong>ID функции:</strong> ' + funcIdValue + '</div>' +
                    '<div><strong>Название:</strong> ' + funcNameValue + '</div>' +
                    '<div><strong>Существующих точек:</strong> ' + existingPointsCount + ' / ' + MAX_POINTS_PER_FUNCTION + '</div>' +
                    '</div>' +
                    '<div class="add-point-form" style="margin-top: 20px;">' +
                    '<h4>Введите данные новой точки:</h4>' +
                    '<div class="form-group">' +
                    '<label for="newPointX">Значение X:</label>' +
                    '<input type="number" id="newPointX" step="any" placeholder="Введите X" required>' +
                    '</div>' +
                    '<div class="form-group">' +
                    '<label for="newPointY">Значение Y:</label>' +
                    '<input type="number" id="newPointY" step="any" placeholder="Введите Y" required>' +
                    '</div>' +
                    '<div id="pointError" class="error-message" style="display: none; margin-bottom: 15px;"></div>' +
                    '<div class="form-actions">' +
                    '<button onclick="submitNewPoint()" class="add-point-btn">Добавить точку</button>' +
                    '<button onclick="closeEditModal()" style="background-color: #757575; color: white;">Отмена</button>' +
                    '</div>' +
                    '</div>';

                // Сохраняем functionId в глобальную переменную
                currentFunctionId = functionId;

                document.getElementById('editModal').style.display = 'flex';

            } catch (error) {
                showError('Ошибка', 'Не удалось загрузить данные функции: ' + error.message);
            }
        }

        // Отправка новой точки
        async function submitNewPoint() {
            console.log('submitNewPoint вызвана, currentFunctionId:', currentFunctionId);

            const xInput = document.getElementById('newPointX');
            const yInput = document.getElementById('newPointY');
            const errorElement = document.getElementById('pointError');

            // Сбрасываем ошибку
            if (errorElement) {
                errorElement.style.display = 'none';
                errorElement.textContent = '';
            }

            // Проверяем, что currentFunctionId определен
            if (!currentFunctionId && currentFunctionId !== 0) {
                console.error('currentFunctionId не определен:', currentFunctionId);
                if (errorElement) {
                    errorElement.textContent = 'Ошибка: ID функции не определен';
                    errorElement.style.display = 'block';
                }
                return;
            }

            const x = parseFloat(xInput.value);
            const y = parseFloat(yInput.value);

            // Валидация
            if (isNaN(x) || isNaN(y)) {
                if (errorElement) {
                    errorElement.textContent = 'Введите корректные числовые значения для X и Y';
                    errorElement.style.display = 'block';
                }
                return;
            }

            try {
                // Преобразуем functionId в число для URL
                const numericFunctionId = Number(currentFunctionId);
                if (isNaN(numericFunctionId)) {
                    throw new Error('Некорректный ID функции: ' + currentFunctionId);
                }

                console.log('Проверяем существующие точки для функции ID:', numericFunctionId);

                // Проверяем, существует ли точка с таким X
                const checkUrl = contextPath + '/api/points/function?functionId=' + numericFunctionId;
                console.log('URL для проверки:', checkUrl);

                const pointsResponse = await fetch(checkUrl, {
                    headers: getAuthHeaders(null)
                });

                if (pointsResponse.ok) {
                    const existingPoints = await pointsResponse.json();
                    console.log('Существующие точки:', existingPoints);

                    // Проверка максимального количества точек
                    if (existingPoints.length >= MAX_POINTS_PER_FUNCTION) {
                        if (errorElement) {
                            errorElement.textContent = 'Достигнуто максимальное количество точек (' + MAX_POINTS_PER_FUNCTION + ') для этой функции';
                            errorElement.style.display = 'block';
                        }
                        return;
                    }

                    // Проверка уникальности X
                    const duplicatePoint = existingPoints.find(point => {
                        const pointX = point.xValue || point.x;
                        return pointX === x;
                    });

                    if (duplicatePoint) {
                        if (errorElement) {
                            errorElement.textContent = 'Точка с X = ' + x + ' уже существует для этой функции';
                            errorElement.style.display = 'block';
                        }
                        return;
                    }
                }

                // Добавляем новую точку
                const url = contextPath + '/api/points';
                console.log('URL для добавления точки:', url);
                console.log('Данные для отправки:', {
                    functionId: numericFunctionId,
                    xValue: x,
                    yValue: y
                });

                const response = await fetch(url, {
                    method: 'POST',
                    headers: getAuthHeaders(),
                    body: JSON.stringify({
                        functionId: numericFunctionId,
                        xValue: x,
                        yValue: y
                    })
                });

                console.log('Ответ сервера:', response.status, response.statusText);

                if (response.ok) {
                    const successMessage = 'Точка (' + x + ', ' + y + ') успешно добавлена';
                    console.log(successMessage);
                    showSuccess(successMessage);

                    // Сохраняем currentFunctionId в локальную переменную
                    const functionIdToUpdate = currentFunctionId;
                    console.log('functionId для обновления счетчика:', functionIdToUpdate);

                    closeEditModal();

                    // Обновляем количество точек на карточке
                    if (functionIdToUpdate) {
                        await updatePointsCountOnCard(functionIdToUpdate);
                    } else {
                        console.error('functionIdToUpdate не установлен при добавлении точки!');
                        loadUserFunctions();
                    }
                } else {
                    let errorMessage = 'Не удалось добавить точку';
                    try {
                        const errorData = await response.json();
                        errorMessage = errorData.message || errorMessage;
                    } catch (e) {
                        errorMessage = errorMessage + ' (статус: ' + response.status + ')';
                    }
                    throw new Error(errorMessage);
                }
            } catch (error) {
                console.error('Ошибка при добавлении точки:', error);
                if (errorElement) {
                    errorElement.textContent = 'Ошибка: ' + error.message;
                    errorElement.style.display = 'block';
                }
            }
        }

        // Удаление точки из функции
        async function deletePointFromFunction(functionId) {
            console.log('Удаление точки из функции ID:', functionId);

            // Сохраняем functionId в глобальную переменную СРАЗУ
            currentFunctionId = functionId;
            console.log('currentFunctionId установлен в:', currentFunctionId);

            try {
                // Загружаем функцию
                const funcResponse = await fetch(contextPath + '/api/functions/' + functionId, {
                    headers: getAuthHeaders(null)
                });

                if (!funcResponse.ok) throw new Error('Функция не найдена');
                const func = await funcResponse.json();

                // Загружаем точки
                const pointsResponse = await fetch(contextPath + '/api/points/function?functionId=' + functionId, {
                    headers: getAuthHeaders(null)
                });

                if (!pointsResponse.ok) throw new Error('Не удалось загрузить точки');
                const points = await pointsResponse.json();

                // Безопасное получение данных
                const funcIdValue = func.id !== undefined ? String(func.id) : 'неизвестен';
                const funcNameValue = func.name !== null && func.name !== undefined && func.name !== false && String(func.name).trim() !== ''
                    ? String(func.name).trim()
                    : 'Без названия';

                let pointsList = '';
                if (points.length > 0) {
                    // Создаем список точек для выбора
                    let options = '';
                    for (let i = 0; i < points.length; i++) {
                        const point = points[i];
                        const pointId = point.id || i;
                        const x = point.xValue || point.x || 0;
                        const y = point.yValue || point.y || 0;

                        options +=
                            '<option value="' + pointId + '">' +
                            'Точка ' + (i + 1) + ': X=' + x + ', Y=' + y +
                            '</option>';
                    }

                    pointsList =
                        '<div class="form-group" style="margin-top: 15px;">' +
                        '<label for="pointToDelete"><strong>Выберите точку для удаления:</strong></label>' +
                        '<select id="pointToDelete" style="width: 100%; padding: 8px; margin-top: 5px;">' +
                        options +
                        '</select>' +
                        '</div>' +
                        '<div id="deletePointError" class="error-message" style="display: none; margin: 15px 0;"></div>';
                } else {
                    pointsList = '<p>У этой функции нет точек для удаления.</p>';
                }

                // Показываем модальное окно для удаления точки
                document.getElementById('modalTitle').textContent = 'Удаление точки из функции: ' + funcNameValue;

                let modalContent =
                    '<div class="function-info">' +
                    '<div><strong>ID функции:</strong> ' + funcIdValue + '</div>' +
                    '<div><strong>Название:</strong> ' + funcNameValue + '</div>' +
                    '<div><strong>Доступно точек:</strong> ' + points.length + '</div>' +
                    '</div>' +
                    pointsList;

                // Добавляем кнопки действий только если есть точки
                if (points.length > 0) {
                    modalContent +=
                        '<div class="form-actions" style="margin-top: 20px;">' +
                        '<button onclick="confirmDeleteSelectedPoint()" class="delete-btn">Удалить выбранную точку</button>' +
                        '<button onclick="closeEditModal()" style="background-color: #757575; color: white;">Отмена</button>' +
                        '</div>';
                } else {
                    modalContent +=
                        '<div class="form-actions" style="margin-top: 20px;">' +
                        '<button onclick="closeEditModal()" style="background-color: #757575; color: white;">Закрыть</button>' +
                        '</div>';
                }

                document.getElementById('modalContent').innerHTML = modalContent;

                document.getElementById('editModal').style.display = 'flex';

            } catch (error) {
                showError('Ошибка', 'Не удалось загрузить данные функции: ' + error.message);
            }
        }

        // Подтверждение удаления выбранной точки
        async function confirmDeleteSelectedPoint() {
            console.log('=== confirmDeleteSelectedPoint вызвана ===');
            console.log('currentFunctionId перед удалением:', currentFunctionId);

            const pointSelect = document.getElementById('pointToDelete');
            const errorElement = document.getElementById('deletePointError');

            if (!pointSelect) {
                console.error('Элемент pointSelect не найден');
                showError('Ошибка', 'Элемент выбора точки не найден');
                return;
            }

            const selectedPointId = pointSelect.value;
            console.log('Выбранный ID точки для удаления:', selectedPointId);

            if (!selectedPointId) {
                if (errorElement) {
                    errorElement.textContent = 'Выберите точку для удаления';
                    errorElement.style.display = 'block';
                }
                return;
            }

            if (!confirm('Вы уверены, что хотите удалить выбранную точку?')) {
                return;
            }

            try {
                const url = contextPath + '/api/points/' + selectedPointId;
                console.log('URL для удаления точки:', url);

                const response = await fetch(url, {
                    method: 'DELETE',
                    headers: getAuthHeaders(null)
                });

                console.log('Статус ответа при удалении:', response.status);

                if (response.ok) {
                    showSuccess('Точка успешно удалена');

                    // СОХРАНЯЕМ currentFunctionId В ЛОКАЛЬНУЮ ПЕРЕМЕННУЮ,
                    // прежде чем закроем модальное окно
                    const functionIdToUpdate = currentFunctionId;
                    console.log('Сохраненный functionId для обновления:', functionIdToUpdate);

                    // Закрываем модальное окно
                    closeEditModal();

                    console.log('Вызываем updatePointsCountOnCard с functionId:', functionIdToUpdate);

                    // Обновляем количество точек на карточке
                    if (functionIdToUpdate) {
                        await updatePointsCountOnCard(functionIdToUpdate);
                        console.log('updatePointsCountOnCard завершена');
                    } else {
                        console.error('functionIdToUpdate не установлен!');
                        // На всякий случай перезагружаем все функции
                        loadUserFunctions();
                    }
                } else {
                    throw new Error('Не удалось удалить точку');
                }
            } catch (error) {
                console.error('Ошибка при удалении точки:', error);
                if (errorElement) {
                    errorElement.textContent = 'Ошибка: ' + error.message;
                    errorElement.style.display = 'block';
                }
            }
        }

        // Обновление количества точек на карточке
        async function updatePointsCountOnCard(functionId) {
            try {
                console.log('=== updatePointsCountOnCard вызвана ===');
                console.log('Полученный functionId:', functionId);

                if (!functionId && functionId !== 0) {
                    console.error('functionId не определен:', functionId);
                    // Попробуем использовать глобальную переменную как запасной вариант
                    console.log('Пробуем использовать currentFunctionId:', currentFunctionId);
                    if (currentFunctionId) {
                        functionId = currentFunctionId;
                        console.log('Используем currentFunctionId:', functionId);
                    } else {
                        console.error('Нет functionId для обновления!');
                        return;
                    }
                }

                const numericId = Number(functionId);
                if (isNaN(numericId)) {
                    console.error('functionId не является числом:', functionId);
                    return;
                }

                const url = contextPath + '/api/points/function?functionId=' + numericId;
                console.log('URL для получения точек:', url);

                const response = await fetch(url, {
                    headers: getAuthHeaders(null)
                });

                if (response.ok) {
                    const points = await response.json();
                    const pointsCount = points.length;
                    console.log('Новое количество точек:', pointsCount);

                    // Находим ВСЕ карточки и проверяем каждую
                    const allCards = document.querySelectorAll('.function-card');
                    console.log('Всего карточек:', allCards.length);

                    let foundCard = false;

                    for (const card of allCards) {
                        const idElement = card.querySelector('.function-id');
                        if (idElement) {
                            const idText = idElement.textContent;
                            console.log('Текст ID элемента:', idText);

                            // Проверяем содержит ли текст наш functionId
                            if (idText.includes(functionId)) {
                                console.log('Найдена карточка с functionId:', functionId);
                                const pointsElement = card.querySelector('.points-count');
                                if (pointsElement) {
                                    console.log('Обновляем счетчик:', pointsElement.textContent, '->', pointsCount);
                                    pointsElement.textContent = pointsCount;
                                    foundCard = true;
                                }
                                break; // Нашли нужную карточку, выходим из цикла
                            }
                        }
                    }

                    if (!foundCard) {
                        console.warn('Карточка с functionId', functionId, 'не найдена!');
                        console.log('Все тексты ID:', Array.from(allCards).map(card => {
                            const idEl = card.querySelector('.function-id');
                            return idEl ? idEl.textContent : 'нет ID';
                        }));
                    }
                } else {
                    console.error('Ошибка при получении точек:', response.status);
                }
            } catch (error) {
                console.error('Ошибка обновления счетчика точек:', error);
            }
        }

        // Удаление функции
        function deleteFunction(functionId) {
            currentFunctionId = functionId;

            console.log('Удаление функции ID:', functionId);

            // Находим функцию для отображения названия
            const func = currentFunctions.find(f => f.id == functionId);

            let funcName = 'эту функцию';
            let displayId = functionId;

            if (func) {
                console.log('Найдена функция для удаления:', func);

                // Безопасное получение имени функции - ПРОСТОЕ СТРОКОВОЕ ПРЕОБРАЗОВАНИЕ
                if (func.name !== null && func.name !== undefined && func.name !== false) {
                    const nameStr = String(func.name);
                    if (nameStr.trim() !== '') {
                        funcName = nameStr.trim();
                    }
                }

                // Убедимся, что ID тоже строка
                if (func.id !== null && func.id !== undefined) {
                    displayId = String(func.id);
                }
            }

            // ПРОСТОЕ СТРОКОВОЕ СЛОЖЕНИЕ как в рендере
            const confirmText = 'Вы уверены, что хотите удалить функцию "' + funcName + '" (ID: ' + displayId + ')? Это действие нельзя отменить.';

            console.log('Текст подтверждения:', confirmText);

            // Прямое установление текста
            const deleteConfirmElement = document.getElementById('deleteConfirmText');
            if (deleteConfirmElement) {
                deleteConfirmElement.textContent = confirmText;
                console.log('Элемент найден, текст установлен');
            } else {
                console.error('Элемент deleteConfirmText не найден!');
            }

            document.getElementById('confirmDeleteModal').style.display = 'flex';
        }

        // Подтверждение удаления функции
        async function confirmDeleteFunction() {
            console.log('confirmDeleteFunction вызвана');
            console.log('currentFunctionId:', currentFunctionId);

            if (!currentFunctionId) {
                showError('Ошибка', 'Не выбрана функция для удаления');
                closeConfirmDeleteModal();
                return;
            }

            try {
                // Преобразуем ID в число на всякий случай
                const numericId = Number(currentFunctionId);

                const url = contextPath + '/api/functions/' + numericId;
                console.log('URL для удаления:', url);

                const response = await fetch(url, {
                    method: 'DELETE',
                    headers: getAuthHeaders(null)
                });

                console.log('Ответ сервера:', response.status, response.statusText);

                if (response.ok) {
                    showSuccess('Функция успешно удалена');
                    // Закрываем модальное окно
                    closeConfirmDeleteModal();
                    // Обновляем список функций
                    loadUserFunctions();
                } else {
                    let errorMessage = 'Не удалось удалить функцию';
                    try {
                        const errorData = await response.json();
                        errorMessage = errorData.message || errorMessage;
                    } catch (e) {
                        errorMessage = 'Ошибка ' + response.status + ': ' + response.statusText;
                    }
                    throw new Error(errorMessage);
                }
            } catch (error) {
                console.error('Ошибка при удалении функции:', error);
                showError('Ошибка удаления', error.message);
                closeConfirmDeleteModal();
            }
        }

        // Закрытие модальных окон
        function closeEditModal() {
            document.getElementById('editModal').style.display = 'none';
            currentFunctionId = null;
        }

        function closeConfirmDeleteModal() {
            document.getElementById('confirmDeleteModal').style.display = 'none';
            currentFunctionId = null;
        }

        // Вспомогательные функции для показа сообщений
        function showSuccess(message) {
            const successEl = document.getElementById('successMessage');
            successEl.textContent = message;
            successEl.style.display = 'block';

            setTimeout(() => {
                successEl.style.display = 'none';
            }, 3000);
        }

        function showError(title, message) {
            const errorEl = document.getElementById('errorMessage');
            errorEl.innerHTML = '<strong>' + title + ':</strong> ' + message;
            errorEl.style.display = 'block';

            setTimeout(() => {
                errorEl.style.display = 'none';
            }, 5000);
        }
    </script>
</body>
</html>