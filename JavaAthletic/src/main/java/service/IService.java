package service;

import domain.AgeGroup;
import domain.Event;
import domain.User;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;

import java.util.List;

public interface IService {
    User checkUserCredentials(String username, String password);
    List<EventCountDTO> getEventsNumber() throws Exception;
    List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup) throws Exception;
    void addParticipant(String name, int age, Event event1, Event event2) throws Exception;
}
