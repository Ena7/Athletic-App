using System.Collections.Generic;
using Domain;
using Domain.DTO;

namespace Persistence
{
    public interface IChildRepository : IRepository<long, Child>
    {
        IEnumerable<ChildNoEventsDTO> GetChildrenNoEvents(Event e, AgeGroup a);
    }
}
