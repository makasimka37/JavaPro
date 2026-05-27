package repository;

import database.DatabaseContext;
import model.Participant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantRepository {

    // 1. Знайти всіх учасників конкретного заходу (для детальної сторінки заходу)
    public List<Participant> findByEventId(Long eventId) {
        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT * FROM participants WHERE event_id = ?";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Participant participant = new Participant(
                            rs.getLong("id"),
                            rs.getLong("event_id"),
                            rs.getString("student_name"),
                            rs.getString("student_email")
                    );
                    participants.add(participant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    // 2. Зберегти нового учасника в базу (реєстрація на захід)
    public void save(Participant participant) {
        String sql = "INSERT INTO participants (event_id, student_name, student_email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, participant.getEventId());
            ps.setString(2, participant.getStudentName());
            ps.setString(3, participant.getStudentEmail());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}