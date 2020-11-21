package com.example.GuideServer;

public class Event {
    private String eventName;
    private String eventDate;
    private String eventDescription;
    private String eventPlace;
    private int id;



    public Event(String eventName, String eventDate, String eventDescription, String eventPlace, int id) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventPlace = eventPlace;
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
