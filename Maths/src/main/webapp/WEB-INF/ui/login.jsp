<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Вход ЛР7</title>
    <style>
        body {
            font-family: Arial;
            padding: 50px;
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .form-container {
            background: rgba(255,255,255,0.1);
            padding: 40px;
            border-radius: 20px;
            backdrop-filter: blur(20px);
            min-width: 400px;
        }
        input {
            width: 100%;
            padding: 15px;
            margin: 10px 0;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            box-sizing: border-box;
            background: rgba(255,255,255,0.2);
            color: white;
        }
        input::placeholder { color: rgba(255,255,255,0.7); }
        button {
            width: 100%;
            padding: 15px;
            background: linear-gradient(45deg, #2196F3, #1976D2);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 18px;
            cursor: pointer;
            margin-top: 20px;
            transition: opacity 0.3s;
        }
        button:hover:not(:disabled) { opacity: 0.9; }
        button:disabled { opacity: 0.6; cursor: not-allowed; }
        .error {
            color: #ff6b6b;
            margin: 10px 0;
            padding: 12px;
            background: rgba(255,107,107,0.3);
            border-radius: 10px;
            display: none;
            border-left: 4px solid #ff4444;
        }
        .success {
            color: #4CAF50;
            margin: 10px 0;
            padding: 12px;
            background: rgba(76,175,80,0.3);
            border-radius: 10px;
            display: none;
            border-left: 4px solid #4CAF50;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: #4CAF50;
            text-decoration: none;
            padding: 10px 20px;
            border: 1px solid #4CAF50;
            border-radius: 8px;
            transition: all 0.3s;
        }
        .back-link:hover {
            background: #4CAF50;
            color: white;
        }
        .loading {
            display: none;
            text-align: center;
            color: rgba(255,255,255,0.8);
            margin-top: 10px;
        }
        .link-row {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1 style="text-align: center; margin-bottom: 30px;">🔐 Вход в систему</h1>

        <form id="loginForm">
            <!-- ✅ AUTOCOMPLETE по стандарту -->
            <input id="username" type="text"
                   placeholder="Логин (например: admin)"
                   autocomplete="username"
                   required>

            <input id="password" type="password"
                   placeholder="Пароль"
                   autocomplete="current-password"
                   required>

            <div id="errorMessage" class="error"></div>
            <div id="successMessage" class="success"></div>
            <div id="loading" class="loading">⏳ Вход...</div>

            <button type="submit" id="submitBtn">Войти</button>
        </form>

        <div class="link-row">
            <a href="${pageContext.request.contextPath}/ui/register" class="back-link">← Регистрация</a>
            <a href="${pageContext.request.contextPath}/ui/main" class="back-link">← Главное меню</a>
        </div>
    </div>

    <script>
        window.onload = function() {
            console.log('✅ login.jsp готов');

            const form = document.getElementById('loginForm');
            const submitBtn = document.getElementById('submitBtn');
            const errorDiv = document.getElementById('errorMessage');
            const successDiv = document.getElementById('successMessage');
            const loadingDiv = document.getElementById('loading');

            const contextPath = '${pageContext.request.contextPath}';
            const returnTo = new URLSearchParams(window.location.search).get('returnTo') || contextPath + '/ui/main';

            form.onsubmit = async function(e) {
                e.preventDefault();
                console.log('✅ Форма входа отправлена');

                const username = document.getElementById('username').value.trim();
                const password = document.getElementById('password').value;

                clearMessages();

                if (!username || !password) {
                    showError('Заполните логин и пароль');
                    return;
                }

                // ✅ ПРОВЕРКА localStorage (tempUser_ из register.jsp)
                const savedPassword = localStorage.getItem('tempUser_' + username);

                submitBtn.disabled = true;
                submitBtn.textContent = 'Вход...';
                loadingDiv.style.display = 'block';

                try {
                    console.log('🔍 Проверка пользователя:', username);

                    if (savedPassword && savedPassword === password) {
                        // ✅ УСПЕШНЫЙ ВХОД
                        console.log('✅ Вход разрешен:', username);

                        // Сохраняем в localStorage для main.jsp
                        localStorage.setItem('username', username);
                        localStorage.setItem('isAuthenticated', 'true');

                        showSuccess('✅ Вход выполнен! Переход в меню...');

                        setTimeout(() => {
                            window.location.href = returnTo +
                                (returnTo.includes('?') ? '&' : '?') +
                                'status=success&message=' +
                                encodeURIComponent('Добро пожаловать, ' + username + '!');
                        }, 1200);

                    } else {
                        // ❌ НЕВЕРНЫЙ ПАРОЛЬ
                        console.log('❌ Неверный пароль для:', username);
                        showError('Неверный логин или пароль');
                    }

                } catch (error) {
                    console.error('❌ Ошибка:', error);
                    showError('Ошибка входа');
                } finally {
                    submitBtn.disabled = false;
                    submitBtn.textContent = 'Войти';
                    loadingDiv.style.display = 'none';
                }
            };

            // ✅ ТЕСТОВЫЕ ДАННЫЕ (admin из UserService)
            console.log('📋 Тестовые данные:');
            console.log('• admin / admin123 (из UserService)');
            console.log('• Зарегистрированные пользователи (localStorage)');

            function showError(msg) {
                errorDiv.textContent = msg;
                errorDiv.style.display = 'block';
                successDiv.style.display = 'none';
            }

            function showSuccess(msg) {
                successDiv.textContent = msg;
                successDiv.style.display = 'block';
                errorDiv.style.display = 'none';
            }

            function clearMessages() {
                errorDiv.style.display = 'none';
                successDiv.style.display = 'none';
                loadingDiv.style.display = 'none';
            }
        };
    </script>
</body>
</html>
