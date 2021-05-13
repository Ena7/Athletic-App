package repo;

import domain.Entry;
import dtos.EventCountDTO;

import java.util.List;

public interface IEntryRepository extends IRepository<Long, Entry> {
    List<EventCountDTO> getEventsNumber();
}
