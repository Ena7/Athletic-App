using System.Collections.Generic;
using Domain;
using Domain.DTO;

namespace Persistence
{
    public interface IEntryRepository : IRepository<long, Entry>
    {
        IEnumerable<EventCountDTO> GetEventsNumber();
    }
}
