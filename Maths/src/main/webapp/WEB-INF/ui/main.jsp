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
            color: #333;
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
            font-size: 14px;
        }

        .settings-btn:hover {
            background-color: #616161;
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

        /* ✅ ССЫЛКИ КАК КНОПКИ (работают как index.jsp) */
        .login-btn, .register-btn, .logout-btn {
            display: inline-block;
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            text-align: center;
        }

        .login-btn {
            background-color: #4CAF50;
            color: white;
        }

        .login-btn:hover {
            background-color: #388E3C;
            transform: translateY(-1px);
        }

        .register-btn {
            background-color: #2196F3;
            color: white;
        }

        .register-btn:hover {
            background-color: #1976D2;
            transform: translateY(-1px);
        }

        .logout-btn {
            background-color: #f44336;
            color: white;
        }

        .logout-btn:hover {
            background-color: #d32f2f;
            transform: translateY(-1px);
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
            color: #333;
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

        .factory-info, .theme-info {
            color: #666;
            margin-bottom: 5px;
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

        .form-group input, .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            color: #333;
        }

        /* ========== ТЕМНАЯ ТЕМА ========== */
        body.dark-theme {
            background-color: #2d2d2d !important;
            color: #f0f0f0 !important;
        }

        .dark-theme .menu-card, .dark-theme .status-bar, .dark-theme .modal-content {
            background-color: #3d3d3d !important;
            border: 1px solid #444 !important;
        }

        .dark-theme .menu-card:hover {
            background-color: #4a4a4a !important;
            box-shadow: 0 6px 12px rgba(0,0,0,0.3) !important;
        }

        .dark-theme header {
            border-bottom: 2px solid #444 !important;
        }

        .dark-theme .user-name, .dark-theme .menu-card h3, .dark-theme .status-bar h4,
        .dark-theme .form-group label, .dark-theme .factory-info, .dark-theme .theme-info {
            color: #f0f0f0 !important;
        }

        .dark-theme .menu-card p {
            color: #bbb !important;
        }

        .dark-theme .form-group input, .dark-theme .form-group select {
            background-color: #2d2d2d !important;
            color: #f0f0f0 !important;
            border: 1px solid #555 !important;
        }

        .dark-theme .close-btn {
            color: #aaa !important;
        }

        .dark-theme .close-btn:hover {
            color: #fff !important;
        }

        /* Темная тема кнопок */
        .dark-theme .settings-btn { background-color: #666 !important; }
        .dark-theme .settings-btn:hover { background-color: #777 !important; }
        .dark-theme .login-btn { background-color: #2e7d32 !important; }
        .dark-theme .register-btn { background-color: #1565c0 !important; }
        .dark-theme .logout-btn { background-color: #c62828 !important; }
    </style>
</head>
<body>
    <div class="container">
        <!-- HEADER -->
        <header>
            <div class="logo">🧮 Табулированные функции</div>
            <div class="auth-buttons">
                <div id="userSection" style="display: none;">
                    <div class="user-info">
                        <span class="user-name" id="userName"></span>
                        <a href="${pageContext.request.contextPath}/api/auth/logout" class="logout-btn">Выйти</a>
                    </div>
                </div>
                <div id="guestSection">
                    <!-- ✅ ПРЯМЫЕ ССЫЛКИ (работают как index.jsp) -->
                    <a href="${pageContext.request.contextPath}/ui/login" class="login-btn">Войти</a>
                    <a href="${pageContext.request.contextPath}/ui/register" class="register-btn">Регистрация</a>
                </div>
                <button class="settings-btn" onclick="openSettings()">⚙️</button>
            </div>
        </header>

        <!-- 6 КАРТОЧЕК МЕНЮ -->
        <div class="main-menu">
            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/operations'">
                <div class="menu-icon">➕</div>
                <h3>Операции с функциями</h3>
                <p>Сложение, вычитание, умножение и деление</p>
            </div>

            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/differentiation'">
                <div class="menu-icon">📐</div>
                <h3>Дифференцирование</h3>
                <p>Численное дифференцирование функции</p>
            </div>

            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/functions/create-from-arrays'">
                <div class="menu-icon">📊</div>
                <h3>Создать из массивов</h3>
                <p>Ручной ввод координат X и Y</p>
            </div>

            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/functions/create-from-function'">
                <div class="menu-icon">📈</div>
                <h3>Создать из функции</h3>
                <p>Табуляция sin(x), x² и др.</p>
            </div>

            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/study'">
                <div class="menu-icon">📊</div>
                <h3>Изучение функций</h3>
                <p>Графическое исследование</p>
            </div>

            <div class="menu-card" onclick="window.location.href='${pageContext.request.contextPath}/ui/functions/manage'">
                <div class="menu-icon">⚙️</div>
                <h3>Управление функциями</h3>
                <p>CRUD своих функций</p>
            </div>
        </div>

        <!-- STATUS BAR -->
        <div class="status-bar">
            <h4>Текущие настройки</h4>
            <div class="factory-info">
                Фабрика: <span id="currentFactory">Массив</span>
            </div>
            <div class="theme-info">
                Тема: <span id="currentTheme">Светлая</span>
            </div>
            <div id="statusMessage" style="margin-top: 10px; padding: 10px; border-radius: 4px; display: none;"></div>
        </div>
    </div>

    <!-- НАСТРОЙКИ МОДАЛКА -->
    <div id="settingsModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>⚙️ Настройки</h3>
                <button class="close-btn" onclick="closeSettings()">×</button>
            </div>

            <div class="form-group">
                <label>Тип фабрики:</label>
                <select id="factoryType">
                    <option value="array">Массив (ArrayTabulatedFunctionFactory)</option>
                    <option value="linkedlist">Связный список (LinkedListTabulatedFunctionFactory)</option>
                </select>
            </div>

            <div class="form-group">
                <label>Тема:</label>
                <select id="themeSelect">
                    <option value="light">Светлая</option>
                    <option value="dark">Тёмная</option>
                </select>
            </div>

            <div style="text-align: right; margin-top: 20px;">
                <button onclick="saveSettings()"
                        style="padding: 10px 20px; background: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Сохранить
                </button>
            </div>
        </div>
    </div>

    <script>
    const contextPath = '${pageContext.request.contextPath}' || '/';

    window.onload = function() {
        console.log('✅ main.jsp загружен');
        loadSettings();
        checkRedirectParams();
        checkAuthState(); // ✅ Теперь работает!
    };

    function checkRedirectParams() {
        const urlParams = new URLSearchParams(window.location.search);
        const status = urlParams.get('status');
        const message = urlParams.get('message');

        if (status && message) {
            showStatusMessage(status, decodeURIComponent(message));
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }

    // ✅ ИСПРАВЛЕННАЯ ПРОВЕРКА ЛОГИНА
    function checkAuthState() {
        const username = localStorage.getItem('username');
        const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';

        console.log('🔍 Auth check:', { username, isAuthenticated });

        if (username && isAuthenticated) {
            showUserSection(username);
        } else {
            showGuestSection();
        }
    }

    function showUserSection(username) {
        document.getElementById('userSection').style.display = 'flex';
        document.getElementById('guestSection').style.display = 'none';
        document.getElementById('userName').textContent = username;
        console.log('✅ Показываем пользователя:', username);
    }

    function showGuestSection() {
        document.getElementById('userSection').style.display = 'none';
        document.getElementById('guestSection').style.display = 'flex';
        console.log('✅ Показываем гостя');
    }

    // Остальные функции без изменений...
    function openSettings() { document.getElementById('settingsModal').style.display = 'flex'; }
    function closeSettings() { document.getElementById('settingsModal').style.display = 'none'; }

    function saveSettings() {
        const factoryType = document.getElementById('factoryType').value;
        const theme = document.getElementById('themeSelect').value;
        localStorage.setItem('factoryType', factoryType);
        localStorage.setItem('theme', theme);
        updateFactoryDisplay(factoryType);
        applyTheme(theme);
        closeSettings();
        showStatusMessage('success', 'Настройки сохранены!');
    }

    function loadSettings() {
        const factoryType = localStorage.getItem('factoryType') || 'array';
        const theme = localStorage.getItem('theme') || 'light';
        document.getElementById('factoryType').value = factoryType;
        document.getElementById('themeSelect').value = theme;
        updateFactoryDisplay(factoryType);
        applyTheme(theme);
    }

    function updateFactoryDisplay(factoryType) {
        document.getElementById('currentFactory').textContent =
            factoryType === 'array' ? 'Массив' : 'Связный список';
    }

    function applyTheme(theme) {
        document.body.classList.remove('light-theme', 'dark-theme');
        if (theme === 'dark') {
            document.body.classList.add('dark-theme');
            document.getElementById('currentTheme').textContent = 'Тёмная';
        } else {
            document.body.classList.add('light-theme');
            document.getElementById('currentTheme').textContent = 'Светлая';
        }
        localStorage.setItem('theme', theme);
    }

    function showStatusMessage(type, text) {
        const msg = document.getElementById('statusMessage');
        msg.textContent = text;
        msg.style.display = 'block';
        if (type === 'success') {
            msg.style.background = '#d4edda';
            msg.style.color = '#155724';
            msg.style.border = '1px solid #c3e6cb';
        } else {
            msg.style.background = '#f8d7da';
            msg.style.color = '#721c24';
            msg.style.border = '1px solid #f5c6cb';
        }
        setTimeout(() => msg.style.display = 'none', 5000);
    }

    function logout() {
        localStorage.removeItem('username');
        localStorage.removeItem('isAuthenticated');
        showGuestSection();
        showStatusMessage('success', 'Выход выполнен');
    }

    // Остальной код...
    window.onclick = function(event) {
        if (event.target.classList.contains('modal')) closeSettings();
    };
    document.addEventListener('keypress', function(event) {
        if (event.key === 'Enter' && document.getElementById('settingsModal').style.display === 'flex') {
            saveSettings();
        }
    });

    window.authUtils = {
        getCurrentUser: () => localStorage.getItem('username'),
        isAuthenticated: () => localStorage.getItem('isAuthenticated') === 'true'
    };
    </script>

</body>
</html>
