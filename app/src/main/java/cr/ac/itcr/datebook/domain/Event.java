package cr.ac.itcr.datebook.domain;

import java.util.Date;

/**
 * Clase que representa al evento
 */

public class Event {

    private int id;
    private Date date;
    private User user;
    private String place;
    private String name;

    public Event(Date date, User user, String place, String name, int id) {
        this.date = date;
        this.user = user;
        this.place = place;
        this.name = name;
        this.id = id;
    }

    public Event(Date date, User user, String place, String name) {
        this.date = date;
        this.user = user;
        this.place = place;
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public String getPlace() {
        return place;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + user +
                ", place='" + place + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
