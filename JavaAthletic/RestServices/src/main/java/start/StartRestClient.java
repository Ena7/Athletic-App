package start;

import rest_client.CompetitionRestClient;
import rest_domain.Competition;

public class StartRestClient {
    private final static CompetitionRestClient competitionClient = new CompetitionRestClient();

    private static void show(Runnable task) {
        try {
            task.run();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            // test save
            show(() -> System.out.println(competitionClient.save( new Competition("3000m", "15-17 years"))));

            // test getAll
            Competition[] competitions = competitionClient.getAll();
            show(() -> {
                for (Competition c : competitions) {
                    System.out.println(c);
                }
            });

            // test update
            Long lastAddedId = competitions[competitions.length - 1].getId();
            Competition updateCompetition = new Competition("4000m", "16-18 years");
            updateCompetition.setId(lastAddedId);
            show(() -> competitionClient.update(updateCompetition));

            // test getById
            show(() -> System.out.println(competitionClient.getById(lastAddedId)));

            // test delete
            show(() -> competitionClient.delete(lastAddedId));

            // check if the new competition is deleted
            show(() -> {
                for (Competition c : competitionClient.getAll()) {
                    System.out.println(c);
                }
            });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
