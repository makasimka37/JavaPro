<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Event" %>
<!DOCTYPE html>
<html>
<head>
    <title>Дошка оголошень заходів</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f4f6f9; }
        h1, h2 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; background: white; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, select { width: 100%; padding: 8px; box-sizing: border-box; }
        button { background-color: #28a745; color: white; padding: 10px 15px; border: none; cursor: pointer; font-size: 16px; }
        button:hover { background-color: #218838; }
        .alert { padding: 15px; margin-bottom: 20px; border-radius: 4px; }
        .alert-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-danger { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>

    <h1>📅 Дошка оголошень університетських заходів</h1>

    <%-- Повідомлення про статус реєстрації (успіх/помилка) --%>
    <% if ("true".equals(request.getParameter("success"))) { %>
        <div class="alert alert-success">Поздоровляємо! Ви успішно зареєструвалися на захід.</div>
    <% } %>
    <% if ("no_seats".equals(request.getParameter("error"))) { %>
        <div class="alert alert-danger">Помилка реєстрації: На жаль, вільні місця на цей захід закінчилися!</div>
    <% } %>

    <h2>Список доступних заходів</h2>
    <table>
        <thead>
            <tr>
                <th>Назва заходу</th>
                <th>Дата та час проведення</th>
                <th>Всього місць</th>
                <th>Залишилось вільних місць</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Event> events = (List<Event>) request.getAttribute("events");
                if (events != null && !events.isEmpty()) {
                    for (Event event : events) {
            %>
                <tr>
                    <td><%= event.getTitle() %></td>
                    <td><%= event.getEventDate().toString().replace("T", " ") %></td>
                    <td><%= event.getMaxSeats() %></td>
                    <td>
                        <strong><%= event.getFreeSeats() %></strong>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="4" style="text-align: center; color: #777;">Наразі немає запланованих заходів.</td>
                </tr>
            <% } %>
        </tbody>
    </table>

    <hr>

    <h2>📝 Швидка реєстрація учасника</h2>
    <form action="<%= request.getContextPath() %>/register" method="POST">
        <div class="form-group">
            <label for="eventId">Оберіть захід:</label>
            <select name="eventId" id="eventId" required>
                <%
                    if (events != null) {
                        for (Event event : events) {
                %>
                    <option value="<%= event.getId() %>" <%= event.getFreeSeats() <= 0 ? "disabled" : "" %>>
                        <%= event.getTitle() %> (Вільних місць: <%= event.getFreeSeats() %>)
                    </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label for="name">Ваше Ім'я та Прізвище:</label>
            <input type="text" id="name" name="name" required placeholder="Н-р, Іван Франко">
        </div>

        <div class="form-group">
            <label for="email">Електронна пошта (Email):</label>
            <input type="email" id="email" name="email" required placeholder="student@univ.edu.ua">
        </div>

        <button type="submit">Зареєструватися</button>
    </form>

</body>
</html>