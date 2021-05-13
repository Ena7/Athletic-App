package repo;

import domain.AgeGroup;
import domain.Child;
import domain.Event;
import dtos.ChildNoEventsDTO;

import java.util.List;

public interface IChildRepository extends IRepository<Long, Child> {
    List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup);
    List<Child> getUnsignedChildren();
}
