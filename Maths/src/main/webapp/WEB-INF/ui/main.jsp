<%-- src/main/webapp/WEB-INF/ui/main.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Табулированные функции</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f2f5;
        }

        .container {
            max-width: 1200px;
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

        .logo {
            font-size: 24px;
            font-weight: bold;
            color: #2196F3;
        }

        .settings-btn {
            padding: 8px 16px;
            background-color: #757575;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .settings-btn:hover {
            background-color: #616161;
        }

        .main-menu {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .menu-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
            text-align: center;
        }

        .menu-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0,0,0,0.15);
        }

        .menu-icon {
            font-size: 48px;
            margin-bottom: 15px;
        }

        .menu-card h3 {
            margin: 0 0 10px 0;
            color: #333;
        }

        .menu-card p {
            color: #666;
            line-height: 1.5;
        }

        .status-bar {
            background: white;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .status-bar h4 {
            margin: 0 0 10px 0;
            color: #333;
        }

        .factory-info {
            color: #666;
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
            max-width: 500px;
            width: 90%;
            max-height: 90vh;
            overflow-y: auto;
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

        .close-btn:hover {
            color: #333;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <div class="logo">Табулированные функции</div>
            <button class="settings-btn" onclick="openSettings()">Настройки</button>
        </header>

        <div class="main-menu">
            <div class="menu-card" onclick="openOperationWindow()">
                <div class="menu-icon">➕</div>
                <h3>Операции с функциями</h3>
                <p>Сложение, вычитание, умножение и деление двух табулированных функций</p>
            </div>

            <div class="menu-card" onclick="openDifferentiationWindow()">
                <div class="menu-icon">📐</div>
                <h3>Дифференцирование</h3>
                <p>Дифференцирование табулированной функции</p>
            </div>

            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/functions/create-from-arrays'">
                <div class="menu-icon">📊</div>
                <h3>Создать из массивов</h3>
                <p>Создание функции путем ввода значений X и Y вручную</p>
            </div>

            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/functions/create-from-function'">
                <div class="menu-icon">📈</div>
                <h3>Создать из функции</h3>
                <p>Создание функции путем табуляции математической функции</p>
            </div>
        </div>

        <div class="status-bar">
            <h4>Текущие настройки</h4>
            <div class="factory-info">
                Используемая фабрика: <span id="currentFactory">Массив</span>
            </div>
        </div>
    </div>

    <!-- Модальное окно настроек -->
    <div id="settingsModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Настройки</h3>
                <button class="close-btn" onclick="closeSettings()">×</button>
            </div>

            <div class="form-group">
                <label for="factoryType">Тип фабрики для создания функций:</label>
                <select id="factoryType" style="width: 100%; padding: 10px; margin: 10px 0;">
                    <option value="array">Массив (ArrayTabulatedFunctionFactory)</option>
                    <option value="linkedlist">Связный список (LinkedListTabulatedFunctionFactory)</option>
                </select>
            </div>

            <div style="margin-top: 20px; text-align: right;">
                <button onclick="saveSettings()" style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Сохранить
                </button>
            </div>
        </div>
    </div>

    <script>
        const contextPath = '<%= request.getContextPath() %>';

        // Загружаем текущие настройки при загрузке страницы
        window.onload = function() {
            loadSettings();
        };

        function openSettings() {
            document.getElementById('settingsModal').style.display = 'flex';
        }

        function closeSettings() {
            document.getElementById('settingsModal').style.display = 'none';
        }

        function saveSettings() {
            const factoryType = document.getElementById('factoryType').value;

            // Сохраняем в localStorage
            localStorage.setItem('factoryType', factoryType);

            // Обновляем отображение
            updateFactoryDisplay(factoryType);

            // Отправляем на сервер
            fetch(contextPath + '/ui/settings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'factoryType=' + encodeURIComponent(factoryType)
            })
            .then(response => {
                if (!response.ok) {
                    console.error('Ошибка сохранения настроек');
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
            });

            closeSettings();
        }

        function loadSettings() {
            const factoryType = localStorage.getItem('factoryType') || 'array';
            document.getElementById('factoryType').value = factoryType;
            updateFactoryDisplay(factoryType);
        }

        function updateFactoryDisplay(factoryType) {
            const displayElement = document.getElementById('currentFactory');
            if (factoryType === 'array') {
                displayElement.textContent = 'Массив (ArrayTabulatedFunctionFactory)';
            } else {
                displayElement.textContent = 'Связный список (LinkedListTabulatedFunctionFactory)';
            }
        }

        function openOperationWindow() {
            // Открываем новое окно для операций
            window.location.href = contextPath + '/ui/operations';
        }

        function openDifferentiationWindow() {
            // Открываем новое окно для дифференцирования
            window.location.href = contextPath + '/ui/differentiation';
        }

        // Закрытие модального окна при клике вне его
        window.onclick = function(event) {
            const modal = document.getElementById('settingsModal');
            if (event.target === modal) {
                closeSettings();
            }
        };
    </script>
</body>
</html>