package rest_client;

import org.springframework.web.client.RestTemplate;
import rest_domain.Competition;

import java.util.concurrent.Callable;

public class CompetitionRestClient {
    public static final String URL = "http://localhost:8080/athletic/competitions";
    private final RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Competition[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Competition[].class));
    }

    public Competition getById(Long id) {
        return execute(() -> restTemplate.getForObject(URL + "/" + id, Competition.class));
    }

    public Competition save(Competition competition) {
        return execute(() -> restTemplate.postForObject(URL, competition, Competition.class));
    }

    public void update(Competition competition) {
        execute(() -> {
            restTemplate.put(URL, competition);
            return null;
        });
    }

    public void delete(Long id) {
        execute(() -> {
            restTemplate.delete(URL + "/" + id);
            return null;
        });
    }
}
