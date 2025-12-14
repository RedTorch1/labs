<%-- src/main/webapp/WEB-INF/ui/main.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Табулированные функции</title>
    <meta charset="UTF-8">
    <style>
        /* БАЗОВЫЕ СТИЛИ - ОБЩИЕ ДЛЯ ВСЕХ ТЕМ */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f2f5;
            color: #333; /* Базовый цвет текста для светлой темы */
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

        .auth-buttons {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .user-name {
            font-weight: bold;
            color: #333;
        }

        .logout-btn {
            padding: 8px 16px;
            background-color: #f44336;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .logout-btn:hover {
            background-color: #d32f2f;
        }

        .login-btn {
            padding: 8px 16px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .login-btn:hover {
            background-color: #388E3C;
        }

        .register-btn {
            padding: 8px 16px;
            background-color: #2196F3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .register-btn:hover {
            background-color: #1976D2;
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
            color: #333; /* Цвет текста в карточке для светлой темы */
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

        .theme-info {
            color: #666;
            margin-top: 5px;
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

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            color: #333;
        }

        .error-message {
            color: #f44336;
            font-size: 14px;
            margin-top: 5px;
            display: none;
        }

        .success-message {
            color: #4CAF50;
            font-size: 14px;
            margin-top: 5px;
            display: none;
        }

        /* ========== ТЕМНАЯ ТЕМА ========== */
        body.dark-theme {
            background-color: #1a1a1a !important;
            color: #f0f0f0 !important;
        }

        /* Текст в темной теме */
        .dark-theme,
        .dark-theme .user-name,
        .dark-theme .menu-card h3,
        .dark-theme .status-bar h4,
        .dark-theme .modal-header h3,
        .dark-theme .form-group label,
        .dark-theme .factory-info,
        .dark-theme .theme-info,
        .dark-theme .menu-card p {
            color: #f0f0f0 !important;
        }

        /* Фоны в темной теме */
        .dark-theme .container,
        .dark-theme .menu-card,
        .dark-theme .status-bar,
        .dark-theme .modal-content {
            background-color: #2d2d2d !important;
        }

        .dark-theme .menu-card {
            background-color: #3d3d3d !important;
            border: 1px solid #444 !important;
        }

        .dark-theme .menu-card:hover {
            box-shadow: 0 6px 12px rgba(0,0,0,0.3) !important;
            background-color: #4a4a4a !important;
        }

        .dark-theme header {
            border-bottom: 2px solid #444 !important;
        }

        /* Кнопки в темной теме */
        .dark-theme .settings-btn {
            background-color: #666 !important;
        }

        .dark-theme .settings-btn:hover {
            background-color: #777 !important;
        }

        .dark-theme .login-btn {
            background-color: #2e7d32 !important;
        }

        .dark-theme .login-btn:hover {
            background-color: #388E3C !important;
        }

        .dark-theme .register-btn {
            background-color: #1565c0 !important;
        }

        .dark-theme .register-btn:hover {
            background-color: #1976D2 !important;
        }

        .dark-theme .logout-btn {
            background-color: #c62828 !important;
        }

        .dark-theme .logout-btn:hover {
            background-color: #d32f2f !important;
        }

        /* Формы в темной теме */
        .dark-theme .form-group input,
        .dark-theme .form-group select {
            background-color: #3d3d3d !important;
            color: #f0f0f0 !important;
            border: 1px solid #555 !important;
        }

        .dark-theme .form-group input:focus,
        .dark-theme .form-group select:focus {
            border-color: #2196F3 !important;
            outline: none !important;
        }

        /* Иконки и кнопки закрытия */
        .dark-theme .close-btn {
            color: #aaa !important;
        }

        .dark-theme .close-btn:hover {
            color: #fff !important;
        }

        /* Сообщения */
        .dark-theme .error-message {
            color: #ff6b6b !important;
        }

        .dark-theme .success-message {
            color: #66bb6a !important;
        }

        /* Логотип - оставляем синий в обеих темах */
        .dark-theme .logo {
            color: #2196F3 !important;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <div class="logo">Табулированные функции</div>
            <div class="auth-buttons">
                <div id="userSection" style="display: none;">
                    <div class="user-info">
                        <span class="user-name" id="userName"></span>
                        <button class="logout-btn" onclick="logout()">Выйти</button>
                    </div>
                </div>
                <div id="guestSection">
                    <button class="login-btn" onclick="openLoginModal()">Войти</button>
                    <button class="register-btn" onclick="openRegisterModal()">Регистрация</button>
                </div>
                <button class="settings-btn" onclick="openSettings()">Настройки</button>
            </div>
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
            <div class="menu-card" onclick="openStudyWindow()">
                <div class="menu-icon">📊</div>
                <h3>Изучение функций</h3>
                <p>Графическое изучение табулированной функции</p>
            </div>
            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/manage-functions'">
                <div class="menu-icon">⚙️</div>
                <h3>Управление функциями</h3>
                <p>Просмотр, редактирование и удаление сохраненных функций</p>
            </div>
        </div>

        <div class="status-bar">
            <h4>Текущие настройки</h4>
            <div class="factory-info">
                Используемая фабрика: <span id="currentFactory">Массив</span>
            </div>
            <div class="theme-info">
                Текущая тема: <span id="currentTheme">Светлая</span>
            </div>
        </div>
    </div>

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

            <div class="form-group">
                <label for="themeSelect">Тема оформления:</label>
                <select id="themeSelect" style="width: 100%; padding: 10px; margin: 10px 0;">
                    <option value="light">Светлая тема</option>
                    <option value="dark">Тёмная тема</option>
                </select>
            </div>

            <div style="margin-top: 20px; text-align: right;">
                <button onclick="saveSettings()" style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Сохранить
                </button>
            </div>
        </div>
    </div>

    <div id="loginModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Вход в систему</h3>
                <button class="close-btn" onclick="closeLoginModal()">×</button>
            </div>
            <div class="form-group">
                <label for="loginUsername">Имя пользователя:</label>
                <input type="text" id="loginUsername" placeholder="Введите имя пользователя">
                <div class="error-message" id="loginUsernameError"></div>
            </div>
            <div class="form-group">
                <label for="loginPassword">Пароль:</label>
                <input type="password" id="loginPassword" placeholder="Введите пароль">
                <div class="error-message" id="loginPasswordError"></div>
            </div>
            <div class="error-message" id="loginGeneralError"></div>
            <div class="success-message" id="loginSuccessMessage"></div>
            <div style="margin-top: 20px; text-align: right;">
                <button onclick="performLogin()" style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Войти
                </button>
            </div>
        </div>
    </div>

    <div id="registerModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Регистрация</h3>
                <button class="close-btn" onclick="closeRegisterModal()">×</button>
            </div>
            <div class="form-group">
                <label for="registerUsername">Имя пользователя:</label>
                <input type="text" id="registerUsername" placeholder="Введите имя пользователя">
                <div class="error-message" id="registerUsernameError"></div>
            </div>
            <div class="form-group">
                <label for="registerPassword">Пароль:</label>
                <input type="password" id="registerPassword" placeholder="Введите пароль">
                <div class="error-message" id="registerPasswordError"></div>
            </div>
            <div class="form-group">
                <label for="registerConfirmPassword">Подтвердите пароль:</label>
                <input type="password" id="registerConfirmPassword" placeholder="Повторите пароль">
                <div class="error-message" id="registerConfirmPasswordError"></div>
            </div>
            <div class="error-message" id="registerGeneralError"></div>
            <div class="success-message" id="registerSuccessMessage"></div>
            <div style="margin-top: 20px; text-align: right;">
                <button onclick="performRegister()" style="padding: 10px 20px; background-color: #2196F3; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Зарегистрироваться
                </button>
            </div>
        </div>
    </div>

    <script>
        // ========== ПРОСТАЯ РАБОЧАЯ ВЕРСИЯ С BASIC AUTH ==========

        const contextPath = '${pageContext.request.contextPath}' || '';
        const API = {
            LOGIN: (contextPath + '/api/auth').replace('//', '/'),
            REGISTER: (contextPath + '/api/auth/register').replace('//', '/')
        };

        console.log('API endpoints:', API);

        // Инициализация
        window.onload = function() {
            console.log('Page loaded');
            loadSettings();
            checkAuthState();
        };

        // Проверка состояния
        function checkAuthState() {
            const username = localStorage.getItem('username');
            const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';
            const userId = localStorage.getItem('userId');

            console.log('Auth state:', { username, isAuthenticated, userId });

            if (username && isAuthenticated && userId) {
                showUserSection(username);
            } else {
                showGuestSection();
                // Очищаем устаревшие данные
                localStorage.removeItem('username');
                localStorage.removeItem('isAuthenticated');
                localStorage.removeItem('userId');
                localStorage.removeItem('authToken');
                localStorage.removeItem('userRole');
            }
        }

        function showUserSection(username) {
            document.getElementById('userSection').style.display = 'block';
            document.getElementById('guestSection').style.display = 'none';
            document.getElementById('userName').textContent = username;
        }

        function showGuestSection() {
            document.getElementById('userSection').style.display = 'none';
            document.getElementById('guestSection').style.display = 'block';
        }

        // ========== ПРОСТАЯ ВЕРСИЯ ЛОГИНА (через UserServlet) ==========
        async function performLogin() {
            clearErrors('login');

            const username = document.getElementById('loginUsername').value.trim();
            const password = document.getElementById('loginPassword').value.trim();

            if (!username || !password) {
                if (!username) showError('loginUsernameError', 'Введите имя пользователя');
                if (!password) showError('loginPasswordError', 'Введите пароль');
                return;
            }

            const loginBtn = document.querySelector('#loginModal button');
            const originalText = loginBtn.textContent;
            loginBtn.textContent = 'Вход...';
            loginBtn.disabled = true;

            try {
                // 1. Получаем всех пользователей
                const usersResponse = await fetch(contextPath + '/api/users', {
                    headers: getAuthHeaders(null)
                });

                if (!usersResponse.ok) {
                    throw new Error('Не удалось получить список пользователей');
                }

                const allUsers = await usersResponse.json();
                console.log('Все пользователи:', allUsers);

                // 2. Ищем пользователя с таким username
                const user = allUsers.find(u =>
                    u.username && u.username.toLowerCase() === username.toLowerCase()
                );

                if (!user) {
                    showError('loginGeneralError', 'Пользователь не найден');
                    return;
                }

                // 3. ПРОСТАЯ ПРОВЕРКА ПАРОЛЯ (пока без хэширования)
                // ВНИМАНИЕ: Это временное решение!
                // В реальном приложении нужно использовать хэширование

                // Проверяем пароль напрямую (пока)
                if (user.passwordHash === password) {
                    // Успешный вход
                    localStorage.setItem('username', username);
                    localStorage.setItem('isAuthenticated', 'true');
                    localStorage.setItem('userId', user.id || '');
                    localStorage.setItem('userRole', user.role || 'USER');

                    // Создаем Basic Auth token для будущих запросов
                    const authToken = 'Basic ' + btoa(username + ':' + password);
                    localStorage.setItem('authToken', authToken);

                    showSuccess('loginSuccessMessage', 'Вход выполнен успешно!');

                    setTimeout(() => {
                        closeLoginModal();
                        checkAuthState();
                    }, 1000);
                } else {
                    showError('loginGeneralError', 'Неверный пароль');
                }

            } catch (error) {
                console.error('Login error:', error);
                showError('loginGeneralError', 'Ошибка: ' + error.message);
            } finally {
                loginBtn.textContent = originalText;
                loginBtn.disabled = false;
            }
        }

        // ========== ПРОСТАЯ ВЕРСИЯ РЕГИСТРАЦИИ (через UserServlet) ==========
        async function performRegister() {
            clearErrors('register');

            const username = document.getElementById('registerUsername').value.trim();
            const password = document.getElementById('registerPassword').value.trim();
            const confirmPassword = document.getElementById('registerConfirmPassword').value.trim();

            if (!validateRegisterForm(username, password, confirmPassword)) {
                return;
            }

            const registerBtn = document.querySelector('#registerModal button');
            const originalText = registerBtn.textContent;
            registerBtn.textContent = 'Регистрация...';
            registerBtn.disabled = true;

            try {
                // 1. Проверяем, нет ли уже такого пользователя
                const checkResponse = await fetch(contextPath + '/api/users/search?username=' + encodeURIComponent(username), {
                    headers: getAuthHeaders(null)
                });

                if (checkResponse.ok) {
                    const existingUsers = await checkResponse.json();
                    if (existingUsers.length > 0) {
                        showError('registerGeneralError', 'Пользователь с таким именем уже существует');
                        return;
                    }
                }

                // 2. Создаем нового пользователя
                const newUser = {
                    username: username,
                    passwordHash: password, // Прямой пароль (временно)
                    role: 'USER'
                };

                const createResponse = await fetch(contextPath + '/api/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(newUser)
                });

                if (createResponse.ok) {
                    showSuccess('registerSuccessMessage', 'Регистрация успешна! Теперь войдите в систему.');

                    setTimeout(() => {
                        closeRegisterModal();
                        // Автоматически заполняем форму логина
                        document.getElementById('loginUsername').value = username;
                        document.getElementById('loginPassword').value = password;
                        openLoginModal();
                    }, 1500);
                } else {
                    const errorText = await createResponse.text();
                    showError('registerGeneralError', 'Ошибка создания пользователя: ' + errorText);
                }

            } catch (error) {
                console.error('Register error:', error);
                showError('registerGeneralError', 'Ошибка: ' + error.message);
            } finally {
                registerBtn.textContent = originalText;
                registerBtn.disabled = false;
            }
        }

        // ========== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ==========
        function getAuthHeaders(contentType = 'application/json') {
            const headers = {};

            // Используем Basic Auth если есть токен
            const authToken = localStorage.getItem('authToken');
            if (authToken) {
                headers['Authorization'] = authToken;
            }

            if (contentType) {
                headers['Content-Type'] = contentType;
            }

            return headers;
        }

        function validateRegisterForm(username, password, confirmPassword) {
            let valid = true;

            if (!username || username.length < 3) {
                showError('registerUsernameError', 'Имя пользователя должно быть не менее 3 символов');
                valid = false;
            }

            if (!password || password.length < 6) {
                showError('registerPasswordError', 'Пароль должен быть не менее 6 символов');
                valid = false;
            }

            if (password !== confirmPassword) {
                showError('registerConfirmPasswordError', 'Пароли не совпадают');
                valid = false;
            }

            return valid;
        }

        // ========== ВЫХОД ==========
        function logout() {
            localStorage.removeItem('username');
            localStorage.removeItem('isAuthenticated');
            localStorage.removeItem('authToken');
            checkAuthState();
        }

        // ========== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ==========
        function showError(elementId, message) {
            const element = document.getElementById(elementId);
            element.textContent = message;
            element.style.display = 'block';
        }

        function showSuccess(elementId, message) {
            const element = document.getElementById(elementId);
            element.textContent = message;
            element.style.display = 'block';
        }

        function clearErrors(modalType) {
            const modal = document.getElementById(modalType + 'Modal');
            const elements = modal.querySelectorAll('.error-message, .success-message');
            elements.forEach(element => {
                element.style.display = 'none';
                element.textContent = '';
            });
        }

        // ========== УПРАВЛЕНИЕ МОДАЛЬНЫМИ ОКНАМИ ==========
        function openSettings() {
            document.getElementById('settingsModal').style.display = 'flex';
        }

        function closeSettings() {
            document.getElementById('settingsModal').style.display = 'none';
        }

        function openLoginModal() {
            clearErrors('login');
            document.getElementById('loginModal').style.display = 'flex';
        }

        function closeLoginModal() {
            document.getElementById('loginModal').style.display = 'none';
            clearErrors('login');
        }

        function openRegisterModal() {
            clearErrors('register');
            document.getElementById('registerModal').style.display = 'flex';
        }

        function closeRegisterModal() {
            document.getElementById('registerModal').style.display = 'none';
            clearErrors('register');
        }

        // ========== ТЕМА ==========
        function applyTheme(theme) {
            // Удаляем старые классы темы
            document.body.classList.remove('light-theme', 'dark-theme');

            // Добавляем новый класс
            if (theme === 'dark') {
                document.body.classList.add('dark-theme');
                document.getElementById('currentTheme').textContent = 'Тёмная';
            } else {
                document.body.classList.add('light-theme');
                document.getElementById('currentTheme').textContent = 'Светлая';
            }

            // Сохраняем в localStorage
            localStorage.setItem('theme', theme);

            // Также сохраняем на сервере для других страниц
            saveThemeToServer(theme);
        }

        async function saveThemeToServer(theme) {
            try {
                const userId = localStorage.getItem('userId');
                if (!userId) return;

                await fetch(contextPath + '/api/users/' + userId, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        ...getAuthHeaders()
                    },
                    body: JSON.stringify({
                        theme: theme
                    })
                });
            } catch (error) {
                console.log('Не удалось сохранить тему на сервере:', error);
            }
        }

        // ========== НАСТРОЙКИ ==========
        function saveSettings() {
            const factoryType = document.getElementById('factoryType').value;
            const theme = document.getElementById('themeSelect').value;

            // Сохраняем настройки
            localStorage.setItem('factoryType', factoryType);
            localStorage.setItem('theme', theme);

            // Применяем настройки
            updateFactoryDisplay(factoryType);
            applyTheme(theme);

            // Сохраняем на сервере
            fetch(contextPath + '/ui/settings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'factoryType=' + encodeURIComponent(factoryType) +
                      '&theme=' + encodeURIComponent(theme)
            }).catch(console.error);

            closeSettings();
        }

        // Обновите функцию loadSettings():
        function loadSettings() {
            const factoryType = localStorage.getItem('factoryType') || 'array';
            const theme = localStorage.getItem('theme') || 'light';

            // Устанавливаем значения в селекты
            document.getElementById('factoryType').value = factoryType;
            document.getElementById('themeSelect').value = theme;

            // Применяем настройки
            updateFactoryDisplay(factoryType);
            applyTheme(theme);
        }


        // Обновите функцию updateFactoryDisplay():
        function updateFactoryDisplay(factoryType) {
            const displayElement = document.getElementById('currentFactory');
            displayElement.textContent = factoryType === 'array'
                ? 'Массив (ArrayTabulatedFunctionFactory)'
                : 'Связный список (LinkedListTabulatedFunctionFactory)';
        }

        // ========== НАВИГАЦИЯ ==========
        function openOperationWindow() {
            window.location.href = contextPath + '/ui/operations';
        }

        function openDifferentiationWindow() {
            window.location.href = contextPath + '/ui/differentiation';
        }

        function openStudyWindow() {
            window.location.href = contextPath + '/ui/study';
        }

        // ========== ОБРАБОТЧИКИ СОБЫТИЙ ==========
        window.onclick = function(event) {
            const modals = ['settingsModal', 'loginModal', 'registerModal'];
            modals.forEach(modalId => {
                const modal = document.getElementById(modalId);
                if (event.target === modal) {
                    if (modalId === 'settingsModal') closeSettings();
                    if (modalId === 'loginModal') closeLoginModal();
                    if (modalId === 'registerModal') closeRegisterModal();
                }
            });
        };

        document.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                if (document.getElementById('loginModal').style.display === 'flex') {
                    performLogin();
                }
                if (document.getElementById('registerModal').style.display === 'flex') {
                    performRegister();
                }
            }
        });

        // Утилиты для других страниц
        window.authUtils = {
            getAuthToken: function() {
                return localStorage.getItem('authToken');
            },
            getCurrentUser: function() {
                return localStorage.getItem('username');
            },
            isAuthenticated: function() {
                return localStorage.getItem('isAuthenticated') === 'true';
            },
            makeAuthenticatedRequest: async function(url, options = {}) {
                const authToken = this.getAuthToken();
                if (!authToken) {
                    throw new Error('Not authenticated');
                }

                return fetch(url, {
                    ...options,
                    headers: {
                        'Authorization': authToken,
                        'Content-Type': 'application/json',
                        ...options.headers
                    }
                });
            },
            logout: logout
        };
    </script>
</body>
</html>