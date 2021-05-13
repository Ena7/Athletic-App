import domain.AgeGroup;
import domain.Event;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements IServiceServer {
    private final String host;
    private final int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private final BlockingQueue<Response> qResponses;
    private volatile boolean finished;

    public ServiceProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qResponses = new LinkedBlockingQueue<>();
    }

    @Override
    public void login(User user, IObserver client) throws Exception {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public void logout(User user, IObserver client) throws Exception {
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
    }

    @Override
    public List<EventCountDTO> getEventsNumber() throws Exception {
        Request request = new Request.Builder().type(RequestType.EVENT_COUNT).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        EventCountDTO[] lst = (EventCountDTO[]) response.data();
        return Arrays.asList(lst);
    }


    @Override
    public List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup) throws Exception {
        EventAgeGroupDTO eag = new EventAgeGroupDTO(event, ageGroup);
        Request request = new Request.Builder().type(RequestType.CHILDREN_EVENTS).data(eag).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        ChildNoEventsDTO[] lst = (ChildNoEventsDTO[]) response.data();
        return Arrays.asList(lst);
    }

    @Override
    public void addParticipant(String name, int age, Event event1, Event event2) throws Exception {
        ParticipantDetailsDTO pd = new ParticipantDetailsDTO(name, age, event1, event2);
        Request request = new Request.Builder().type(RequestType.ADD_PARTICIPANT).data(pd).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            System.out.println("Add participant response ok");
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host,port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qResponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.REFRESH_EVENTS;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Response received " + response);
                    if (isUpdate((Response)response)) {
                        handleUpdate((Response)response);
                    }
                    else {
                        try {
                            qResponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.REFRESH_EVENTS) {
            EventCountDTO[] arr = (EventCountDTO[]) response.data();
            System.out.println("Refresh events count");
            try {
                client.refreshEvents(Arrays.asList(arr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
