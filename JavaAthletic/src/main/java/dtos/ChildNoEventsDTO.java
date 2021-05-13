package dtos;

import domain.Child;

import java.io.Serializable;

public class ChildNoEventsDTO implements Serializable {
    private Child child;
    private int noEvents;


    public ChildNoEventsDTO(Child child, int noEvents) {
        this.child = child;
        this.noEvents = noEvents;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public int getNoEvents() {
        return noEvents;
    }

    public void setNoEvents(int noEvents) {
        this.noEvents = noEvents;
    }
}
