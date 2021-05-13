package rest_domain;

import domain.Entity;

public class Competition extends Entity<Long> {
    private String event;
    private String ageGroup;

    public Competition() {}

    public Competition(String event, String ageGroup) {
        this.event = event;
        this.ageGroup = ageGroup;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    @Override
    public String toString() {
        return "ID: " + this.getId() + " | Event: " + this.getEvent() + " | Age Group: " + this.getAgeGroup();
    }

}
