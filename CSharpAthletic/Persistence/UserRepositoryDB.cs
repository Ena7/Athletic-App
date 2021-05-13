using System.Collections.Generic;
using System.Data;
using Domain;
using Persistence.DB;

namespace Persistence
{
	public class UserRepositoryDB : IUserRepository
	{
		public UserRepositoryDB()
		{
		}

		public IEnumerable<User> GetAll()
		{
			return null;
		}

		public User GetById(long id)
		{
			return null;
		}

		public User GetByUsername(string username)
		{
			IDbConnection con = DBUtils.getConnection();

			using (var comm = con.CreateCommand())
			{
				comm.CommandText = "SELECT * FROM User WHERE Username = @username;";
				IDbDataParameter paramUsername = comm.CreateParameter();
				paramUsername.ParameterName = "@username";
				paramUsername.Value = username;
				comm.Parameters.Add(paramUsername);

				using (var dataR = comm.ExecuteReader())
				{
					if (dataR.Read())
					{
						string username2 = dataR.GetString(0);
						string password = dataR.GetString(1);
						long id = dataR.GetInt64(2);
						User user = new User { Id = id, Username = username2, Password = password };
						return user;
					}
				}
			}
			return null;
		}

		public long Save(User user)
		{
			return -1;
		}
	}
}
