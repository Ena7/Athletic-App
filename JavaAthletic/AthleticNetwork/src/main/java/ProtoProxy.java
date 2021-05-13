import domain.AgeGroup;
import domain.Event;
import domain.User;
import dto.ParticipantDetailsDTO;
import dtos.ChildNoEventsDTO;
import dtos.EventAgeGroupDTO;
import dtos.EventCountDTO;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements IServiceServer {
    private final String host;
    private final int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private final BlockingQueue<Protobuf.Response> qResponses;
    private volatile boolean finished;

    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qResponses = new LinkedBlockingQueue<>();
    }

    @Override
    public void login(User user, IObserver client) throws Exception {
        initializeConnection();
        sendRequest(ProtoUtils.loginRequest(user));
        Protobuf.Response response = readResponse();
        if (response.getType() == Protobuf.Response.Type.OK) {
            this.client = client;
            return;
        }
        if (response.getType() == Protobuf.Response.Type.ERROR) {
            String err = ProtoUtils.getError(response);
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public void logout(User user, IObserver client) throws Exception {
        sendRequest(ProtoUtils.logoutRequest(user));
        Protobuf.Response response = readResponse();
        closeConnection();
        if (response.getType() == Protobuf.Response.Type.ERROR) {
            String err = ProtoUtils.getError(response);
            throw new Exception(err);
        }
    }

    @Override
    public List<EventCountDTO> getEventsNumber() throws Exception {
        sendRequest(ProtoUtils.getEventsNumberRequest());
        Protobuf.Response response = readResponse();
        if (response.getType() == Protobuf.Response.Type.ERROR) {
            String err = ProtoUtils.getError(response);
            throw new Exception(err);
        }
        EventCountDTO[] lst = ProtoUtils.getEventsNumber(response);
        return Arrays.asList(lst);
    }


    @Override
    public List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup) throws Exception {
        EventAgeGroupDTO eag = new EventAgeGroupDTO(event, ageGroup);
        sendRequest(ProtoUtils.getChildrenNoEventsRequest(eag));
        Protobuf.Response response = readResponse();
        if (response.getType() == Protobuf.Response.Type.ERROR) {
            String err = ProtoUtils.getError(response);
            throw new Exception(err);
        }
        ChildNoEventsDTO[] lst = ProtoUtils.getChildrenNoEvents(response);
        return Arrays.asList(lst);
    }

    @Override
    public void addParticipant(String name, int age, Event event1, Event event2) throws Exception {
        ParticipantDetailsDTO pd = new ParticipantDetailsDTO(name, age, event1, event2);
        sendRequest(ProtoUtils.addParticipantRequest(pd));
        Protobuf.Response response = readResponse();
        if (response.getType() == Protobuf.Response.Type.OK) {
            System.out.println("Add participant response ok");
            return;
        }
        if (response.getType() == Protobuf.Response.Type.ERROR) {
            String err = ProtoUtils.getError(response);
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
            output = connection.getOutputStream();
            input = connection.getInputStream();
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

    private void sendRequest(Protobuf.Request request) throws Exception {
        try {
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }
    }

    private Protobuf.Response readResponse() {
        Protobuf.Response response = null;
        try {
            response = qResponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private boolean isUpdate(Protobuf.Response.Type type){
        return type == Protobuf.Response.Type.REFRESH_EVENTS;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Protobuf.Response response = Protobuf.Response.parseDelimitedFrom(input);
                    System.out.println("Response received " + response);
                    if (isUpdate(response.getType())) {
                        handleUpdate(response);
                    }
                    else {
                        try {
                            qResponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    private void handleUpdate(Protobuf.Response response) {
        if (response.getType() == Protobuf.Response.Type.REFRESH_EVENTS) {
            EventCountDTO[] arr = ProtoUtils.getEventsNumber(response);
            System.out.println("Refresh events count");
            try {
                client.refreshEvents(Arrays.asList(arr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
