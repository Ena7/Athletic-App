using System.Configuration;
using System.Data;
using System.Data.SQLite;

namespace Persistence.DB
{
	public class SqliteConnectionFactory : ConnectionFactory
	{
		public override IDbConnection createConnection()
		{
			string connectionString = "Data Source=G:\\UBB\\Profesori UBB\\An 2 - Sem 2\\MPP\\datebases\\atletism.db;Version=3";
            //var connectionString = ConfigurationManager.ConnectionStrings["Conn"].ConnectionString;
            return new SQLiteConnection(connectionString);
		}
	}
}
