package cr.ac.itcr.datebook.domain;

import java.util.Date;

/**
 * Clase que representa al evento
 */

public class Event {

    private int id;
    private Date date;
    private int userId;
    private String place;
    private String name;

    public Event(int id, Date date, int userId, String place, String name) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.place = place;
        this.name = name;
    }

    public Event(Date date, int userId, String place, String name) {
        this.date = date;
        this.userId = userId;
        this.place = place;
        this.name = name;
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    public String getPlace() {
        return place;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "\n" + place + "\n" + date.toString();
    }
}
