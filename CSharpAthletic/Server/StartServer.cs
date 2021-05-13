using System;
using Network;
using NetworkProto;
using Persistence;
using Services;

namespace Server
{
    public class StartServer
    {
        static void Main(string[] args)
        {
            IUserRepository userRepo = new UserRepositoryDB();
            IChildRepository childRepo = new ChildRepositoryDB();
            IEntryRepository entryRepo = new EntryRepositoryDB();

            IServices service = new ServerImplementation(userRepo, childRepo, entryRepo);

            //SerialServer server = new SerialServer("127.0.0.1", 55555, service);
            ProtoServer server = new ProtoServer("127.0.0.1", 55556, service);

            server.Start();
            Console.WriteLine("Server started...");
            Console.ReadLine();
        }
    }
}
