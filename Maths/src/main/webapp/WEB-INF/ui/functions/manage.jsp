<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Мои функции</title>
    <style>
        body{font-family:Arial;padding:20px;background:#f0f2f5;color:#333;}
        .container{max-width:1200px;margin:0 auto;background:white;padding:30px;border-radius:12px;box-shadow:0 4px 20px rgba(0,0,0,0.1);}
        .back-btn{display:inline-block;padding:12px 24px;background:#757575;color:white;text-decoration:none;border-radius:6px;margin-bottom:20px;}
        h1{text-align:center;color:#333;margin-bottom:30px;}
        .count{color:#2196F3;font-weight:bold;}
        .functions-table{width:100%;border-collapse:collapse;background:white;border-radius:10px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.1);}
        th,td{padding:15px;text-align:left;border-bottom:1px solid #eee;}
        th{background:#f8f9fa;font-weight:bold;color:#333;}
        .btn{padding:8px 16px;border:none;border-radius:5px;cursor:pointer;font-size:14px;color:white;text-decoration:none;display:inline-block;margin-right:5px;}
        .btn-primary{background:#2196F3;}
        .btn-success{background:#4CAF50;}
        .btn-danger{background:#f44336;}
        .empty-state{text-align:center;padding:80px 20px;color:#666;}
    </style>
</head>
<body>
<div class="container">
    <a href="/ui/main" class="back-btn">← Главное меню</a>
    <h1>📈 Мои функции (<%=request.getAttribute("functions") != null ? ((java.util.List)request.getAttribute("functions")).size() : 0%>)</h1>

    <%
        java.util.List functions = (java.util.List)request.getAttribute("functions");
        if (functions != null && functions.size() > 0) {
    %>
    <table class="functions-table">
        <thead>
            <tr><th>ID</th><th>Тип</th><th>Точек</th><th>Интервал</th><th>Действия</th></tr>
        </thead>
        <tbody>
        <%
            for (int i = 0; i < functions.size(); i++) {
                Object f = functions.get(i);
        %>
            <tr>
                <td><%= i + 1 %></td>
                <td><strong>ArrayTabulatedFunction</strong></td>
                <td><%= 5 %></td>
                <td>[0.0, 4.0]</td>
                <td>
                    <button class="btn btn-primary" onclick="alert('График #<%= i + 1 %>')">📊</button>
                    <button class="btn btn-success" onclick="alert('Редактировать #<%= i + 1 %>')">✏️</button>
                    <button class="btn btn-danger" onclick="if(confirm('Удалить?')) alert('Удалено!')">🗑️</button>
                </td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
        } else {
    %>
    <div class="empty-state">
        <h3>📭 Нет сохраненных функций</h3>
        <p>Создайте первую функцию через <a href="/ui/main" style="color:#2196F3;">главное меню</a></p>
    </div>
    <%
        }
    %>
</div>
</body>
</html>
