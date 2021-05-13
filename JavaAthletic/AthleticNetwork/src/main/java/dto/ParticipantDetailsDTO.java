package dto;

import domain.Event;

import java.io.Serializable;

public class ParticipantDetailsDTO implements Serializable {
    private final String name;
    private final int age;
    private final Event event1;
    private final Event event2;

    public ParticipantDetailsDTO(String name, int age, Event event1, Event event2) {
        this.name = name;
        this.age = age;
        this.event1 = event1;
        this.event2 = event2;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Event getEvent1() {
        return event1;
    }

    public Event getEvent2() {
        return event2;
    }
}