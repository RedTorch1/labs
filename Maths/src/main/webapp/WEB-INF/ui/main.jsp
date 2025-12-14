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

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
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
        </div>
    </div>

    <!-- Модальные окна -->
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

            if (username && isAuthenticated) {
                showUserSection(username);
            } else {
                showGuestSection();
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

        // ========== BASIC AUTH ЛОГИН ==========
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
                // ТОЛЬКО Basic Auth - как работало раньше
                const authHeader = 'Basic ' + btoa(username + ':' + password);

                console.log('Sending Basic Auth request to:', API.LOGIN);

                // ВАЖНО: Ваш AuthServlet.login() использует AuthHelper.authenticate()
                // который читает Basic Auth из заголовка
                // Отправляем POST с Basic Auth и ПУСТЫМ JSON телом
                const response = await fetch(API.LOGIN, {
                    method: 'POST',
                    headers: {
                        'Authorization': authHeader,
                        'Content-Type': 'application/json'
                    },
                    body: '{}' // ПУСТОЕ тело, только Basic Auth в заголовке
                });

                console.log('Response status:', response.status);

                if (response.ok) {
                    // Успешный вход через Basic Auth
                    localStorage.setItem('username', username);
                    localStorage.setItem('isAuthenticated', 'true');
                    localStorage.setItem('authToken', authHeader);

                    showSuccess('loginSuccessMessage', 'Вход выполнен успешно!');

                    setTimeout(() => {
                        closeLoginModal();
                        checkAuthState();
                    }, 1000);
                } else {
                    const errorText = await response.text();
                    console.error('Login failed:', response.status, errorText);

                    if (response.status === 401) {
                        showError('loginGeneralError', 'Неверное имя пользователя или пароль');
                    } else if (response.status === 400) {
                        // Может быть сервер ожидает JSON с username/password
                        // Попробуем отправить JSON с теми же данными
                        await tryBasicAuthWithJson(username, password);
                    } else {
                        showError('loginGeneralError', `Ошибка сервера: ${response.status}`);
                    }
                }
            } catch (error) {
                console.error('Login error:', error);
                showError('loginGeneralError', 'Ошибка сети: ' + error.message);
            } finally {
                loginBtn.textContent = originalText;
                loginBtn.disabled = false;
            }
        }

        // Альтернатива: Basic Auth + JSON body
        async function tryBasicAuthWithJson(username, password) {
            console.log('Trying Basic Auth with JSON body...');

            const authHeader = 'Basic ' + btoa(username + ':' + password);

            const response = await fetch(API.LOGIN, {
                method: 'POST',
                headers: {
                    'Authorization': authHeader,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            console.log('Basic Auth + JSON response:', response.status);

            if (response.ok) {
                localStorage.setItem('username', username);
                localStorage.setItem('isAuthenticated', 'true');
                localStorage.setItem('authToken', authHeader);

                showSuccess('loginSuccessMessage', 'Вход выполнен успешно!');

                setTimeout(() => {
                    closeLoginModal();
                    checkAuthState();
                }, 1000);
                return true;
            }

            return false;
        }

        // ========== РЕГИСТРАЦИЯ (остается JSON) ==========
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
                console.log('Sending registration request...');

                const response = await fetch(API.REGISTER, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: username,
                        password: password,
                        role: 'USER'
                    })
                });

                const responseText = await response.text();
                console.log('Register response:', response.status, responseText);

                if (response.ok) {
                    showSuccess('registerSuccessMessage', 'Регистрация успешна! Теперь войдите в систему.');

                    setTimeout(() => {
                        closeRegisterModal();
                        document.getElementById('loginUsername').value = username;
                        document.getElementById('loginPassword').value = password;
                        openLoginModal();
                    }, 1500);
                } else {
                    if (response.status === 400 || response.status === 409) {
                        showError('registerGeneralError', 'Пользователь с таким именем уже существует');
                    } else {
                        showError('registerGeneralError', `Ошибка регистрации: ${response.status}`);
                    }
                }
            } catch (error) {
                console.error('Register error:', error);
                showError('registerGeneralError', 'Ошибка сети: ' + error.message);
            } finally {
                registerBtn.textContent = originalText;
                registerBtn.disabled = false;
            }
        }

        // ========== ВЫХОД ==========
        function logout() {
            localStorage.removeItem('username');
            localStorage.removeItem('isAuthenticated');
            localStorage.removeItem('authToken');
            checkAuthState();
        }

        // ========== ВАЛИДАЦИЯ ==========
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

        // ========== НАСТРОЙКИ ==========
        function saveSettings() {
            const factoryType = document.getElementById('factoryType').value;
            localStorage.setItem('factoryType', factoryType);
            updateFactoryDisplay(factoryType);

            fetch(contextPath + '/ui/settings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'factoryType=' + encodeURIComponent(factoryType)
            }).catch(console.error);

            closeSettings();
        }

        function loadSettings() {
            const factoryType = localStorage.getItem('factoryType') || 'array';
            document.getElementById('factoryType').value = factoryType;
            updateFactoryDisplay(factoryType);
        }

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