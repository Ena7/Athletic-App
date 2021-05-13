import dtos.EventCountDTO;

import java.rmi.Remote;
import java.util.List;

public interface IObserver extends Remote {
    void refreshEvents(List<EventCountDTO> eventCountList) throws Exception;
}
