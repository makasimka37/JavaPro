package service;

import model.Event;
import model.Participant;
import repository.EventRepository;
import repository.ParticipantRepository;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    @Test
    void shouldReturnFalseWhenNoSeatsAvailable() {
        // 1. Імітуємо репозиторій подій, який нібито повертає захід, де вільних місць уже 0
        EventRepository mockEventRepo = new EventRepository() {
            @Override
            public Event findById(Long id) {
                Event event = new Event(1L, "Тестова лекція", LocalDateTime.now().plusDays(2), 20);
                event.setFreeSeats(0); // Вільних місць немає
                return event;
            }
        };

        // 2. Імітуємо репозиторій учасників, який нічого насправді в базу не пише під час тесту
        ParticipantRepository mockParticipantRepo = new ParticipantRepository() {
            @Override
            public void save(Participant p) {
                // порожній метод для ізольованого тесту
            }
        };

        // 3. Створюємо сервіс і передаємо йому наші "фейкові" репозиторії
        EventService eventService = new EventService(mockEventRepo, mockParticipantRepo);
        Participant student = new Participant(null, 1L, "Олег", "oleg@test.com");

        // 4. Викликаємо бізнес-логику перевірки
        boolean result = eventService.registerParticipant(student);

        // 5. Перевіряємо результат: метод повинен повернути false, бо місць немає
        assertFalse(result, "Реєстрація має повернути false, якщо на заході закінчилися місця");
    }
}