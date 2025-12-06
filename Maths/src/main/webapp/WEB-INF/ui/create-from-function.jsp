<%-- src/main/webapp/WEB-INF/ui/create-from-function.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Создание функции из математической функции</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
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

        select, input[type="number"] {
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
        }

        button:hover {
            background-color: #1976D2;
        }

        button:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
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

        .back-button {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #757575;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            text-align: center;
        }

        .back-button:hover {
            background-color: #616161;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Создание функции из математической функции</h1>

        <form id="createFunctionForm">
            <div class="form-group">
                <label for="functionName">Выберите функцию:</label>
                <select id="functionName" name="functionName" required>
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

            <button type="button" onclick="createFunction()" id="createBtn">Создать функцию</button>
            <div id="loading" class="loading">Создание функции...</div>
        </form>

        <a href="${pageContext.request.contextPath}/ui" class="back-button">Назад к выбору метода</a>
    </div>

    <div id="errorModal" class="error-modal">
        <div class="modal-content">
            <h3 id="errorTitle">Ошибка</h3>
            <p id="errorMessage"></p>
            <button onclick="closeErrorModal()">Закрыть</button>
        </div>
    </div>

    <script>
        // Загрузка списка функций при загрузке страницы
        window.onload = function() {
            fetch('${pageContext.request.contextPath}/ui/functions')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Ошибка загрузки: ' + response.status);
                    }
                    return response.json();
                })
                .then(functions => {
                    const select = document.getElementById('functionName');
                    functions.forEach(funcName => {
                        const option = document.createElement('option');
                        option.value = funcName;
                        option.textContent = funcName;
                        select.appendChild(option);
                    });

                    // Добавляем обработчик изменения выбора
                    select.addEventListener('change', function() {
                        const descriptionDiv = document.getElementById('functionDescription');
                        const descriptionText = document.getElementById('descriptionText');

                        if (this.value) {
                            // Здесь можно добавить более подробные описания
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

        function createFunction() {
            const form = document.getElementById('createFunctionForm');

            // Базовая валидация
            if (!form.checkValidity()) {
                form.reportValidity();
                return;
            }

            const formData = new FormData(form);

            // Дополнительная валидация
            const xFrom = parseFloat(document.getElementById('xFrom').value);
            const xTo = parseFloat(document.getElementById('xTo').value);

            if (isNaN(xFrom) || isNaN(xTo)) {
                showError('Ошибка', 'Введите корректные числовые значения для интервала');
                return;
            }

            if (xFrom >= xTo) {
                showError('Ошибка', 'Начало интервала должно быть меньше конца');
                return;
            }

            const pointsCount = parseInt(document.getElementById('pointsCount').value);
            if (isNaN(pointsCount) || pointsCount < 2 || pointsCount > 10000) {
                showError('Ошибка', 'Количество точек должно быть от 2 до 10000');
                return;
            }

            document.getElementById('loading').style.display = 'block';
            document.getElementById('createBtn').disabled = true;

            fetch('${pageContext.request.contextPath}/ui/functions/create-from-function', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                document.getElementById('loading').style.display = 'none';
                document.getElementById('createBtn').disabled = false;

                if (!response.ok) {
                    return response.json().then(error => {
                        throw new Error(`${error.error || 'Ошибка'}: ${error.details || error.message || 'Неизвестная ошибка'}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                alert(data.message + '\n' +
                      'Функция: ' + formData.get('functionName') + '\n' +
                      'Количество точек: ' + data.pointsCount + '\n' +
                      'Интервал: [' + data.leftBound + ', ' + data.rightBound + ']');
                // Можно закрыть окно или перейти куда-то
                window.location.href = '${pageContext.request.contextPath}/ui';
            })
            .catch(error => {
                showError('Ошибка создания функции', error.message);
            });
        }

        function showError(title, message) {
            document.getElementById('errorTitle').textContent = title;
            document.getElementById('errorMessage').textContent = message;
            document.getElementById('errorModal').style.display = 'flex';
        }

        function closeErrorModal() {
            document.getElementById('errorModal').style.display = 'none';
        }
    </script>
</body>
</html>