import domain.User;
import dto.ParticipantDetailsDTO;
import dtos.ChildNoEventsDTO;
import dtos.EventAgeGroupDTO;
import dtos.EventCountDTO;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ProtoWorker implements Runnable, IObserver {
    private final IServiceServer server;
    private final Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public ProtoWorker(IServiceServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = connection.getOutputStream();
            input = connection.getInputStream();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refreshEvents(List<EventCountDTO> eventCountList) throws Exception {
        EventCountDTO[] arr = eventCountList.toArray(new EventCountDTO[0]);
        try {
            sendResponse(ProtoUtils.getEventsNumberResponse(arr, true));
        }
        catch (IOException e) {
            throw new Exception("Sending error\n" + e);
        }
    }

    @Override
    public void run() {
        while(connected) {
            try {
                Protobuf.Request request = Protobuf.Request.parseDelimitedFrom(input);
                Protobuf.Response response = handleRequest(request);
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

    private void sendResponse(Protobuf.Response response) throws IOException {
        System.out.println("Sending response " + response);
        response.writeDelimitedTo(output);
        output.flush();
    }

    private Protobuf.Response handleRequest(Protobuf.Request request) {
        if (request.getType() == Protobuf.Request.Type.LOGIN) {
            System.out.println("Login request");
            User user = ProtoUtils.getUser(request);
            try {
                server.login(user, this);
                return ProtoUtils.okResponse();
            } catch (Exception e) {
                connected = false;
                return ProtoUtils.errorResponse(e.getMessage());
            }
        }
        if (request.getType() == Protobuf.Request.Type.LOGOUT) {
            System.out.println("Logout request");
            User user = ProtoUtils.getUser(request);
            try {
                server.logout(user, this);
                connected = false;
                return ProtoUtils.okResponse();
            } catch (Exception e) {
                return ProtoUtils.errorResponse(e.getMessage());
            }
        }
        if (request.getType() == Protobuf.Request.Type.CHILDREN_EVENTS) {
            System.out.println("Children events request");
            EventAgeGroupDTO eag = ProtoUtils.getEventAgeGroupDTO(request);
            try {
                List<ChildNoEventsDTO> lst = server.getChildrenNoEvents(eag.getEvent(), eag.getAgeGroup());
                ChildNoEventsDTO[] arr = lst.toArray(new ChildNoEventsDTO[0]);
                return ProtoUtils.getChildrenNoEventsResponse(arr);
            }
            catch (Exception e) {
                return ProtoUtils.errorResponse(e.getMessage());
            }
        }
        if (request.getType() == Protobuf.Request.Type.ADD_PARTICIPANT) {
            System.out.println("Add participant request");
            ParticipantDetailsDTO pd = ProtoUtils.getParticipantDetailsDTO(request);
            try {
                server.addParticipant(pd.getName(), pd.getAge(), pd.getEvent1(), pd.getEvent2());
                return ProtoUtils.okResponse();
            } catch (Exception e) {
                return ProtoUtils.errorResponse(e.getMessage());
            }
        }
        if (request.getType() == Protobuf.Request.Type.EVENT_COUNT) {
            System.out.println("Event count request");
            try {
                List<EventCountDTO> lst = server.getEventsNumber();
                EventCountDTO[] arr = lst.toArray(new EventCountDTO[0]);
                return ProtoUtils.getEventsNumberResponse(arr, false);

            } catch (Exception e) {
                return ProtoUtils.errorResponse(e.getMessage());
            }
        }
        return null;
    }
}
