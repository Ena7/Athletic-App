import domain.*;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;
import repo.IChildRepository;
import repo.IEntryRepository;
import repo.IUserRepository;
import utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceServer implements IServiceServer {

    private final IUserRepository userRepo;
    private final IChildRepository childRepo;
    private final IEntryRepository entryRepo;
    private final Map<String, IObserver> loggedClients;


    public ServiceServer(IUserRepository userRepo, IChildRepository childRepo, IEntryRepository entryRepo) {
        this.userRepo = userRepo;
        this.childRepo = childRepo;
        this.entryRepo = entryRepo;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User input_user, IObserver client) throws Exception {
        User user = userRepo.getByUsername(input_user.getUsername());
        if (user == null){
            throw new Exception("Authentication failed");
        }
        if (!user.getPassword().equals(input_user.getPassword())){
            throw new Exception("Authentication failed");
        }
        if (loggedClients.get(user.getUsername()) != null) {
            throw new Exception("User is already logged in");
        }
        loggedClients.put(user.getUsername(), client);
    }

    @Override
    public synchronized void logout(User user, IObserver client) throws Exception {
        IObserver localClient = loggedClients.remove(user.getUsername());
        if (localClient == null) {
            throw new Exception("User " + user.getUsername() + " is not logged in");
        }
    }

    @Override
    public List<EventCountDTO> getEventsNumber() {
        return entryRepo.getEventsNumber();
    }

    @Override
    public synchronized List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup) {
        return childRepo.getChildrenNoEvents(event, ageGroup);
    }

    private Pair<Integer, Integer> ParseAgeGroup(AgeGroup ageGroup) {
        String ageGroupString = ageGroup.toString();
        String[] ageBorders = ageGroupString.split("[A_Y]");
        return new Pair<>(Integer.parseInt(ageBorders[1]), Integer.parseInt(ageBorders[2]));
    }

    private void notifyEvents() {
        int defaultThreadsNo = 5;
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver client : loggedClients.values()){
            executor.execute(() -> {
                try {
                    client.refreshEvents(entryRepo.getEventsNumber());
                } catch (Exception e) {
                    System.err.println("Error notifying friend " + e);
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public synchronized void addParticipant(String name, int age, Event event1, Event event2) throws Exception {
        AgeGroup ageGroup = null;
        for (AgeGroup ag : AgeGroup.values()) {
            Pair<Integer, Integer> ageBorders = ParseAgeGroup(ag);
            if (age >= ageBorders.getKey() && age <= ageBorders.getValue()) {
                ageGroup = ag;
                break;
            }
        }
        if (ageGroup == null) {
            throw new Exception("Invalid age group");
        }

        Child child = new Child(name, age);
        child.setId(childRepo.save(child));
        entryRepo.save(new Entry(child, event1, event2, ageGroup));
        notifyEvents();
    }
}
