package service;

import domain.*;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;
import javafx.util.Pair;
import repo.IChildRepository;
import repo.IEntryRepository;
import repo.IUserRepository;

import java.util.List;

public class Service implements IService {

    private final IUserRepository userRepo;
    private final IChildRepository childRepo;
    private final IEntryRepository entryRepo;

    public Service(IUserRepository userRepo, IChildRepository childRepo, IEntryRepository entryRepo) {
        this.userRepo = userRepo;
        this.childRepo = childRepo;
        this.entryRepo = entryRepo;
    }

    private Pair<Integer, Integer> ParseAgeGroup(AgeGroup ageGroup) {
        String ageGroupString = ageGroup.toString();
        String[] ageBorders = ageGroupString.split("[A_Y]");
        return new Pair<>(Integer.parseInt(ageBorders[1]), Integer.parseInt(ageBorders[2]));
    }

    public User checkUserCredentials(String username, String password) {
        User user = userRepo.getByUsername(username);
        if (user == null){
            return null;
        }
        if (!user.getPassword().equals(password)){
            return null;
        }
        return user;
    }

    public List<EventCountDTO> getEventsNumber(){
        return entryRepo.getEventsNumber();
    }

    public List<ChildNoEventsDTO> getChildrenNoEvents(Event event, AgeGroup ageGroup) {
        return childRepo.getChildrenNoEvents(event, ageGroup);
    }

    public void addParticipant(String name, int age, Event event1, Event event2) throws Exception {
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
    }
}
