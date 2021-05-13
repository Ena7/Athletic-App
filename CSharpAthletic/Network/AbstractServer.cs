using System;
using System.Net;
using System.Net.Sockets;

namespace Network
{
    public abstract class AbstractServer
    {
        private TcpListener Server;
        private readonly string Host;
        private readonly int Port;

        public AbstractServer(string host, int port)
        {
            Host = host;
            Port = port;
        }

        public void Start()
        {
            IPAddress adr = IPAddress.Parse(Host);
            IPEndPoint ep = new IPEndPoint(adr, Port);
            Server = new TcpListener(ep);
            Server.Start();
            while (true)
            {
                Console.WriteLine("Waiting for clients...");
                TcpClient client = Server.AcceptTcpClient();
                Console.WriteLine("Client connected...");
                ProcessRequest(client);
            }
        }

        public abstract void ProcessRequest(TcpClient client);
    }
}
