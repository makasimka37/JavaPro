package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Event;
import repository.EventRepository;

import java.io.IOException;
import java.util.List;

// Аннотація робить цей сервлет доступним за головною адресою сайту
@WebServlet(name = "EventListServlet", urlPatterns = {"", "/events"})
public class EventListServlet extends HttpServlet {

    private final EventRepository eventRepository = new EventRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Забираємо з бази даних список усіх майбутніх подій через репозиторій
        List<Event> events = eventRepository.findAllFutureEvents();

        // 2. Кладемо цей список в атрибути запиту під назвою "events"
        request.setAttribute("events", events);

        // 3. Перенаправляємо користувача на сторінку інтрефейсу list.jsp
        request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
    }
}