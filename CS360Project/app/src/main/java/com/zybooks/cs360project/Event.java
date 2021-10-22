package com.zybooks.cs360project;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Event {

    private int id;
    private String title;
    private Calendar date;
    private String description;
    private String eventUsername;

    public Event() { }

    public Event(String newTitle, Calendar newDate, String newDescription, String newEventUsername) {
        title = newTitle;
        date = newDate;
        description = newDescription;
        eventUsername = newEventUsername;
    }

    public Event(int newId, String newTitle, Calendar newDate, String newDescription, String newEventUsername) {
        id = newId;
        title = newTitle;
        date = newDate;
        description = newDescription;
        eventUsername = newEventUsername;
    }

    public String getFormattedDate() {
        SimpleDateFormat formatted = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return formatted.format(date.getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventusername() {
        return eventUsername;
    }

    public void setEventusername(String eventusername) {
        this.eventUsername = eventusername;
    }
}
