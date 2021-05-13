using System.Collections.Generic;
using Domain.DTO;

namespace Services
{
    public interface IObserver
    {
        void RefreshEvents(IEnumerable<EventCountDTO> eventCountList);
    }
}
