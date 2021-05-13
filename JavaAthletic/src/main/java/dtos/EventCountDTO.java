package dtos;

import domain.AgeGroup;
import domain.Event;

import java.io.Serializable;

public class EventCountDTO implements Serializable {
    private Event event;
    private AgeGroup ageGroup;
    private long count;

    public EventCountDTO(Event event, AgeGroup ageGroup, long count){
        this.event = event;
        this.ageGroup = ageGroup;
        this.count = count;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
