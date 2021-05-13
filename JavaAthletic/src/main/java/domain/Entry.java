package domain;

public class Entry extends Entity<Long> {
    private Child child;
    private Event event1;
    private Event event2;
    private AgeGroup ageGroup;


    public Entry(Child child, Event event1, Event event2, AgeGroup ageGroup) {
        this.child = child;
        this.event1 = event1;
        this.event2 = event2;
        this.ageGroup = ageGroup;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Event getEvent1() {
        return event1;
    }

    public void setEvent1(Event event1) {
        this.event1 = event1;
    }

    public Event getEvent2() {
        return event2;
    }

    public void setEvent2(Event event2) {
        this.event2 = event2;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }
}
