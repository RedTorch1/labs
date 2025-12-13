<%-- src/main/webapp/WEB-INF/ui/operations.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Операции с функциями</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f2f5;
        }

        .container {
            max-width: 1400px;
            margin: 0 auto;
        }

        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 15px;
            border-bottom: 2px solid #ddd;
        }

        .back-btn {
            padding: 8px 16px;
            background-color: #757575;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .back-btn:hover {
            background-color: #616161;
        }

        .operations-container {
            display: grid;
            grid-template-columns: 1fr auto 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }

        .function-panel {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .function-panel h3 {
            margin-top: 0;
            color: #333;
            text-align: center;
        }

        .controls {
            margin-bottom: 15px;
            text-align: center;
        }

        .controls button {
            margin: 0 5px;
            padding: 8px 16px;
            background-color: #2196F3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .controls button:hover {
            background-color: #1976D2;
        }

        .function-table {
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
            padding: 8px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
            position: sticky;
            top: 0;
        }

        .x-column {
            background-color: #f9f9f9;
            font-weight: bold;
        }

        .editable {
            background-color: #fff;
        }

        .operations-panel {
            display: flex;
            flex-direction: column;
            justify-content: center;
            gap: 10px;
        }

        .operation-btn {
            padding: 12px 24px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        .operation-btn:hover {
            background-color: #45a049;
        }

        .result-panel {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            grid-column: 1 / -1;
        }

        .result-panel h3 {
            margin-top: 0;
            color: #333;
            text-align: center;
        }

        .result-controls {
            margin-bottom: 15px;
            text-align: center;
        }

        .no-data {
            text-align: center;
            color: #999;
            padding: 20px;
            font-style: italic;
        }

        /* Модальные окна */
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
            border-radius: 10px;
            max-width: 400px;
            width: 90%;
            box-shadow: 0 8px 30px rgba(0,0,0,0.3);
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

        .modal-buttons {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        .modal-buttons button {
            flex: 1;
            padding: 10px;
        }

        .btn-primary {
            background-color: #2196F3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-primary:hover {
            background-color: #1976D2;
        }

        .btn-success {
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-success:hover {
            background-color: #45a049;
        }

        /* Стили для сообщений */
        .message {
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            display: none;
        }

        .success-message {
            background-color: #dff0d8;
            color: #3c763d;
            border: 1px solid #d6e9c6;
        }

        .error-message {
            background-color: #f2dede;
            color: #a94442;
            border: 1px solid #ebccd1;
        }

        /* Стили для загрузки */
        .loading {
            display: none;
            text-align: center;
            padding: 10px;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Операции с табулированными функциями</h1>
            <a href="${pageContext.request.contextPath}/ui/main" class="back-btn">На главную</a>
        </header>

        <!-- Сообщения -->
        <div id="successMessage" class="message success-message"></div>
        <div id="errorMessage" class="message error-message"></div>

        <div class="operations-container">
            <!-- Функция 1 -->
            <div class="function-panel">
                <h3>Функция 1</h3>
                <div class="controls">
                    <button onclick="createFunction(1)">Создать</button>
                    <button onclick="loadFunction(1)">Загрузить</button>
                    <button onclick="saveFunction(1)">Сохранить</button>
                </div>
                <div id="function1Loading" class="loading">Загрузка...</div>
                <div id="function1Table" class="function-table">
                    <div class="no-data">Функция не загружена</div>
                </div>
            </div>

            <!-- Операции -->
            <div class="operations-panel">
                <button class="operation-btn" onclick="performOperation('add')">➕ Сложить</button>
                <button class="operation-btn" onclick="performOperation('subtract')">➖ Вычесть</button>
                <button class="operation-btn" onclick="performOperation('multiply')">✖ Умножить</button>
                <button class="operation-btn" onclick="performOperation('divide')">➗ Разделить</button>
            </div>

            <!-- Функция 2 -->
            <div class="function-panel">
                <h3>Функция 2</h3>
                <div class="controls">
                    <button onclick="createFunction(2)">Создать</button>
                    <button onclick="loadFunction(2)">Загрузить</button>
                    <button onclick="saveFunction(2)">Сохранить</button>
                </div>
                <div id="function2Loading" class="loading">Загрузка...</div>
                <div id="function2Table" class="function-table">
                    <div class="no-data">Функция не загружена</div>
                </div>
            </div>
        </div>

        <!-- Результат -->
        <div class="result-panel">
            <h3>Результат</h3>
            <div class="result-controls">
                <button onclick="saveResult()">Сохранить результат</button>
            </div>
            <div id="resultLoading" class="loading">Выполнение операции...</div>
            <div id="resultTable" class="function-table">
                <div class="no-data">Результат операции отобразится здесь</div>
            </div>
        </div>
    </div>

    <!-- Модальное окно выбора типа создания -->
    <div id="createModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Создание функции</h3>
                <button class="close-btn" onclick="closeCreateModal()">×</button>
            </div>
            <p>Выберите способ создания функции:</p>
            <div class="modal-buttons">
                <button class="btn-primary" onclick="createFromArrays()">
                    Из массивов
                </button>
                <button class="btn-success" onclick="createFromFunction()">
                    Из математической функции
                </button>
            </div>
        </div>
    </div>

    <script>
        // ПРОВЕРКА АВТОРИЗАЦИИ
        if (localStorage.getItem('isAuthenticated') !== 'true') {
            alert('Пожалуйста, войдите в систему');
            window.location.href = '${pageContext.request.contextPath}/ui/';
        }

        // Устанавливаем имя пользователя если есть элемент
        const username = localStorage.getItem('username');
        const userElement = document.getElementById('currentUser');
        if (username && userElement) {
            userElement.textContent = username;
        }
        const contextPath = '<%= request.getContextPath() %>';
        let currentFunctionPanel = 1;
        let function1Data = null;
        let function2Data = null;
        let resultData = null;
        let activeChildWindows = {};

        // ========== ОСНОВНЫЕ ФУНКЦИИ ==========

        function createFunction(panelNumber) {
            currentFunctionPanel = panelNumber;
            document.getElementById('createModal').style.display = 'flex';
        }

        function closeCreateModal() {
            document.getElementById('createModal').style.display = 'none';
        }

        function createFromArrays() {
            closeCreateModal();
            // Открываем окно создания из массивов
            const url = contextPath + '/ui/functions/create-from-arrays?returnTo=operations&panel=' + currentFunctionPanel;
            openChildWindow(url, 'createWindow' + currentFunctionPanel);
        }

        function createFromFunction() {
            closeCreateModal();
            // Открываем окно создания из функции
            const url = contextPath + '/ui/functions/create-from-function?returnTo=operations&panel=' + currentFunctionPanel;
            openChildWindow(url, 'createWindow' + currentFunctionPanel);
        }

        function openChildWindow(url, windowName) {
            // Закрываем предыдущее окно с таким же именем, если оно открыто
            if (activeChildWindows[windowName] && !activeChildWindows[windowName].closed) {
                activeChildWindows[windowName].close();
            }

            // Открываем новое окно
            const childWindow = window.open(
                url,
                windowName,
                'width=900,height=700,resizable=yes,scrollbars=yes,location=no,menubar=no,toolbar=no'
            );

            // Сохраняем ссылку на окно
            activeChildWindows[windowName] = childWindow;

            // Начинаем отслеживать закрытие окна
            if (childWindow) {
                trackChildWindow(childWindow, windowName);
            }
        }

        function trackChildWindow(childWindow, windowName) {
            const checkInterval = setInterval(() => {
                if (childWindow.closed) {
                    clearInterval(checkInterval);
                    delete activeChildWindows[windowName];

                    // Проверяем, были ли сохранены данные
                    checkForSavedData();
                }
            }, 100);
        }

        function checkForSavedData() {
            const savedData = localStorage.getItem('createdFunctionData');
            if (savedData) {
                try {
                    const data = JSON.parse(savedData);
                    const returnTo = data.returnTo;
                    const panelNumber = data.panel || currentFunctionPanel;

                    // Проверяем, что данные предназначены для operations
                    if (returnTo === 'operations') {
                        setFunctionData(parseInt(panelNumber), data);
                        showMessage('Функция успешно создана и загружена!', 'success');
                    }

                    // Очищаем localStorage в любом случае
                    localStorage.removeItem('createdFunctionData');
                } catch (error) {
                    showMessage('Ошибка обработки данных функции: ' + error.message, 'error');
                }
            }
        }

        // Универсальный обработчик данных
        window.handleFunctionData = function(data) {
            console.log('Получены данные через handleFunctionData:', data);

            if (data.returnTo === 'operations') {
                const panelNumber = data.panel || 1;
                setFunctionData(parseInt(panelNumber), data);
                showMessage('Функция успешно создана и загружена!', 'success');
            }
        };

        // Функция для получения данных из дочернего окна (для обратной совместимости)
        window.receiveFunctionData = function(panelNumber, data) {
            console.log('Получены данные для панели', panelNumber, ':', data);
            setFunctionData(panelNumber, data);
            showMessage('Функция успешно создана и загружена!', 'success');
        };

        function loadFunction(panelNumber) {
            showLoading(panelNumber, true);

            // Создаем input для выбора файла
            const input = document.createElement('input');
            input.type = 'file';
            input.accept = '.dat,.txt,.json';
            input.onchange = function(event) {
                const file = event.target.files[0];
                if (file) {
                    // Создаем FormData для отправки файла
                    const formData = new FormData();
                    formData.append('file', file);
                    formData.append('returnTo', 'operations');
                    formData.append('panel', panelNumber.toString());

                    // Отправляем файл на сервер
                    fetch(contextPath + '/ui/functions/upload', {
                        method: 'POST',
                        body: formData
                    })
                    .then(response => {
                        showLoading(panelNumber, false);

                        if (!response.ok) {
                            return response.json().then(error => {
                                throw new Error(error.details || error.error || 'Ошибка загрузки');
                            });
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.success) {
                            // Обрабатываем данные функции
                            setFunctionData(panelNumber, data);
                            showMessage(data.message, 'success');
                        } else {
                            throw new Error(data.error || 'Ошибка загрузки файла');
                        }
                    })
                    .catch(error => {
                        showMessage('Ошибка загрузки файла: ' + error.message, 'error');
                    });
                } else {
                    showLoading(panelNumber, false);
                }
            };
            input.click();
        }

        function saveFunction(panelNumber) {
            const functionData = panelNumber === 1 ? function1Data : function2Data;
            if (!functionData) {
                showMessage('Нет данных для сохранения', 'error');
                return;
            }

            // ПРОСТОЙ ВАРИАНТ - без выбора формата
            const defaultName = 'function_' + panelNumber;
            const fileName = prompt('Введите имя файла:', defaultName);

            if (!fileName) return;

            // Подготавливаем данные
            const xValuesStr = JSON.stringify(functionData.xValues);
            const yValuesStr = JSON.stringify(functionData.yValues);

            // Создаем URL для скачивания (бинарный формат по умолчанию)
            const url = contextPath + '/ui/functions/download?' +
                'xValues=' + encodeURIComponent(xValuesStr) +
                '&yValues=' + encodeURIComponent(yValuesStr) +
                '&format=binary' +
                '&fileName=' + encodeURIComponent(fileName);

            // Инициируем скачивание
            const link = document.createElement('a');
            link.href = url;
            link.download = fileName + '.dat';
            link.style.display = 'none';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            showMessage('Функция успешно сохранена!', 'success');
        }

        function saveResult() {
            if (!resultData) {
                showMessage('Нет результата для сохранения', 'error');
                return;
            }

            // ПРОСТОЙ ВАРИАНТ - без выбора формата
            const operationName = getOperationName(resultData.operation);
            const defaultName = 'result_' + operationName;
            const fileName = prompt('Введите имя файла:', defaultName);

            if (!fileName) return;

            const xValuesStr = JSON.stringify(resultData.xValues);
            const yValuesStr = JSON.stringify(resultData.yValues);

            const url = contextPath + '/ui/functions/download?' +
                'xValues=' + encodeURIComponent(xValuesStr) +
                '&yValues=' + encodeURIComponent(yValuesStr) +
                '&format=binary' +
                '&fileName=' + encodeURIComponent(fileName);

            const link = document.createElement('a');
            link.href = url;
            link.download = fileName + '.dat';
            link.style.display = 'none';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            showMessage('Результат успешно сохранен!', 'success');
        }

        function getOperationName(operation) {
            switch(operation) {
                case 'add': return 'addition';
                case 'subtract': return 'subtraction';
                case 'multiply': return 'multiplication';
                case 'divide': return 'division';
                default: return 'result';
            }
        }

        function setFunctionData(panelNumber, data) {
            if (panelNumber === 1) {
                function1Data = data;
                renderFunctionTable('function1Table', data, true);
            } else {
                function2Data = data;
                renderFunctionTable('function2Table', data, true);
            }
            showLoading(panelNumber, false);
        }

        function renderFunctionTable(containerId, data, editable) {
            const container = document.getElementById(containerId);

            if (!data || !data.xValues || !data.yValues || data.xValues.length === 0) {
                container.innerHTML = '<div class="no-data">Функция не загружена</div>';
                return;
            }

            let tableHTML = '<table>';
            tableHTML += '<thead><tr><th>№</th><th>X</th><th>Y</th></tr></thead>';
            tableHTML += '<tbody>';

            for (let i = 0; i < data.xValues.length; i++) {
                tableHTML += '<tr>';
                tableHTML += '<td>' + (i + 1) + '</td>';
                tableHTML += '<td class="x-column">' + formatNumber(data.xValues[i]) + '</td>';

                if (editable) {
                    tableHTML += '<td class="editable">';
                    tableHTML += '<input type="number" value="' + formatNumber(data.yValues[i]) + '" ';
                    tableHTML += 'onchange="updateYValue(\'' + containerId + '\', ' + i + ', this.value)" ';
                    tableHTML += 'step="any" style="width: 100%; border: none; text-align: center;">';
                    tableHTML += '</td>';
                } else {
                    tableHTML += '<td>' + formatNumber(data.yValues[i]) + '</td>';
                }

                tableHTML += '</tr>';
            }

            tableHTML += '</tbody></table>';
            container.innerHTML = tableHTML;
        }

        function formatNumber(num) {
            if (typeof num !== 'number') return num;
            // Форматируем число: оставляем 6 знаков после запятой
            return parseFloat(num.toFixed(6));
        }

        function updateYValue(containerId, index, value) {
            const panelNumber = containerId === 'function1Table' ? 1 : 2;
            const functionData = panelNumber === 1 ? function1Data : function2Data;

            if (functionData && functionData.yValues) {
                functionData.yValues[index] = parseFloat(value);
            }
        }

        function performOperation(operation) {
            if (!function1Data || !function2Data) {
                showMessage('Загрузите обе функции для выполнения операции', 'error');
                return;
            }

            // Показываем загрузку
            document.getElementById('resultLoading').style.display = 'block';
            document.getElementById('resultTable').innerHTML = '';

            // Проверяем совпадение X значений
            const x1 = function1Data.xValues;
            const x2 = function2Data.xValues;

            if (x1.length !== x2.length) {
                showMessage('Функции должны иметь одинаковое количество точек', 'error');
                document.getElementById('resultLoading').style.display = 'none';
                return;
            }

            // Проверяем совпадение X значений с допустимой погрешностью
            for (let i = 0; i < x1.length; i++) {
                if (Math.abs(x1[i] - x2[i]) > 0.000001) {
                    showMessage('X значения функций должны совпадать. Ошибка в точке ' + (i + 1), 'error');
                    document.getElementById('resultLoading').style.display = 'none';
                    return;
                }
            }

            // Выполняем операцию
            const y1 = function1Data.yValues;
            const y2 = function2Data.yValues;
            const resultY = new Array(x1.length);

            try {
                for (let i = 0; i < x1.length; i++) {
                    switch (operation) {
                        case 'add':
                            resultY[i] = y1[i] + y2[i];
                            break;
                        case 'subtract':
                            resultY[i] = y1[i] - y2[i];
                            break;
                        case 'multiply':
                            resultY[i] = y1[i] * y2[i];
                            break;
                        case 'divide':
                            if (Math.abs(y2[i]) < 0.000001) {
                                throw new Error('Деление на ноль в точке x = ' + x1[i]);
                            }
                            resultY[i] = y1[i] / y2[i];
                            break;
                    }
                }

                // Сохраняем результат
                resultData = {
                    xValues: x1.slice(),
                    yValues: resultY,
                    operation: operation
                };

                // Отображаем результат
                renderFunctionTable('resultTable', resultData, false);
                document.getElementById('resultLoading').style.display = 'none';

                showMessage('Операция выполнена успешно!', 'success');

            } catch (error) {
                document.getElementById('resultLoading').style.display = 'none';
                showMessage('Ошибка выполнения операции: ' + error.message, 'error');
            }
        }

        // ========== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ==========

        function showMessage(message, type) {
            const successEl = document.getElementById('successMessage');
            const errorEl = document.getElementById('errorMessage');

            if (type === 'success') {
                successEl.textContent = message;
                successEl.style.display = 'block';
                errorEl.style.display = 'none';

                // Автоматически скрываем через 5 секунд
                setTimeout(() => {
                    successEl.style.display = 'none';
                }, 5000);
            } else {
                errorEl.textContent = message;
                errorEl.style.display = 'block';
                successEl.style.display = 'none';

                // Автоматически скрываем через 5 секунд
                setTimeout(() => {
                    errorEl.style.display = 'none';
                }, 5000);
            }
        }

        function showLoading(panelNumber, show) {
            const loadingId = panelNumber === 1 ? 'function1Loading' :
                             panelNumber === 2 ? 'function2Loading' : 'resultLoading';
            const loadingEl = document.getElementById(loadingId);

            if (loadingEl) {
                loadingEl.style.display = show ? 'block' : 'none';
            }

            // Скрываем/показываем таблицу
            const tableId = panelNumber === 1 ? 'function1Table' :
                           panelNumber === 2 ? 'function2Table' : 'resultTable';
            const tableEl = document.getElementById(tableId);

            if (tableEl && show) {
                tableEl.style.display = 'none';
            } else if (tableEl && !show) {
                tableEl.style.display = 'block';
            }
        }

        // Проверяем URL параметры для загрузки данных при загрузке страницы
        window.onload = function() {
            const urlParams = new URLSearchParams(window.location.search);
            const loadedPanel = urlParams.get('loadedPanel');
            const loadedData = urlParams.get('loadedData');

            if (loadedPanel && loadedData) {
                try {
                    const data = JSON.parse(decodeURIComponent(loadedData));
                    setFunctionData(parseInt(loadedPanel), data);
                    showMessage('Функция загружена из URL параметров', 'success');
                } catch (error) {
                    console.error('Ошибка загрузки данных:', error);
                }
            }

            // Также проверяем localStorage на случай, если страница была обновлена
            checkForSavedData();
        };

        // Закрытие модального окна при клике вне его
        window.onclick = function(event) {
            const modal = document.getElementById('createModal');
            if (event.target === modal) {
                closeCreateModal();
            }
        };

        // Закрываем все дочерние окна при закрытии этой страницы
        window.onbeforeunload = function() {
            for (const windowName in activeChildWindows) {
                if (activeChildWindows[windowName] && !activeChildWindows[windowName].closed) {
                    activeChildWindows[windowName].close();
                }
            }
        };
    </script>
</body>
</html>