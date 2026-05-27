package repository;

import database.DatabaseContext;
import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {

    // 1. Отримати всі майбутні заходи з підрахунком вільних місць (для головної сторінки)
    public List<Event> findAllFutureEvents() {
        List<Event> events = new ArrayList<>();
        // SQL-запит вираховує вільні місця: max_seats мінус кількість зареєстрованих учасників
        String sql = "SELECT e.*, " +
                "(e.max_seats - COUNT(p.id)) AS free_seats " +
                "FROM events e " +
                "LEFT JOIN participants p ON e.id = p.event_id " +
                "WHERE e.event_date >= NOW() " +
                "GROUP BY e.id " +
                "ORDER BY e.event_date ASC";

        // Використовуємо try-with-resources для безпечного закриття ресурсів
        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getTimestamp("event_date").toLocalDateTime(),
                        rs.getInt("max_seats")
                );
                event.setFreeSeats(rs.getInt("free_seats"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    // 2. Знайти один захід за його ID (потрібно для сервісу перевірки місць та сторінки деталей)
    public Event findById(Long id) {
        String sql = "SELECT e.*, (e.max_seats - COUNT(p.id)) AS free_seats " +
                "FROM events e " +
                "LEFT JOIN participants p ON e.id = p.event_id " +
                "WHERE e.id = ? " +
                "GROUP BY e.id";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id); // Захист від SQL Injection

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Event event = new Event(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getTimestamp("event_date").toLocalDateTime(),
                            rs.getInt("max_seats")
                    );
                    event.setFreeSeats(rs.getInt("free_seats"));
                    return event;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Зберегти новий захід у базу даних (для форми створення)
    public void save(Event event) {
        String sql = "INSERT INTO events (title, event_date, max_seats) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, event.getTitle());
            ps.setTimestamp(2, Timestamp.valueOf(event.getEventDate()));
            ps.setInt(3, event.getMaxSeats());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}