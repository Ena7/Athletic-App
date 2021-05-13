import domain.User;
import dtos.EventAgeGroupDTO;
import dto.ParticipantDetailsDTO;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;
import protocol.Request;
import protocol.RequestType;
import protocol.Response;
import protocol.ResponseType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable, IObserver {
    private final IServiceServer server;
    private final Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientWorker(IServiceServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refreshEvents(List<EventCountDTO> eventCountList) throws Exception {
        EventCountDTO[] arr = eventCountList.toArray(new EventCountDTO[0]);
        Response response = new Response.Builder().type(ResponseType.REFRESH_EVENTS).data(arr).build();
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            throw new Exception("Sending error\n" + e);
        }
    }

    @Override
    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request)request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error\n" + e);
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    private final static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        if (request.type() == RequestType.LOGIN) {
            System.out.println("Login request");
            User user = (User) request.data();
            try {
                server.login(user, this);
                return okResponse;
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            System.out.println("Logout request");
            User user = (User) request.data();
            try {
                server.logout(user, this);
                connected = false;
                return okResponse;
            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.CHILDREN_EVENTS) {
            System.out.println("Children events request");
            EventAgeGroupDTO eag = (EventAgeGroupDTO) request.data();
            try {
                List<ChildNoEventsDTO> lst = server.getChildrenNoEvents(eag.getEvent(), eag.getAgeGroup());
                ChildNoEventsDTO[] arr = lst.toArray(new ChildNoEventsDTO[0]);
                return new Response.Builder().type(ResponseType.CHILDREN_EVENTS).data(arr).build();
            }
            catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.ADD_PARTICIPANT) {
            System.out.println("Add participant request");
            ParticipantDetailsDTO pd = (ParticipantDetailsDTO) request.data();
            try {
                server.addParticipant(pd.getName(), pd.getAge(), pd.getEvent1(), pd.getEvent2());
                return okResponse;
            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.EVENT_COUNT) {
            System.out.println("Event count request");
            try {
                List<EventCountDTO> lst = server.getEventsNumber();
                EventCountDTO[] arr = lst.toArray(new EventCountDTO[0]);
                return new Response.Builder().type(ResponseType.EVENT_COUNT).data(arr).build();

            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return null;
    }

}
