<%-- src/main/webapp/WEB-INF/ui/differentiation.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Дифференцирование функции</title>
    <meta charset="UTF-8">
    <style>
        /* Стили аналогичные operations.jsp, но с другой структурой */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f2f5;
        }

        .container {
            max-width: 1400px;
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

        .back-btn {
            padding: 8px 16px;
            background-color: #757575;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .differentiation-container {
            display: grid;
            grid-template-columns: 1fr auto 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }

        .function-panel {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .function-panel h3 {
            margin-top: 0;
            color: #333;
            text-align: center;
        }

        .controls {
            margin-bottom: 15px;
            text-align: center;
        }

        .controls button {
            margin: 0 5px;
            padding: 8px 16px;
            background-color: #2196F3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .function-table {
            max-height: 400px;
            overflow-y: auto;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: center;
        }

        .operation-panel {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            gap: 20px;
        }

        .differentiate-btn {
            padding: 15px 30px;
            background-color: #FF9800;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
        }

        .differentiate-btn:hover {
            background-color: #F57C00;
        }

        .arrow {
            font-size: 36px;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Дифференцирование табулированной функции</h1>
            <a href="${pageContext.request.contextPath}/ui/main" class="back-btn">На главную</a>
        </header>

        <div class="differentiation-container">
            <!-- Исходная функция -->
            <div class="function-panel">
                <h3>Исходная функция</h3>
                <div class="controls">
                    <button onclick="createFunction()">Создать</button>
                    <button onclick="loadFunction()">Загрузить</button>
                    <button onclick="saveFunction()">Сохранить</button>
                </div>
                <div id="sourceTable" class="function-table">
                    <div class="no-data">Функция не загружена</div>
                </div>
            </div>

            <!-- Операция -->
            <div class="operation-panel">
                <div class="arrow">→</div>
                <button class="differentiate-btn" onclick="differentiate()">
                    Дифференцировать
                </button>
                <div class="arrow">→</div>
            </div>

            <!-- Результат -->
            <div class="function-panel">
                <h3>Производная</h3>
                <div class="controls">
                    <button onclick="saveResult()">Сохранить результат</button>
                </div>
                <div id="resultTable" class="function-table">
                    <div class="no-data">Результат дифференцирования отобразится здесь</div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const contextPath = '<%= request.getContextPath() %>';
        let sourceFunctionData = null;
        let resultFunctionData = null;

        function createFunction() {
            // Аналогично operations.jsp
            const url = contextPath + '/ui/functions/create-from-arrays?returnTo=differentiation';
            window.open(url, 'createWindow', 'width=900,height=700,resizable=yes,scrollbars=yes');
        }

        function loadFunction() {
            // Аналогично operations.jsp
            const input = document.createElement('input');
            input.type = 'file';
            input.accept = '.dat,.txt';
            input.onchange = function(event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        try {
                            const functionData = JSON.parse(e.target.result);
                            setSourceFunctionData(functionData);
                        } catch (error) {
                            alert('Ошибка загрузки файла: ' + error.message);
                        }
                    };
                    reader.readAsText(file);
                }
            };
            input.click();
        }

        function saveFunction() {
            if (!sourceFunctionData) {
                alert('Нет данных для сохранения');
                return;
            }

            const blob = new Blob([JSON.stringify(sourceFunctionData)], {type: 'application/json'});
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'source_function.dat';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }

        function saveResult() {
            if (!resultFunctionData) {
                alert('Нет результата для сохранения');
                return;
            }

            const blob = new Blob([JSON.stringify(resultFunctionData)], {type: 'application/json'});
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'derivative_function.dat';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }

        function setSourceFunctionData(data) {
            sourceFunctionData = data;
            renderFunctionTable('sourceTable', data, true);
        }

        function renderFunctionTable(containerId, data, editable) {
            const container = document.getElementById(containerId);

            if (!data || !data.xValues || !data.yValues || data.xValues.length === 0) {
                container.innerHTML = '<div class="no-data">Функция не загружена</div>';
                return;
            }

            let tableHTML = '<table>';
            tableHTML += '<thead><tr><th>№</th><th>X</th><th>Y</th></tr></thead>';
            tableHTML += '<tbody>';

            for (let i = 0; i < data.xValues.length; i++) {
                tableHTML += '<tr>';
                tableHTML += '<td>' + (i + 1) + '</td>';
                tableHTML += '<td class="x-column">' + data.xValues[i] + '</td>';

                if (editable) {
                    tableHTML += '<td class="editable">';
                    tableHTML += '<input type="number" value="' + data.yValues[i] + '" ';
                    tableHTML += 'onchange="updateYValue(' + i + ', this.value)" ';
                    tableHTML += 'step="any" style="width: 100%; border: none; text-align: center;">';
                    tableHTML += '</td>';
                } else {
                    tableHTML += '<td>' + data.yValues[i] + '</td>';
                }

                tableHTML += '</tr>';
            }

            tableHTML += '</tbody></table>';
            container.innerHTML = tableHTML;
        }

        function updateYValue(index, value) {
            if (sourceFunctionData && sourceFunctionData.yValues) {
                sourceFunctionData.yValues[index] = parseFloat(value);
            }
        }

        function differentiate() {
            if (!sourceFunctionData) {
                alert('Загрузите функцию для дифференцирования');
                return;
            }

            if (sourceFunctionData.xValues.length < 2) {
                alert('Для дифференцирования нужно как минимум 2 точки');
                return;
            }

            // Выполняем численное дифференцирование
            const xValues = sourceFunctionData.xValues;
            const yValues = sourceFunctionData.yValues;
            const n = xValues.length;
            const derivativeY = new Array(n);

            // Первая точка (левая разность)
            derivativeY[0] = (yValues[1] - yValues[0]) / (xValues[1] - xValues[0]);

            // Средние точки (центральная разность)
            for (let i = 1; i < n - 1; i++) {
                derivativeY[i] = (yValues[i + 1] - yValues[i - 1]) /
                                 (xValues[i + 1] - xValues[i - 1]);
            }

            // Последняя точка (правая разность)
            derivativeY[n - 1] = (yValues[n - 1] - yValues[n - 2]) /
                                 (xValues[n - 1] - xValues[n - 2]);

            // Сохраняем результат
            resultFunctionData = {
                xValues: xValues.slice(),
                yValues: derivativeY,
                operation: 'differentiation'
            };

            // Отображаем результат
            renderFunctionTable('resultTable', resultFunctionData, false);
        }

        // Функция для получения данных из дочернего окна
        window.receiveFunctionData = function(data) {
            setSourceFunctionData(data);
        };
    </script>
</body>
</html>