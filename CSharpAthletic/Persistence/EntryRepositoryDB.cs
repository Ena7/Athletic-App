using System;
using System.Collections.Generic;
using System.Data;
using Domain;
using Domain.DTO;
using Persistence.DB;

namespace Persistence
{
    public class EntryRepositoryDB : IEntryRepository
    {
        public EntryRepositoryDB()
        {
        }

        public IEnumerable<Entry> GetAll()
        {
            return null;
        }

        public Entry GetById(long id)
        {
            return null;
        }

        public IEnumerable<EventCountDTO> GetEventsNumber()
        {
            IDbConnection con = DBUtils.getConnection();
            IList<EventCountDTO> events = new List<EventCountDTO>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "SELECT Event, AgeGroup, COUNT(*) AS Number FROM (SELECT Event1 AS Event, AgeGroup FROM Entry UNION ALL SELECT Event2, AgeGroup FROM Entry WHERE Event2 != 'NONE') GROUP BY Event, AgeGroup;";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        Event Eventt = (Event)Enum.Parse(typeof(Event), dataR.GetString(0));
                        AgeGroup ageGroup = (AgeGroup)Enum.Parse(typeof(AgeGroup), dataR.GetString(1));
                        long Count = dataR.GetInt64(2);
                        EventCountDTO eventt = new EventCountDTO { Event = Eventt, AgeGroup = ageGroup, Count = Count };
                        events.Add(eventt);
                    }
                }
            }
            return events;
        }

        public long Save(Entry entry)
        {
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "INSERT INTO Entry (IDChild, Event1, Event2, AgeGroup) VALUES (@idChild, @event1, @event2, @ageGroup);";
                IDbDataParameter paramIdChild = comm.CreateParameter();
                paramIdChild.ParameterName = "@idChild";
                paramIdChild.Value = entry.Child.Id.ToString();
                comm.Parameters.Add(paramIdChild);

                IDbDataParameter paramEvent1 = comm.CreateParameter();
                paramEvent1.ParameterName = "@event1";
                paramEvent1.Value = entry.Event1.ToString();
                comm.Parameters.Add(paramEvent1);

                IDbDataParameter paramEvent2 = comm.CreateParameter();
                paramEvent2.ParameterName = "@event2";
                paramEvent2.Value = entry.Event2.ToString();
                comm.Parameters.Add(paramEvent2);

                IDbDataParameter paramAgeGroup = comm.CreateParameter();
                paramAgeGroup.ParameterName = "@ageGroup";
                paramAgeGroup.Value = entry.AgeGroup.ToString();
                comm.Parameters.Add(paramAgeGroup);

                var result = comm.ExecuteNonQuery();
                if (result == 0)
                    throw new Exception("No task added!");

                return 0;
            }
        }
    }
}
