using System.Net.Sockets;
using System.Threading;
using Services;

namespace NetworkProto
{
    public class ProtoServer : ConcurrentServer
    {
        private readonly IServices Server;
        private ProtoWorker Worker;

        public ProtoServer(string host, int port, IServices server) : base(host, port)
        {
            Server = server;
        }
        protected override Thread CreateWorker(TcpClient client)
        {
            Worker = new ProtoWorker(Server, client);
            return new Thread(new ThreadStart(Worker.Run));
        }
    }
}
