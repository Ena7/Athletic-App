using System.Net.Sockets;
using System.Threading;
using Network.ObjectProtocol;
using Services;

namespace Network
{
    public class SerialServer : ConcurrentServer
    {
        private readonly IServices Server;
        private ClientWorker Worker;

        public SerialServer(string host, int port, IServices server) : base(host, port)
        {
            Server = server;
        }

        protected override Thread CreateWorker(TcpClient client)
        {
            Worker = new ClientWorker(Server, client);
            return new Thread(new ThreadStart(Worker.Run));
        }
    }
}
