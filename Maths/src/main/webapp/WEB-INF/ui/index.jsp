<%-- src/main/webapp/WEB-INF/ui/index.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Создание табулированных функций</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f2f5;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
        }

        header {
            text-align: center;
            margin-bottom: 40px;
        }

        h1 {
            color: #333;
        }

        .methods {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .method-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
            text-decoration: none;
            color: inherit;
        }

        .method-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0,0,0,0.15);
        }

        .method-card h2 {
            color: #2196F3;
            margin-top: 0;
        }

        .method-card p {
            color: #666;
            line-height: 1.6;
        }

        .method-icon {
            font-size: 48px;
            text-align: center;
            margin-bottom: 20px;
            color: #2196F3;
        }

        footer {
            text-align: center;
            margin-top: 40px;
            color: #888;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Создание табулированных функций</h1>
            <p>Выберите способ создания функции</p>
        </header>

        <div class="methods">
            <a href="${pageContext.request.contextPath}/ui/functions/create-from-arrays" class="method-card">
                <div class="method-icon">📊</div>
                <h2>Из массивов значений</h2>
                <p>Создание функции путем ввода значений X и Y вручную. Подходит для произвольных данных.</p>
            </a>

            <a href="${pageContext.request.contextPath}/ui/functions/create-from-function" class="method-card">
                <div class="method-icon">📈</div>
                <h2>Из математической функции</h2>
                <p>Создание функции путем табуляции выбранной математической функции на указанном интервале.</p>
            </a>
        </div>

        <footer>
            <p>Табулированные функции &copy; 2025</p>
        </footer>
    </div>
</body>
</html>