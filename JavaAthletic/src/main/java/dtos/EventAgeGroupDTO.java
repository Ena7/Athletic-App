package dtos;

import domain.AgeGroup;
import domain.Event;

import java.io.Serializable;

public class EventAgeGroupDTO implements Serializable {
    private final Event event;
    private final AgeGroup ageGroup;

    public EventAgeGroupDTO(Event event, AgeGroup ageGroup) {
        this.event = event;
        this.ageGroup = ageGroup;
    }

    public Event getEvent() {
        return event;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }
}
