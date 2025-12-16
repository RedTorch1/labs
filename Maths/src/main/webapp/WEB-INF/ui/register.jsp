<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Регистрация ЛР7</title>
    <style>
        body { font-family: Arial; padding: 50px; background: linear-gradient(45deg, #667eea, #764ba2); color: white; min-height: 100vh; display: flex; align-items: center; justify-content: center; }
        .form-container { background: rgba(255,255,255,0.1); padding: 40px; border-radius: 20px; backdrop-filter: blur(20px); min-width: 400px; }
        input { width: 100%; padding: 15px; margin: 10px 0; border: none; border-radius: 10px; font-size: 16px; box-sizing: border-box; background: rgba(255,255,255,0.2); color: white; }
        input::placeholder { color: rgba(255,255,255,0.7); }
        button { width: 100%; padding: 15px; background: linear-gradient(45deg, #4CAF50, #45a049); color: white; border: none; border-radius: 10px; font-size: 18px; cursor: pointer; margin-top: 20px; transition: opacity 0.3s; }
        button:hover:not(:disabled) { opacity: 0.9; }
        button:disabled { opacity: 0.6; cursor: not-allowed; }
        .error { color: #ff6b6b; margin: 10px 0; padding: 12px; background: rgba(255,107,107,0.3); border-radius: 10px; display: none; border-left: 4px solid #ff4444; }
        .success { color: #4CAF50; margin: 10px 0; padding: 12px; background: rgba(76,175,80,0.3); border-radius: 10px; display: none; border-left: 4px solid #4CAF50; }
        .back-link { display: inline-block; margin-top: 20px; color: #4CAF50; text-decoration: none; padding: 10px 20px; border: 1px solid #4CAF50; border-radius: 8px; transition: all 0.3s; }
        .back-link:hover { background: #4CAF50; color: white; }
        .loading { display: none; text-align: center; color: rgba(255,255,255,0.8); margin-top: 10px; }
    </style>
</head>
<body>
    <div class="form-container">
        <h1 style="text-align: center; margin-bottom: 30px;">📝 Регистрация</h1>

        <form id="registerForm">
            <input id="username" type="text"
                   placeholder="Логин (например: admin)"
                   autocomplete="username"
                   required minlength="3">

            <!-- ✅ AUTOCOMPLETE по стандарту -->
            <input id="password" type="password"
                   placeholder="Пароль (6+ символов)"
                   autocomplete="new-password"
                   required minlength="6">

            <input id="passwordConfirm" type="password"
                   placeholder="Повторите пароль"
                   autocomplete="new-password"
                   required>

            <div id="errorMessage" class="error"></div>
            <div id="successMessage" class="success"></div>
            <div id="loading" class="loading">⏳ Регистрация...</div>

            <button type="submit" id="submitBtn">Зарегистрироваться</button>
        </form>

        <div style="text-align: center; margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/ui/login" class="back-link">← Войти</a> |
            <a href="${pageContext.request.contextPath}/ui/main" class="back-link">← Главное меню</a>
        </div>
    </div>

    <script>
        window.onload = function() {
            console.log('✅ register.jsp готов');

            const form = document.getElementById('registerForm');
            const submitBtn = document.getElementById('submitBtn');
            const errorDiv = document.getElementById('errorMessage');
            const successDiv = document.getElementById('successMessage');
            const loadingDiv = document.getElementById('loading');

            const contextPath = '${pageContext.request.contextPath}';
            const returnTo = new URLSearchParams(window.location.search).get('returnTo') || contextPath + '/ui/main';

            form.onsubmit = async function(e) {
                e.preventDefault();
                console.log('✅ Форма отправлена');

                const username = document.getElementById('username').value.trim();
                const password = document.getElementById('password').value;
                const passwordConfirm = document.getElementById('passwordConfirm').value;

                clearMessages();

                if (!username || username.length < 3) {
                    showError('Логин минимум 3 символа');
                    return;
                }
                if (!password || password.length < 6) {
                    showError('Пароль минимум 6 символов');
                    return;
                }
                if (password !== passwordConfirm) {
                    showError('Пароли не совпадают');
                    return;
                }

                // ✅ UI состояния
                submitBtn.disabled = true;
                submitBtn.textContent = 'Регистрация...';
                loadingDiv.style.display = 'block';

                try {
                    // ✅ Простая регистрация БЕЗ API (временное решение)
                    console.log('✅ Регистрация:', username);

                    // Имитация сохранения (пока нет UserServlet)
                    localStorage.setItem('tempUser_' + username, password);

                    showSuccess('✅ Регистрация успешна!');
                    setTimeout(() => {
                        window.location.href = returnTo +
                            (returnTo.includes('?') ? '&' : '?') +
                            'status=success&message=' +
                            encodeURIComponent('Пользователь "' + username + '" создан!');
                    }, 1500);

                } catch (error) {
                    console.error('❌ Ошибка:', error);
                    showError('Ошибка регистрации');
                } finally {
                    submitBtn.disabled = false;
                    submitBtn.textContent = 'Зарегистрироваться';
                    loadingDiv.style.display = 'none';
                }
            };

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
