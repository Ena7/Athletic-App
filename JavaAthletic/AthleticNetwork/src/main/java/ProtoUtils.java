import domain.AgeGroup;
import domain.Child;
import domain.Event;
import domain.User;
import dto.ParticipantDetailsDTO;
import dtos.ChildNoEventsDTO;
import dtos.EventAgeGroupDTO;
import dtos.EventCountDTO;

public class ProtoUtils {

    // REQUESTS
    public static Protobuf.Request loginRequest(User user) {
        Protobuf.User userDTO = Protobuf.User.newBuilder().setUsername(user.getUsername()).setPassword(user.getPassword()).build();
        return Protobuf.Request.newBuilder().setType(Protobuf.Request.Type.LOGIN).setUser(userDTO).build();
    }

    public static Protobuf.Request logoutRequest(User user) {
        Protobuf.User userDTO = Protobuf.User.newBuilder()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword()).build();
        return Protobuf.Request.newBuilder().setType(Protobuf.Request.Type.LOGOUT).setUser(userDTO).build();
    }

    public static Protobuf.Request getEventsNumberRequest() {
        return Protobuf.Request.newBuilder().setType(Protobuf.Request.Type.EVENT_COUNT).build();
    }

    public static Protobuf.Request getChildrenNoEventsRequest(EventAgeGroupDTO eag) {
        Protobuf.EventAgeGroupDTO eagDTO = Protobuf.EventAgeGroupDTO.newBuilder()
                .setEvent(getProtobufEvent(eag.getEvent()))
                .setAgeGroup(getProtobufAgeGroup(eag.getAgeGroup())).build();
        return Protobuf.Request.newBuilder().setType(Protobuf.Request.Type.CHILDREN_EVENTS).setEag(eagDTO).build();
    }

    public static Protobuf.Request addParticipantRequest(ParticipantDetailsDTO pd) {
        Protobuf.ParticipantDetailsDTO pdDTO = Protobuf.ParticipantDetailsDTO.newBuilder()
                .setName(pd.getName())
                .setAge(pd.getAge())
                .setEvent1(getProtobufEvent(pd.getEvent1()))
                .setEvent2(getProtobufEvent(pd.getEvent2())).build();
        return Protobuf.Request.newBuilder().setType(Protobuf.Request.Type.ADD_PARTICIPANT).setPd(pdDTO).build();
    }

    // RESPONSES
    public static Protobuf.Response okResponse() {
        return Protobuf.Response.newBuilder().setType(Protobuf.Response.Type.OK).build();
    }

    public static Protobuf.Response errorResponse(String error) {
        return Protobuf.Response.newBuilder().setType(Protobuf.Response.Type.ERROR)
                .setError(error).build();
    }

    public static Protobuf.Response getChildrenNoEventsResponse(ChildNoEventsDTO[] cnes) {
        Protobuf.Response.Builder response = Protobuf.Response.newBuilder().setType(Protobuf.Response.Type.CHILDREN_EVENTS);
        for (ChildNoEventsDTO cne : cnes) {
            Protobuf.ChildNoEventsDTO cneDTO = Protobuf.ChildNoEventsDTO.newBuilder()
                    .setChild(getProtobufChild(cne.getChild()))
                    .setNoEvents(cne.getNoEvents()).build();
            response.addCne(cneDTO);
        }
        return response.build();
    }

    public static Protobuf.Response getEventsNumberResponse(EventCountDTO[] ecs, boolean refresh) {
        Protobuf.Response.Builder response;
        if (refresh)
            response = Protobuf.Response.newBuilder().setType(Protobuf.Response.Type.REFRESH_EVENTS);
        else
            response = Protobuf.Response.newBuilder().setType(Protobuf.Response.Type.EVENT_COUNT);
        for (EventCountDTO ec : ecs) {
            Protobuf.EventCountDTO ecDTO = Protobuf.EventCountDTO.newBuilder()
                    .setEvent(getProtobufEvent(ec.getEvent()))
                    .setAgeGroup(getProtobufAgeGroup(ec.getAgeGroup()))
                    .setCount(ec.getCount()).build();
            response.addEc(ecDTO);
        }
        return response.build();
    }

    // PROTOBUF RESPONSE TO DOMAIN
    public static String getError(Protobuf.Response response) {
        return response.getError();
    }

    public static EventCountDTO[] getEventsNumber(Protobuf.Response response) {
        EventCountDTO[] lst = new EventCountDTO[response.getEcCount()];
        for (int i = 0; i < response.getEcCount(); i++) {
            Protobuf.EventCountDTO ecDTO = response.getEc(i);
            EventCountDTO ec = new EventCountDTO(getProtobufEvent(ecDTO.getEvent()), getProtobufAgeGroup(ecDTO.getAgeGroup()), ecDTO.getCount());
            lst[i] = ec;
        }
        return lst;
    }

    public static ChildNoEventsDTO[] getChildrenNoEvents(Protobuf.Response response) {
        ChildNoEventsDTO[] lst = new ChildNoEventsDTO[response.getCneCount()];
        for (int i = 0; i < response.getCneCount(); i++) {
            Protobuf.ChildNoEventsDTO cneDTO = response.getCne(i);
            ChildNoEventsDTO cne = new ChildNoEventsDTO(getProtobufChild(cneDTO.getChild()), cneDTO.getNoEvents());
            lst[i] = cne;
        }
        return lst;
    }

    // PROTOBUF REQUEST TO DOMAIN
    public static User getUser(Protobuf.Request request) {
        return new User(request.getUser().getUsername(), request.getUser().getPassword());
    }

    public static EventAgeGroupDTO getEventAgeGroupDTO(Protobuf.Request request) {
        return new EventAgeGroupDTO(getProtobufEvent(request.getEag().getEvent()), getProtobufAgeGroup(request.getEag().getAgeGroup()));
    }

    public static ParticipantDetailsDTO getParticipantDetailsDTO(Protobuf.Request request) {
        return new ParticipantDetailsDTO(request.getPd().getName(), request.getPd().getAge(), getProtobufEvent(request.getPd().getEvent1()), getProtobufEvent(request.getPd().getEvent2()));
    }

    // DOMAIN TO PROTOBUF
    private static Protobuf.AgeGroup getProtobufAgeGroup(AgeGroup ageGroup) {
        return switch (ageGroup) {
            case A6_8Y -> Protobuf.AgeGroup.A6_8Y;
            case A9_11Y -> Protobuf.AgeGroup.A9_11Y;
            case A12_15Y -> Protobuf.AgeGroup.A12_15Y;
        };
    }

    private static Protobuf.Event getProtobufEvent(Event event) {
        return switch (event) {
            case NONE -> Protobuf.Event.NONE;
            case E50M -> Protobuf.Event.E50M;
            case E100M -> Protobuf.Event.E100M;
            case E1000M -> Protobuf.Event.E1000M;
            case E1500M -> Protobuf.Event.E1500M;
        };
    }

    private static Protobuf.Child getProtobufChild(Child child) {
        return Protobuf.Child.newBuilder().setName(child.getName()).setAge(child.getAge()).build();
    }

    // PROTOBUF TO DOMAIN
    private static AgeGroup getProtobufAgeGroup(Protobuf.AgeGroup ageGroup) {
        return switch (ageGroup) {
            case A6_8Y -> AgeGroup.A6_8Y;
            case A9_11Y -> AgeGroup.A9_11Y;
            case A12_15Y -> AgeGroup.A12_15Y;
            case UNRECOGNIZED -> null;
        };
    }

    private static Event getProtobufEvent(Protobuf.Event event) {
        return switch (event) {
            case NONE -> Event.NONE;
            case E50M -> Event.E50M;
            case E100M -> Event.E100M;
            case E1000M -> Event.E1000M;
            case E1500M -> Event.E1500M;
            case UNRECOGNIZED -> null;
        };
    }

    private static Child getProtobufChild(Protobuf.Child child) {
        return new Child(child.getName(), child.getAge());
    }

}
