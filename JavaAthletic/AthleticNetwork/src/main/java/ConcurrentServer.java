import java.net.Socket;


public class ConcurrentServer extends AbstractConcurrentServer {
    private final IServiceServer server;

    public ConcurrentServer(int port, IServiceServer server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker = new ClientWorker(server, client);
        return new Thread(worker);
    }

    @Override
    public void stop(){
        System.out.println("Stopping services...");
    }
}
