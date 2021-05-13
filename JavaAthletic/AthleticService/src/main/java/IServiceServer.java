import domain.AgeGroup;
import domain.Event;
import domain.User;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;

import java.util.List;

public interface IServiceServer {
    void login(User input_user, IObserver client) throws Exception;
    void logout(User user, IObserver client) throws Exception;
    List<EventCountDTO> getEventsNumber() throws Exception;
    List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup) throws Exception;
    void addParticipant(String name, int age, Event event1, Event event2) throws Exception;
}
