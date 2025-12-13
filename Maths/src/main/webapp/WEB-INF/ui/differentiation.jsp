<%-- src/main/webapp/WEB-INF/ui/differentiation.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Дифференцирование функции</title>
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

        .differentiation-container {
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

        .operation-panel {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            gap: 20px;
        }

        .differentiate-btn {
            padding: 15px 30px;
            background-color: #FF9800;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
        }

        .differentiate-btn:hover {
            background-color: #F57C00;
        }

        .arrow {
            font-size: 36px;
            color: #666;
        }

        .no-data {
            text-align: center;
            color: #999;
            padding: 20px;
            font-style: italic;
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
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Дифференцирование табулированной функции</h1>
            <a href="${pageContext.request.contextPath}/ui/main" class="back-btn">На главную</a>
        </header>

        <!-- Сообщения -->
        <div id="successMessage" class="message success-message"></div>
        <div id="errorMessage" class="message error-message"></div>

        <div class="differentiation-container">
            <!-- Исходная функция -->
            <div class="function-panel">
                <h3>Исходная функция</h3>
                <div class="controls">
                    <button onclick="createFunction()">Создать</button>
                    <button onclick="loadFunction()">Загрузить</button>
                    <button onclick="saveFunction()">Сохранить</button>
                </div>
                <div id="sourceLoading" class="loading">Загрузка...</div>
                <div id="sourceTable" class="function-table">
                    <div class="no-data">Функция не загружена</div>
                </div>
            </div>

            <!-- Операция -->
            <div class="operation-panel">
                <div class="arrow">→</div>
                <button class="differentiate-btn" onclick="differentiate()">
                    Дифференцировать
                </button>
                <div class="arrow">→</div>
            </div>

            <!-- Результат -->
            <div class="function-panel">
                <h3>Производная</h3>
                <div class="controls">
                    <button onclick="saveResult()">Сохранить результат</button>
                </div>
                <div id="resultLoading" class="loading">Выполнение операции...</div>
                <div id="resultTable" class="function-table">
                    <div class="no-data">Результат дифференцирования отобразится здесь</div>
                </div>
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
        let sourceFunctionData = null;
        let resultFunctionData = null;
        let activeChildWindows = {};

        // ========== ОСНОВНЫЕ ФУНКЦИИ ==========

        function createFunction() {
            document.getElementById('createModal').style.display = 'flex';
        }

        function closeCreateModal() {
            document.getElementById('createModal').style.display = 'none';
        }

        function createFromArrays() {
            closeCreateModal();
            const url = contextPath + '/ui/functions/create-from-arrays?returnTo=differentiation';
            openChildWindow(url, 'createWindow');
        }

        function createFromFunction() {
            closeCreateModal();
            const url = contextPath + '/ui/functions/create-from-function?returnTo=differentiation';
            openChildWindow(url, 'createWindow');
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
            if (childWindow) {
                activeChildWindows[windowName] = childWindow;
                trackChildWindow(childWindow, windowName);
            } else {
                showMessage('Ошибка: браузер заблокировал открытие окна. Разрешите всплывающие окна для этого сайта.', 'error');
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

                    // Проверяем, что данные предназначены для differentiation
                    if (returnTo === 'differentiation') {
                        setSourceFunctionData(data);
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

            if (data.returnTo === 'differentiation') {
                setSourceFunctionData(data);
                showMessage('Функция успешно создана и загружена!', 'success');
            }
        };

        // Функция для получения данных из дочернего окна (для обратной совместимости)
        window.receiveFunctionData = function(data) {
            console.log('Получены данные функции из дочернего окна:', data);
            setSourceFunctionData(data);
            showMessage('Функция успешно создана и загружена!', 'success');
        };

        function loadFunction() {
            showSourceLoading(true);

            const input = document.createElement('input');
            input.type = 'file';
            input.accept = '.dat,.txt,.json';
            input.onchange = function(event) {
                const file = event.target.files[0];
                if (file) {
                    const formData = new FormData();
                    formData.append('file', file);
                    formData.append('returnTo', 'differentiation');

                    fetch(contextPath + '/ui/functions/upload', {
                        method: 'POST',
                        body: formData
                    })
                    .then(response => {
                        showSourceLoading(false);

                        if (!response.ok) {
                            return response.json().then(error => {
                                throw new Error(error.details || error.error || 'Ошибка загрузки');
                            });
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.success) {
                            setSourceFunctionData(data);
                            showMessage(data.message, 'success');
                        } else {
                            throw new Error(data.error || 'Ошибка загрузки файла');
                        }
                    })
                    .catch(error => {
                        showMessage('Ошибка загрузки файла: ' + error.message, 'error');
                    });
                } else {
                    showSourceLoading(false);
                }
            };
            input.click();
        }

        function saveFunction() {
            if (!sourceFunctionData) {
                showMessage('Нет данных для сохранения', 'error');
                return;
            }

            const defaultName = 'source_function';
            const fileName = prompt('Введите имя файла:', defaultName);

            if (!fileName) {
                showMessage('Сохранение отменено', 'info');
                return;
            }

            try {
                const xValuesStr = JSON.stringify(sourceFunctionData.xValues);
                const yValuesStr = JSON.stringify(sourceFunctionData.yValues);

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

                setTimeout(() => {
                    document.body.removeChild(link);
                    showMessage('Функция успешно сохранена!', 'success');
                }, 100);

            } catch (error) {
                showMessage('Ошибка при сохранении: ' + error.message, 'error');
            }
        }

        function saveResult() {
            if (!resultFunctionData) {
                showMessage('Нет результата для сохранения', 'error');
                return;
            }

            const defaultName = 'derivative_function';
            const fileName = prompt('Введите имя файла:', defaultName);

            if (!fileName) {
                showMessage('Сохранение отменено', 'info');
                return;
            }

            try {
                const xValuesStr = JSON.stringify(resultFunctionData.xValues);
                const yValuesStr = JSON.stringify(resultFunctionData.yValues);

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

                setTimeout(() => {
                    document.body.removeChild(link);
                    showMessage('Результат успешно сохранен!', 'success');
                }, 100);

            } catch (error) {
                showMessage('Ошибка при сохранении: ' + error.message, 'error');
            }
        }

        function setSourceFunctionData(data) {
            sourceFunctionData = data;
            renderFunctionTable('sourceTable', data, true);
            showSourceLoading(false);
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
                    tableHTML += 'onchange="updateYValue(' + i + ', this.value)" ';
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
            const formatted = num.toFixed(6);
            return parseFloat(formatted).toString();
        }

        function updateYValue(index, value) {
            if (sourceFunctionData && sourceFunctionData.yValues) {
                const numValue = parseFloat(value);
                if (!isNaN(numValue)) {
                    sourceFunctionData.yValues[index] = numValue;
                }
            }
        }

        function differentiate() {
            if (!sourceFunctionData) {
                showMessage('Загрузите функцию для дифференцирования', 'error');
                return;
            }

            if (sourceFunctionData.xValues.length < 2) {
                showMessage('Для дифференцирования нужно как минимум 2 точки', 'error');
                return;
            }

            // Показываем загрузку
            document.getElementById('resultLoading').style.display = 'block';
            document.getElementById('resultTable').innerHTML = '';

            // Выполняем численное дифференцирование
            const xValues = sourceFunctionData.xValues;
            const yValues = sourceFunctionData.yValues;
            const n = xValues.length;
            const derivativeY = new Array(n);

            try {
                // Первая точка (левая разность)
                derivativeY[0] = (yValues[1] - yValues[0]) / (xValues[1] - xValues[0]);

                // Средние точки (центральная разность)
                for (let i = 1; i < n - 1; i++) {
                    derivativeY[i] = (yValues[i + 1] - yValues[i - 1]) /
                                     (xValues[i + 1] - xValues[i - 1]);
                }

                // Последняя точка (правая разность)
                derivativeY[n - 1] = (yValues[n - 1] - yValues[n - 2]) /
                                     (xValues[n - 1] - xValues[n - 2]);

                // Сохраняем результат
                resultFunctionData = {
                    xValues: xValues.slice(),
                    yValues: derivativeY,
                    operation: 'differentiation'
                };

                // Отображаем результат
                renderFunctionTable('resultTable', resultFunctionData, false);
                document.getElementById('resultLoading').style.display = 'none';

                showMessage('Дифференцирование выполнено успешно!', 'success');

            } catch (error) {
                document.getElementById('resultLoading').style.display = 'none';
                showMessage('Ошибка дифференцирования: ' + error.message, 'error');
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

                setTimeout(() => {
                    successEl.style.display = 'none';
                }, 5000);
            } else {
                errorEl.textContent = message;
                errorEl.style.display = 'block';
                successEl.style.display = 'none';

                setTimeout(() => {
                    errorEl.style.display = 'none';
                }, 5000);
            }
        }

        function showSourceLoading(show) {
            const loadingEl = document.getElementById('sourceLoading');
            const tableEl = document.getElementById('sourceTable');

            if (loadingEl) {
                loadingEl.style.display = show ? 'block' : 'none';
            }

            if (tableEl) {
                tableEl.style.display = show ? 'none' : 'block';
            }
        }

        // Проверяем URL параметры для загрузки данных при загрузке страницы
        window.onload = function() {
            console.log('Страница дифференцирования загружена');

            const urlParams = new URLSearchParams(window.location.search);
            const loadedData = urlParams.get('loadedData');

            if (loadedData) {
                try {
                    const data = JSON.parse(decodeURIComponent(loadedData));
                    setSourceFunctionData(data);
                    showMessage('Функция загружена из URL параметров', 'success');
                } catch (error) {
                    console.error('Ошибка загрузки данных из URL:', error);
                }
            }

            // Проверяем localStorage
            checkForSavedData();

            // Настраиваем обработчики закрытия модальных окон
            setupModalCloseHandlers();
        };

        function setupModalCloseHandlers() {
            // Закрытие модальных окон при клике вне их
            window.onclick = function(event) {
                const modal = document.getElementById('createModal');
                if (event.target === modal) {
                    closeCreateModal();
                }
            };

            // Закрытие модальных окон по Escape
            document.addEventListener('keydown', function(event) {
                if (event.key === 'Escape') {
                    const modal = document.getElementById('createModal');
                    if (modal && modal.style.display === 'flex') {
                        closeCreateModal();
                    }
                }
            });
        }

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