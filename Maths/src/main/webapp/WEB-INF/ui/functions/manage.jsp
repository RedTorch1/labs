<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Мои функции</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        /* Весь CSS остается */
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
        .btn:hover{opacity:0.9;transform:translateY(-1px);}
        .empty-state{text-align:center;padding:80px 20px;color:#666;}
        .modal{display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.8);z-index:1000;justify-content:center;align-items:center;}
        .modal-content{background:white;max-width:90%;max-height:90%;border-radius:10px;overflow:hidden;}
        .modal-header{padding:20px;background:#2196F3;color:white;position:relative;}
        .close{position:absolute;top:15px;right:20px;font-size:30px;cursor:pointer;color:white;}
        .modal-body{padding:20px;max-height:70vh;overflow:auto;}
        canvas{max-width:100%;height:400px;}
        .loading{display:none;text-align:center;padding:20px;color:#666;}
        .success-message{background:#4CAF50;color:white;padding:10px;border-radius:5px;margin:10px 0;}
    </style>
</head>
<body>
<div class="container">
    <a href="/ui/main" class="back-btn">← Главное меню</a>
    <h1>📈 Мои функции (<%=request.getAttribute("functions") != null ? ((java.util.List)request.getAttribute("functions")).size() : 0%>)</h1>

    <div id="loading" class="loading">🔄 Обновление...</div>
    <div id="successMsg" class="success-message" style="display:none;"></div>

    <%
        java.util.List functions = (java.util.List)request.getAttribute("functions");
        if (functions != null && functions.size() > 0) {
    %>
    <table class="functions-table" id="functionsTable">
        <thead>
            <tr><th>ID</th><th>Название</th><th>Точек</th><th>Интервал</th><th>Действия</th></tr>
        </thead>
        <tbody id="functionsBody">
        <%
            for (int i = 0; i < functions.size(); i++) {
        %>
            <tr data-id="<%= i + 1 %>">
                <td><%= i + 1 %></td>
                <td contenteditable="true" class="function-name"><%= functions.get(i) != null ? functions.get(i).getClass().getSimpleName() : "Неизвестно" %></td>
                <td>5</td>
                <td>[0.0, 4.0]</td>
                <td>
                    <button class="btn btn-primary" onclick="showChart(<%= i %>)">📊</button>
                    <button class="btn btn-success edit-btn" onclick="toggleEdit(<%= i + 1 %>)">✏️</button>
                    <button class="btn btn-success save-btn" onclick="saveEdit(<%= i + 1 %>)" style="display:none;">💾</button>
                    <button class="btn btn-danger" onclick="deleteFunction(<%= i + 1 %>)">🗑️</button>
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

<!-- МОДАЛЬНОЕ ОКНО ГРАФИКА (остается) -->
<div id="chartModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <span class="close" onclick="closeChart()">&times;</span>
            <h2 id="chartTitle">График функции</h2>
        </div>
        <div class="modal-body">
            <canvas id="functionChart"></canvas>
        </div>
    </div>
</div>

<script>
const contextPath = '<%= request.getContextPath() %>';

// ✅ ДАННЫЕ ФУНКЦИЙ (остаются)
const functionData = [
    {name: "x²", labels: ['0','1','2','3','4'], data: [0,1,4,9,16], color: '#2196F3'},
    {name: "sin(x)", labels: ['0.0','0.4','0.8','1.2','1.6','2.0','2.4','2.8','3.2','3.6','4.0'],
     data: [0.00,0.39,0.72,0.93,0.99,0.91,0.68,0.33,-0.06,-0.44,-0.76], color: '#4CAF50'}
];

let chartInstance = null;

// ✅ ГРАФИК (остается)
function showChart(index) {
    const data = functionData[index];
    document.getElementById('chartTitle').textContent = data.name;
    document.getElementById('chartModal').style.display = 'flex';

    const ctx = document.getElementById('functionChart').getContext('2d');
    if (chartInstance) chartInstance.destroy();

    chartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.labels,
            datasets: [{
                label: data.name, data: data.data, borderColor: data.color,
                backgroundColor: data.color + '20', borderWidth: 3, fill: false,
                tension: 0.4, pointRadius: 5, pointHoverRadius: 8
            }]
        },
        options: {
            responsive: true, maintainAspectRatio: false,
            plugins: { legend: { display: false }, title: { display: true, text: 'График функции' }},
            scales: { x: { title: { display: true, text: 'x' } }, y: { title: { display: true, text: 'f(x)' } } }
        }
    });
}

function closeChart() {
    document.getElementById('chartModal').style.display = 'none';
    if (chartInstance) { chartInstance.destroy(); chartInstance = null; }
}

// ✅ УДАЛЕНИЕ ФУНКЦИИ
async function deleteFunction(id) {
    if (!confirm(`Удалить функцию #${id}?`)) return;

    showLoading(true);
    try {
        const response = await fetch(`${contextPath}/ui/api/functions/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showSuccess(`Функция #${id} удалена!`);
            setTimeout(() => location.reload(), 1500);
        } else {
            alert('Ошибка удаления');
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Ошибка сети');
    } finally {
        showLoading(false);
    }
}

// ✅ СОХРАНЕНИЕ РЕДАКТИРОВАНИЯ
async function saveEdit(id) {
    const row = document.querySelector(`tr[data-id="${id}"]`);
    const newName = row.querySelector('.function-name').textContent.trim();

    if (!newName) {
        alert('Введите название функции');
        return;
    }

    showLoading(true);
    try {
        const response = await fetch(`${contextPath}/api/functions/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: newName })
        });

        if (response.ok) {
            showSuccess(`Функция #${id} обновлена!`);
        } else {
            alert('Ошибка сохранения');
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Ошибка сети');
    } finally {
        showLoading(false);
    }
}

// ✅ НОВЫЕ ФУНКЦИИ
function toggleEdit(id) {
    const row = document.querySelector(`tr[data-id="${id}"]`);
    const nameCell = row.querySelector('.function-name');
    const editBtn = row.querySelector('.edit-btn');
    const saveBtn = row.querySelector('.save-btn');

    nameCell.contentEditable = true;
    nameCell.classList.add('editing');
    editBtn.style.display = 'none';
    saveBtn.style.display = 'inline-block';
    nameCell.focus();
}

async function saveEdit(id) {
    const row = document.querySelector(`tr[data-id="${id}"]`);
    if (!row) {
        alert('Строка не найдена');
        return;
    }

    const nameCell = row.querySelector('.function-name');
    const newName = nameCell.textContent.trim();

    if (!newName) {
        alert('Введите название функции');
        return;
    }

    showLoading(true);
    try {
        const response = await fetch(`${contextPath}/ui/api/functions/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: newName })
        });

        if (response.ok) {
            showSuccess(`Функция #${id} обновлена!`);
            nameCell.contentEditable = false;
            nameCell.classList.remove('editing');
            row.querySelector('.edit-btn').style.display = 'inline-block';
            row.querySelector('.save-btn').style.add('display', 'none');
        } else {
            alert('Ошибка сохранения');
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Ошибка сети');
    } finally {
        showLoading(false);
    }
}

async function deleteFunction(id) {
    if (!confirm(`Удалить функцию #${id}?`)) return;

    showLoading(true);
    try {
        // ✅ ФИКС ПУТИ!
        const response = await fetch(`${contextPath}/ui/api/functions/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showSuccess(`Функция #${id} удалена!`);
            setTimeout(() => location.reload(), 1500);
        } else {
            alert('Ошибка удаления');
        }
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Ошибка сети');
    } finally {
        showLoading(false);
    }
}

// ✅ ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ
function showLoading(show) {
    document.getElementById('loading').style.display = show ? 'block' : 'none';
}

function showSuccess(message) {
    const msg = document.getElementById('successMsg');
    msg.textContent = message;
    msg.style.display = 'block';
    setTimeout(() => msg.style.display = 'none', 3000);
}

// Закрытие модалок
document.getElementById('chartModal').onclick = e => { if (e.target === this) closeChart(); };
document.onkeydown = e => { if (e.key === 'Escape') closeChart(); };
</script>
</body>
</html>
