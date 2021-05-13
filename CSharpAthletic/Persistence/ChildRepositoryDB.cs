using System.Collections.Generic;
using System.Data;
using Domain;
using Domain.DTO;
using Persistence.DB;

namespace Persistence
{
    public class ChildRepositoryDB : IChildRepository
    {

        public ChildRepositoryDB()
        {
        }

        public IEnumerable<Child> GetAll()
        {
            return null;
        }

        public Child GetById(long id)
        {
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "SELECT * FROM Child WHERE ID = @id;";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long id2 = dataR.GetInt64(0);
                        string name = dataR.GetString(1);
                        int age = dataR.GetInt32(2);
                        Child child = new Child { Id = id2, Name = name, Age = age };
                        return child;
                    }
                }
            }
            return null;
        }

        public IEnumerable<ChildNoEventsDTO> GetChildrenNoEvents(Event e, AgeGroup a)
        {
            IDbConnection con = DBUtils.getConnection();
            IList<ChildNoEventsDTO> children = new List<ChildNoEventsDTO>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "SELECT C.ID, C.Name, C.Age, CASE WHEN Event2 != 'NONE' THEN 2 ELSE 1 END AS NoEvents FROM Entry E INNER JOIN Child C on C.ID = E.IDChild WHERE (Event1 = @event OR Event2 = @event) AND AgeGroup = @ageGroup;";

                IDbDataParameter paramEvent = comm.CreateParameter();
                paramEvent.ParameterName = "@event";
                paramEvent.Value = e.ToString();
                comm.Parameters.Add(paramEvent);

                IDbDataParameter paramAgeGroup = comm.CreateParameter();
                paramAgeGroup.ParameterName = "@ageGroup";
                paramAgeGroup.Value = a.ToString();
                comm.Parameters.Add(paramAgeGroup);

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt64(0);
                        string name = dataR.GetString(1);
                        int age = dataR.GetInt32(2);
                        int noEvents = dataR.GetInt32(3);
                        Child ch = new Child { Id = id, Name = name, Age = age };
                        ChildNoEventsDTO child = new ChildNoEventsDTO { Child = ch, NoEvents = noEvents };
                        children.Add(child);
                    }
                }
            }
            return children;
        }

        public long Save(Child child)
        {
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "INSERT INTO Child (Name, Age) VALUES (@name, @age); SELECT last_insert_rowid()";
                IDbDataParameter paramName = comm.CreateParameter();
                paramName.ParameterName = "@name";
                paramName.Value = child.Name;
                comm.Parameters.Add(paramName);

                IDbDataParameter paramAge = comm.CreateParameter();
                paramAge.ParameterName = "@age";
                paramAge.Value = child.Age.ToString();
                comm.Parameters.Add(paramAge);

                long IDGenerated = (long)comm.ExecuteScalar();

                return IDGenerated;
            }
        }
    }
}
