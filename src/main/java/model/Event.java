package model;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String title;
    private LocalDateTime eventDate;
    private int maxSeats;
    private int freeSeats; // Додаткове поле для зручності виведення на головній сторінці

    // Конструктор за замовчуванням
    public Event() {
    }

    // Повний конструктор
    public Event(Long id, String title, LocalDateTime eventDate, int maxSeats) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.maxSeats = maxSeats;
    }

    // Геттери та сеттери
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }
    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public int getMaxSeats() {
        return maxSeats;
    }
    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public int getFreeSeats() {
        return freeSeats;
    }
    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }
}