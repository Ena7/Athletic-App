import domain.*;
import dtos.ChildNoEventsDTO;
import dtos.EventCountDTO;
import repo.ChildRepositoryDB;
import repo.EntryRepositoryDB;
import repo.UserRepositoryDB;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        }
        catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        UserRepositoryDB userRepo = new UserRepositoryDB(props);
        EntryRepositoryDB entryRepo = new EntryRepositoryDB(props);
        ChildRepositoryDB childRepo = new ChildRepositoryDB(props);

         //userRepo.save(new User("destroyer2000", "spaceship"));
         //childRepo.save(new Child("Sonny Crockett", 15));

        for(EventCountDTO ev : entryRepo.getEventsNumber()) {
            System.out.println(ev.getEvent().toString() + " | " + ev.getCount());
        }

        for(ChildNoEventsDTO ch : childRepo.getChildrenNoEvents(Event.E50M, AgeGroup.A9_11Y)) {
            System.out.println(ch.getChild().getId() + " | " + ch.getChild().getName() + " | " + ch.getChild().getAge() + " | " + ch.getNoEvents());
        }

        for(Child ch : childRepo.getUnsignedChildren()) {
            System.out.println(ch.getId() + " | " + ch.getName() + " | " + ch.getAge());
        }



    }
}
