using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Domain;
using Domain.DTO;
using Persistence;
using Services;

namespace Server
{
    public class ServerImplementation : IServices
    {
        private readonly IUserRepository UserRepo;
        private readonly IChildRepository ChildRepo;
        private readonly IEntryRepository EntryRepo;
        private readonly IDictionary<string, IObserver> LoggedClients;

        public ServerImplementation(IUserRepository ur, IChildRepository cr, IEntryRepository er)
        {
            UserRepo = ur;
            ChildRepo = cr;
            EntryRepo = er;
            LoggedClients = new Dictionary<string, IObserver>();
        }

        private (int, int) ParseAgeGroup(Domain.AgeGroup ageGroup)
        {
            string ageGroupString = ageGroup.ToString();
            string[] ageBorders = ageGroupString.Split('A', '_', 'Y');
            return (int.Parse(ageBorders[1]), int.Parse(ageBorders[2]));
        }

        private void NotifyEvents()
        {
            foreach (IObserver client in LoggedClients.Values)
            {
                Task.Run(() => client.RefreshEvents(GetEventsNumber()));
            }
        }

        public IEnumerable<Domain.DTO.EventCountDTO> GetEventsNumber()
        {
            return EntryRepo.GetEventsNumber();
        }

        public void AddParticipant(string name, int age, Domain.Event event1, Domain.Event event2)
        {
            Domain.AgeGroup? ageGroup = null;
            foreach (Domain.AgeGroup ag in Enum.GetValues(typeof(Domain.AgeGroup)))
            {
                (int, int) ageBorders = ParseAgeGroup(ag);
                if (age >= ageBorders.Item1 && age <= ageBorders.Item2)
                {
                    ageGroup = ag;
                    break;
                }
            }
            if (ageGroup == null)
            {
                throw new Exception("Invalid age group");
            }

            Domain.Child child = new Domain.Child { Name = name, Age = age };
            child.Id = ChildRepo.Save(child);
            EntryRepo.Save(new Entry { Child = child, Event1 = event1, Event2 = event2, AgeGroup = ageGroup.Value });

            NotifyEvents();
        }


        public IEnumerable<Domain.DTO.ChildNoEventsDTO> GetChildrenNoEvents(Domain.Event e, Domain.AgeGroup a)
        {
            return ChildRepo.GetChildrenNoEvents(e, a);
        }

        public void Login(Domain.User inputUser, IObserver client)
        {
            Domain.User user = UserRepo.GetByUsername(inputUser.Username);
            if (user == null)
            {
                throw new Exception("Authentication failed.");
            }
            if (user.Password != inputUser.Password)
            {
                throw new Exception("Authentication failed.");
            }
            if (LoggedClients.ContainsKey(user.Username))
            {
                throw new Exception("User already logged in.");
            }
            LoggedClients[user.Username] = client;
        }

        public void Logout(Domain.User user, IObserver client)
        {
            IObserver localClient = LoggedClients[user.Username];
            if (localClient == null)
            {
                throw new Exception("User " + user.Username + " is not logged in.");
            }
            LoggedClients.Remove(user.Username);
        }

    }
}
