<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Создание функции из массивов</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
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

        input[type="number"] {
            width: 200px;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
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
        }

        button:hover {
            background-color: #45a049;
        }

        button:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
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
            justify-content: space-between;
            align-items: center;
        }

        .loading {
            display: none;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Создание табулированной функции из массивов</h1>

        <div class="form-group">
            <label for="pointsCount">Количество точек (от 2 до 1000):</label>
            <input type="number" id="pointsCount" name="pointsCount" min="2" max="1000" value="10">
            <button onclick="generateTable()">Создать таблицу</button>
        </div>

        <div id="pointsTable">
            <!-- Таблица будет создана здесь -->
        </div>

        <div class="controls">
            <button onclick="createFunction()" id="createBtn" disabled>Создать функцию</button>
            <span id="loading" class="loading">Создание функции...</span>
        </div>

        <div style="margin-top: 20px;">
            <button onclick="goBack()">Назад к списку</button>
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
        // Получаем контекст приложения
        const contextPath = '<%= request.getContextPath() %>';

        let currentPointsCount = 0;

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
                inputX.id = 'x' + i;          // Важно: уникальный ID
                inputX.name = 'x' + i;        // Важно: уникальное имя
                inputX.step = 'any';
                inputX.required = true;
                tdX.appendChild(inputX);
                row.appendChild(tdX);

                // Поле Y
                const tdY = document.createElement('td');
                const inputY = document.createElement('input');
                inputY.type = 'number';
                inputY.id = 'y' + i;          // Важно: уникальный ID
                inputY.name = 'y' + i;        // Важно: уникальное имя
                inputY.step = 'any';
                inputY.required = true;
                tdY.appendChild(inputY);
                row.appendChild(tdY);

                tbody.appendChild(row);
            }

            table.appendChild(thead);
            table.appendChild(tbody);
            tableDiv.appendChild(table);

            document.getElementById('createBtn').disabled = false;

            console.log('Таблица создана с', currentPointsCount, 'строками');

            // Очищаем все поля
            for (let i = 0; i < currentPointsCount; i++) {
                const xInput = document.getElementById('x' + i);
                const yInput = document.getElementById('y' + i);

                if (xInput) xInput.value = '';
                if (yInput) yInput.value = '';
            }
        }

        function createFunction() {
            console.log('=== createFunction called ===');
            console.log('Current points count:', currentPointsCount);

            // Создаем URLSearchParams для отправки данных
            const params = new URLSearchParams();
            params.append('pointsCount', currentPointsCount.toString());

            let hasError = false;

            // Собираем все значения X и Y
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

                // Добавляем в параметры
                params.append('x' + i, xValue);
                params.append('y' + i, yValue);
            }

            if (!hasError) {
                console.log('Все данные собраны, отправляем...');

                document.getElementById('loading').style.display = 'block';
                document.getElementById('createBtn').disabled = true;

                // Отправляем как обычную форму (application/x-www-form-urlencoded)
                fetch(contextPath + '/ui/functions/create-from-arrays', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                    },
                    body: params.toString()
                })
                .then(response => {
                    console.log('Ответ получен:', response.status, response.statusText);

                    document.getElementById('loading').style.display = 'none';
                    document.getElementById('createBtn').disabled = false;

                    if (!response.ok) {
                        return response.text().then(text => {
                            console.error('Текст ошибки:', text);
                            try {
                                const errorData = JSON.parse(text);
                                throw new Error(`${errorData.error || 'Ошибка сервера'}: ${errorData.details || text}`);
                            } catch (e) {
                                throw new Error(`HTTP ${response.status}: ${text || 'Неизвестная ошибка'}`);
                            }
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Успешный ответ:', data);
                    alert(data.message + '\n' +
                          'Количество точек: ' + data.pointsCount + '\n' +
                          'Интервал: [' + data.leftBound + ', ' + data.rightBound + ']');
                    goBack();
                })
                .catch(error => {
                    console.error('Ошибка fetch:', error);
                    showError('Ошибка создания функции', error.message);
                });
            }
        }

        function showError(title, message) {
            document.getElementById('errorTitle').textContent = title;
            document.getElementById('errorMessage').textContent = message;
            document.getElementById('errorModal').style.display = 'flex';
        }

        function closeErrorModal() {
            document.getElementById('errorModal').style.display = 'none';
        }

        function goBack() {
            window.location.href = contextPath + '/ui';
        }

        // Генерируем таблицу при загрузке
        window.onload = function() {
            console.log('Page loaded, generating initial table...');
            generateTable();
        };
    </script>
</body>
</html>