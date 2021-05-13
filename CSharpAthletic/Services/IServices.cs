using System.Collections.Generic;
using Domain;
using Domain.DTO;

namespace Services
{
    public interface IServices
    {
        void Login(User user, IObserver client);
        void Logout(User user, IObserver client);
        IEnumerable<EventCountDTO> GetEventsNumber();
        IEnumerable<ChildNoEventsDTO> GetChildrenNoEvents(Event e, AgeGroup a);
        void AddParticipant(string name, int age, Event event1, Event event2);
    }
}
