import java.net.Socket;

public class ProtoConcurrentServer extends AbstractConcurrentServer {
    private final IServiceServer server;

    public ProtoConcurrentServer(int port, IServiceServer server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoWorker worker = new ProtoWorker(server, client);
        return new Thread(worker);
    }

    @Override
    public void stop(){
        System.out.println("Stopping services...");
    }
}
