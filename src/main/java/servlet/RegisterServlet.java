package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Participant;
import repository.EventRepository;
import repository.ParticipantRepository;
import service.EventService;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    // Ініціалізуємо сервіс та передаємо йому потрібні репозиторії
    private final EventService eventService = new EventService(
            new EventRepository(),
            new ParticipantRepository()
    );

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Витягуємо дані, які студент ввів у форму на сайті
        Long eventId = Long.parseLong(request.getParameter("eventId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        // 2. Створюємо об'єкт учасника
        Participant participant = new Participant(null, eventId, name, email);

        // 3. Викликаємо нашу бізнес-логіку
        boolean success = eventService.registerParticipant(participant);

        if (success) {
            // Якщо місця були і реєстрація пройшла — повертаємося на головну сторінку з успіхом
            response.sendRedirect(request.getContextPath() + "/events?success=true");
        } else {
            // Якщо місць немає — перенаправляємо з помилкою
            response.sendRedirect(request.getContextPath() + "/events?error=no_seats");
        }
    }
}