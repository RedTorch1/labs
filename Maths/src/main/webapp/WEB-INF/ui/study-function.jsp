<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Изучение табулированной функции</title>
    <meta charset="UTF-8">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.js"></script>
    <style>
        /* ВСЕ СТИЛИ ОСТАЮТСЯ ТЕ ЖЕ - как в предыдущей версии */
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f0f2f5; }
        .container { max-width: 1400px; margin: 0 auto; }
        header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; padding-bottom: 15px; border-bottom: 2px solid #FF9800; }
        .back-btn { padding: 8px 16px; background-color: #757575; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
        .back-btn:hover { background-color: #616161; }

        .study-container { display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 30px; }
        .function-panel, .chart-panel { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .function-panel h3, .chart-panel h3 { margin-top: 0; color: #FF9800; text-align: center; }

        .controls { margin-bottom: 15px; text-align: center; }
        .controls button { margin: 0 5px; padding: 8px 16px; background-color: #FF9800; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .controls button:hover { background-color: #F57C00; }

        .function-table { max-height: 400px; overflow-y: auto; border: 1px solid #ddd; border-radius: 4px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
        th { background-color: #fff3e0; color: #FF9800; position: sticky; top: 0; }
        .x-column { background-color: #f9f9f9; font-weight: bold; }
        input[type="number"] { width: 100%; border: 1px solid #ddd; border-radius: 3px; text-align: center; padding: 4px; background: white; }

        #chartCanvas { max-height: 400px; width: 100%; border: 1px solid #ddd; border-radius: 4px; }
        .apply-controls { margin-top: 15px; text-align: center; padding: 15px; background: #f8f9fa; border-radius: 6px; }
        .apply-controls input { padding: 8px; border: 1px solid #ddd; border-radius: 4px; width: 120px; }
        .apply-controls button { background-color: #FF9800; color: white; border: none; padding: 8px 16px; border-radius: 4px; cursor: pointer; margin: 0 5px; }
        .apply-controls button:hover { background-color: #F57C00; }
        #result { font-weight: bold; color: #FF9800; font-size: 18px; min-width: 100px; display: inline-block; }

        .no-data { text-align: center; color: #999; padding: 40px; font-style: italic; }

        /* МОДАЛЬНЫЕ ОКНА */
        .modal { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 1000; justify-content: center; align-items: center; }
        .modal-content { background: white; padding: 30px; border-radius: 10px; max-width: 400px; width: 90%; box-shadow: 0 8px 30px rgba(0,0,0,0.3); }
        .modal-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .modal-header h3 { margin: 0; color: #333; }
        .close-btn { background: none; border: none; font-size: 24px; cursor: pointer; color: #666; }
        .close-btn:hover { color: #333; }
        .modal-buttons { display: flex; gap: 10px; margin-top: 20px; }
        .btn-primary { flex: 1; padding: 12px; background-color: #2196F3; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; }
        .btn-primary:hover { background-color: #1976D2; }
        .btn-success { flex: 1; padding: 12px; background-color: #4CAF50; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; }
        .btn-success:hover { background-color: #45a049; }

        /* СООБЩЕНИЯ */
        .message { padding: 10px; margin: 10px 0; border-radius: 4px; display: none; }
        .success-message { background-color: #dff0d8; color: #3c763d; border: 1px solid #d6e9c6; }
        .error-message { background-color: #f2dede; color: #a94442; border: 1px solid #ebccd1; }
        .loading { display: none; text-align: center; padding: 10px; color: #666; font-style: italic; }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>📊 Изучение табулированной функции</h1>
            <a href="<%=request.getContextPath()%>/ui/main" class="back-btn">На главную</a>
        </header>

        <!-- Сообщения -->
        <div id="successMessage" class="message success-message"></div>
        <div id="errorMessage" class="message error-message"></div>

        <div class="study-container">
            <!-- ТАБЛИЦА -->
            <div class="function-panel">
                <h3>Таблица значений</h3>
                <div class="controls">
                    <button onclick="createNewFunction()">Создать</button>
                    <button onclick="loadFunction()">Загрузить</button>
                    <button onclick="downloadFunction('dat')">Сохранить</button>
                </div>
                <div id="functionLoading" class="loading">Загрузка...</div>
                <div class="function-table">
                    <div id="pointsTable"></div>
                    <div style="text-align: center; margin-top: 10px;">
                        <button onclick="addPoint()">+ Добавить точку</button>
                    </div>
                </div>
            </div>

            <!-- ГРАФИК + ВЫЧИСЛЕНИЕ -->
            <div class="chart-panel">
                <h3>График функции</h3>
                <canvas id="chartCanvas"></canvas>
                <div class="apply-controls">
                    <label>f(</label>
                    <input type="number" id="xValue" step="0.01" placeholder="x">
                    <label>) =</label>
                    <button onclick="calculate()">Вычислить</button>
                    <span id="result">-</span>
                </div>
            </div>
        </div>
    </div>

    <!-- МОДАЛЬНОЕ ОКНО СОЗДАНИЯ -->
    <div id="createModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Создание функции</h3>
                <button class="close-btn" onclick="closeCreateModal()">×</button>
            </div>
            <p>Выберите способ создания табулированной функции:</p>
            <div class="modal-buttons">
                <button class="btn-primary" onclick="createFromArrays()">📈 Из массивов</button>
                <button class="btn-success" onclick="createFromFunction()">⚙️ Из функции</button>
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
        let chart = null;
        const contextPath = '<%=request.getContextPath()%>';
        let activeChildWindows = {};

        // Новый формат данных (как в operations.jsp)
        let functionData = {
            xValues: [],
            yValues: []
        };

        // Инициализация графика
        function initChart() {
            const ctx = document.getElementById('chartCanvas').getContext('2d');
            chart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: [],
                    datasets: [{
                        label: 'f(x)',
                        data: [],
                        borderColor: '#FF9800',
                        backgroundColor: 'rgba(255,152,0,0.1)',
                        tension: 0.3,
                        fill: true,
                        pointBackgroundColor: '#FF9800',
                        pointBorderColor: '#fff',
                        pointBorderWidth: 2,
                        pointRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: { title: { display: true, text: 'x' } },
                        y: { title: { display: true, text: 'f(x)' } }
                    }
                }
            });
        }

        // ✅ ОСНОВНАЯ ФУНКЦИЯ РЕНДЕРИНГА ТАБЛИЦЫ
        function renderTable() {
            const tbody = document.getElementById('pointsTable');

            if (!functionData.xValues || functionData.xValues.length === 0) {
                container.innerHTML = '<div class="no-data">Таблица пустая<br><small>Используйте "Создать" или "Загрузить" чтобы добавить данные</small></div>';
                updateChart();
                return;
            }

            let tableHTML = '<table>';
            tableHTML += '<thead><tr><th>№</th><th>X</th><th>f(x)</th><th></th></tr></thead>';
            tableHTML += '<tbody>';

            // Сортируем точки по X и сохраняем оригинальные индексы
            const indexedPoints = functionData.xValues.map((x, idx) => ({
                x: x,
                y: functionData.yValues[idx],
                originalIndex: idx
            })).sort((a, b) => a.x - b.x);

            for (let i = 0; i < indexedPoints.length; i++) {
                const point = indexedPoints[i];

                tableHTML += '<tr>';
                tableHTML += '<td>' + (i + 1) + '</td>';
                tableHTML += '<td class="x-column">' + formatNumber(point.x) + '</td>';
                tableHTML += '<td>';
                tableHTML += '<input type="number" step="any" value="' + formatNumber(point.y) + '" ';
                tableHTML += 'onchange="updateYValue(' + point.originalIndex + ', this.value)" ';
                tableHTML += 'style="width: 100%; border: 1px solid #ddd; border-radius: 3px; text-align: center; padding: 4px; background: white;">';
                tableHTML += '</td>';
                tableHTML += '<td>';
                tableHTML += '<button onclick="deletePoint(' + point.originalIndex + ')" ';
                tableHTML += 'style="background:#f44336;color:white;border:none;padding:4px 8px;border-radius:3px;cursor:pointer;font-size:12px;">';
                tableHTML += '✕</button>';
                tableHTML += '</td>';
                tableHTML += '</tr>';
            }

            tableHTML += '</tbody></table>';
            tbody.innerHTML = tableHTML;

            updateChart();
        }

        // ✅ ФОРМАТИРОВАНИЕ ЧИСЛА (целые числа без дробной части)
        function formatNumber(num) {
            if (typeof num !== 'number' || isNaN(num)) return '0';
            // Если число целое - показываем без точки
            if (Math.abs(num - Math.round(num)) < 0.000001) {
                return Math.round(num);
            }
            // Иначе показываем с 4 знаками после запятой
            return parseFloat(num.toFixed(4));
        }

        // ✅ ОБНОВЛЕНИЕ ЗНАЧЕНИЯ Y
        function updateYValue(index, value) {
            const newY = parseFloat(value);
            if (!isNaN(newY) && index >= 0 && index < functionData.yValues.length) {
                functionData.yValues[index] = newY;
                renderTable();
            }
        }

        // ✅ УДАЛЕНИЕ ТОЧКИ
        function deletePoint(index) {
            if (index >= 0 && index < functionData.xValues.length) {
                functionData.xValues.splice(index, 1);
                functionData.yValues.splice(index, 1);
                renderTable();
            }
        }

        // ✅ ДОБАВЛЕНИЕ НОВОЙ ТОЧКИ
        function addPoint() {
            const lastX = functionData.xValues.length > 0
                ? Math.max(...functionData.xValues) + 0.5
                : 0;

            functionData.xValues.push(lastX);
            functionData.yValues.push(0);
            renderTable();
        }

        // ✅ ОБНОВЛЕНИЕ ГРАФИКА
        function updateChart() {
            if (!chart) return;

            // Создаем массив точек и сортируем по X
            const points = functionData.xValues.map((x, i) => ({x, y: functionData.yValues[i]}))
                .sort((a, b) => a.x - b.x);

            chart.data.labels = points.map(p => formatNumber(p.x));
            chart.data.datasets[0].data = points.map(p => p.y);
            chart.update('none');
        }

        // ========== ФУНКЦИИ КНОПОК ==========

        // ✅ СОЗДАНИЕ НОВОЙ ФУНКЦИИ
        function createNewFunction() {
            document.getElementById('createModal').style.display = 'flex';
        }

        function closeCreateModal() {
            document.getElementById('createModal').style.display = 'none';
        }

        function createFromArrays() {
            closeCreateModal();
            openChildWindow(contextPath + '/ui/functions/create-from-arrays?returnTo=study&panel=1', 'createArrays');
        }

        function createFromFunction() {
            closeCreateModal();
            openChildWindow(contextPath + '/ui/functions/create-from-function?returnTo=study&panel=1', 'createFunction');
        }

        // ✅ ЗАГРУЗКА ФУНКЦИИ (исправленная)
        function loadFunction() {
            const input = document.createElement('input');
            input.type = 'file';
            input.accept = '.dat,.txt,.json';
            input.onchange = function(event) {
                const file = event.target.files[0];
                if (file) {
                    const formData = new FormData();
                    formData.append('file', file);
                    formData.append('returnTo', 'study');
                    formData.append('panel', '1');

                    showLoading(true);
                    fetch(contextPath + '/ui/functions/upload', {
                        method: 'POST',
                        body: formData
                    })
                    .then(response => response.json())
                    .then(data => {
                        showLoading(false);
                        if (data.success && data.xValues && data.yValues) {
                            // Сохраняем в новом формате
                            functionData = {
                                xValues: data.xValues,
                                yValues: data.yValues
                            };
                            renderTable();
                            showMessage(data.message || 'Функция загружена!', 'success');
                        } else {
                            showMessage(data.error || 'Ошибка загрузки', 'error');
                        }
                    })
                    .catch((error) => {
                        showLoading(false);
                        showMessage('Ошибка загрузки: ' + error.message, 'error');
                    });
                }
            };
            input.click();
        }

        // ✅ СОХРАНЕНИЕ ФУНКЦИИ (исправленная)
        function downloadFunction(format) {
            if (!functionData.xValues || functionData.xValues.length === 0) {
                return showMessage('Нет данных для сохранения', 'error');
            }

            // Подготавливаем данные
            const xValuesStr = JSON.stringify(functionData.xValues);
            const yValuesStr = JSON.stringify(functionData.yValues);

            // Формируем URL для скачивания
            const url = contextPath + '/ui/study/download?' +
                'xValues=' + encodeURIComponent(xValuesStr) +
                '&yValues=' + encodeURIComponent(yValuesStr) +
                '&format=' + format;

            const a = document.createElement('a');
            a.href = url;
            a.download = 'function.' + format;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            showMessage('Функция сохранена!', 'success');
        }

        // ✅ ВЫЧИСЛЕНИЕ В ТОЧКЕ (исправленная)
        function calculate() {
            const xInput = document.getElementById('xValue');
            const x = parseFloat(xInput.value);
            // Проверяем, что поле не пустое
                if (xInput.value.trim() === '') {
                    return showMessage('Введите значение x', 'error');
                }

                if (isNaN(x)) {
                    return showMessage('Введите корректное число для x', 'error');
                }

                if (!functionData.xValues || functionData.xValues.length < 2) {
                    return showMessage('Нужно минимум 2 точки для вычисления', 'error');
                }

            console.log('Отправляем запрос с данными:', {
                x: x,
                xValues: functionData.xValues,
                yValues: functionData.yValues
            });

            // ВАРИАНТ 1: Используем FormData (как в operations.jsp)
            const formData = new FormData();
            formData.append('x', x);
            formData.append('xValues', JSON.stringify(functionData.xValues));
            formData.append('yValues', JSON.stringify(functionData.yValues));

            fetch(contextPath + '/ui/study/apply', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                console.log('Статус ответа:', response.status);
                if (!response.ok) {
                    throw new Error('HTTP ошибка: ' + response.status);
                }
                return response.json();
            })
            .then(result => {
                console.log('Ответ сервера:', result);
                if (result.y !== undefined) {
                    document.getElementById('result').textContent = formatNumber(result.y);
                    showMessage('Значение вычислено', 'success');
                } else if (result.error) {
                    showMessage(result.error, 'error');
                } else {
                    showMessage('Некорректный ответ сервера', 'error');
                }
            })
            .catch(error => {
                console.error('Полная ошибка:', error);
                // Пробуем альтернативный endpoint
                tryAlternativeCalculation(x);
            });
        }

        // Альтернативный метод расчета на клиенте (если сервер не отвечает)
         function tryAlternativeCalculation(x) {
             console.log('Пробуем локальный расчет...');

             const xValues = functionData.xValues;
             const yValues = functionData.yValues;

             // Если функция постоянная
             const allSame = yValues.every(val => Math.abs(val - yValues[0]) < 0.0001);
             if (allSame && yValues.length > 0) {
                 const result = yValues[0];
                 document.getElementById('result').textContent = formatNumber(result);
                 showMessage('Постоянная функция', 'success');
                 return;
             }

             // Проверяем точное совпадение
             for (let i = 0; i < xValues.length; i++) {
                 if (Math.abs(xValues[i] - x) < 0.0001) {
                     document.getElementById('result').textContent = formatNumber(yValues[i]);
                     showMessage('Точное совпадение', 'success');
                     return;
                 }
             }

             // Сортируем точки
             const sortedPoints = xValues.map((xVal, idx) => ({x: xVal, y: yValues[idx]}))
                 .sort((a, b) => a.x - b.x);

             const n = sortedPoints.length;

             // Определяем положение x
             if (x < sortedPoints[0].x) {
                 // ЭКСТРАПОЛЯЦИЯ ВЛЕВО
                 if (n >= 3) {
                     // Квадратичная экстраполяция
                     const x0 = sortedPoints[0].x, y0 = sortedPoints[0].y;
                     const x1 = sortedPoints[1].x, y1 = sortedPoints[1].y;
                     const x2 = sortedPoints[2].x, y2 = sortedPoints[2].y;

                     const L0 = ((x - x1)*(x - x2)) / ((x0 - x1)*(x0 - x2));
                     const L1 = ((x - x0)*(x - x2)) / ((x1 - x0)*(x1 - x2));
                     const L2 = ((x - x0)*(x - x1)) / ((x2 - x0)*(x2 - x1));

                     const result = y0*L0 + y1*L1 + y2*L2;

                     document.getElementById('result').textContent = formatNumber(result);
                     showMessage('Квадратичная экстраполяция влево', 'warning');
                 } else {
                     // Линейная экстраполяция
                     const x1 = sortedPoints[0].x, y1 = sortedPoints[0].y;
                     const x2 = sortedPoints[1].x, y2 = sortedPoints[1].y;
                     const slope = (y2 - y1) / (x2 - x1);
                     const result = y1 + slope * (x - x1);

                     document.getElementById('result').textContent = formatNumber(result);
                     showMessage('Линейная экстраполяция влево', 'warning');
                 }

             } else if (x > sortedPoints[n-1].x) {
                 // ЭКСТРАПОЛЯЦИЯ ВПРАВО
                 if (n >= 3) {
                     // Квадратичная экстраполяция
                     const x0 = sortedPoints[n-3].x, y0 = sortedPoints[n-3].y;
                     const x1 = sortedPoints[n-2].x, y1 = sortedPoints[n-2].y;
                     const x2 = sortedPoints[n-1].x, y2 = sortedPoints[n-1].y;

                     const L0 = ((x - x1)*(x - x2)) / ((x0 - x1)*(x0 - x2));
                     const L1 = ((x - x0)*(x - x2)) / ((x1 - x0)*(x1 - x2));
                     const L2 = ((x - x0)*(x - x1)) / ((x2 - x0)*(x2 - x1));

                     const result = y0*L0 + y1*L1 + y2*L2;

                     document.getElementById('result').textContent = formatNumber(result);
                     showMessage('Квадратичная экстраполяция вправо', 'warning');
                 } else {
                     // Линейная экстраполяция
                     const x1 = sortedPoints[n-2].x, y1 = sortedPoints[n-2].y;
                     const x2 = sortedPoints[n-1].x, y2 = sortedPoints[n-1].y;
                     const slope = (y2 - y1) / (x2 - x1);
                     const result = y2 + slope * (x - x2);

                     document.getElementById('result').textContent = formatNumber(result);
                     showMessage('Линейная экстраполяция вправо', 'warning');
                 }

             } else {
                 // ИНТЕРПОЛЯЦИЯ
                 for (let i = 0; i < n - 1; i++) {
                     if (x >= sortedPoints[i].x && x <= sortedPoints[i + 1].x) {
                         const x1 = sortedPoints[i].x, y1 = sortedPoints[i].y;
                         const x2 = sortedPoints[i + 1].x, y2 = sortedPoints[i + 1].y;

                         // Линейная интерполяция
                         const t = (x - x1) / (x2 - x1);
                         const result = y1 + t * (y2 - y1);

                         document.getElementById('result').textContent = formatNumber(result);
                         showMessage('Линейная интерполяция', 'success');
                         return;
                     }
                 }

                 document.getElementById('result').textContent = '—';
                 showMessage('Не удалось вычислить', 'error');
             }
         }

        // ========== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ==========

        function openChildWindow(url, name) {
            if (activeChildWindows[name] && !activeChildWindows[name].closed) {
                activeChildWindows[name].close();
            }
            const win = window.open(url, name, 'width=900,height=700,resizable=yes,scrollbars=yes');
            activeChildWindows[name] = win;
            if (win) trackChildWindow(win, name);
        }

        function trackChildWindow(win, name) {
            const interval = setInterval(() => {
                if (win.closed) {
                    clearInterval(interval);
                    delete activeChildWindows[name];
                    checkForSavedData();
                }
            }, 100);
        }

        // ✅ ОБРАБОТЧИК ДАННЫХ ИЗ ДОЧЕРНИХ ОКОН
        window.handleFunctionData = function(data) {
            console.log('Получены данные функции:', data);
            if (data.returnTo === 'study') {
                // Конвертируем данные в новый формат
                functionData = {
                    xValues: data.xValues || [],
                    yValues: data.yValues || []
                };
                renderTable();
                showMessage('Функция создана!', 'success');
            }
        };

        function checkForSavedData() {
            const dataStr = localStorage.getItem('createdFunctionData');
            if (dataStr) {
                try {
                    const data = JSON.parse(dataStr);
                    if (data.returnTo === 'study') {
                        functionData = {
                            xValues: data.xValues || [],
                            yValues: data.yValues || []
                        };
                        renderTable();
                        showMessage('Функция загружена из сохранения!', 'success');
                    }
                    localStorage.removeItem('createdFunctionData');
                } catch(e) {
                    console.error('Ошибка парсинга данных:', e);
                }
            }
        }

        function showLoading(show) {
            document.getElementById('functionLoading').style.display = show ? 'block' : 'none';
            document.querySelector('.function-table').style.display = show ? 'none' : 'block';
        }

        function showMessage(msg, type) {
            const successMsg = document.getElementById('successMessage');
            const errorMsg = document.getElementById('errorMessage');

            if (type === 'success') {
                successMsg.textContent = msg;
                successMsg.style.display = 'block';
                errorMsg.style.display = 'none';
                setTimeout(() => {
                    successMsg.style.display = 'none';
                }, 4000);
            } else {
                errorMsg.textContent = msg;
                errorMsg.style.display = 'block';
                successMsg.style.display = 'none';
                setTimeout(() => {
                    errorMsg.style.display = 'none';
                }, 4000);
            }
        }

        // ✅ ИНИЦИАЛИЗАЦИЯ ПРИ ЗАГРУЗКЕ СТРАНИЦЫ
        window.onload = function() {
            initChart();
            // Начальные данные в новом формате
            functionData = {
                xValues: [],
                yValues: []
            };
            renderTable();
        };

        // Закрытие модального окна при клике вне его
        window.onclick = function(event) {
            if (event.target.id === 'createModal') {
                closeCreateModal();
            }
        };
    </script>
</body>
</html>