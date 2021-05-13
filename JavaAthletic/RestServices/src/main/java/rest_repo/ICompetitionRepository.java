package rest_repo;

import rest_domain.Competition;

public interface ICompetitionRepository {
    void save(Competition competition);
    void update(Competition competition);
    void delete(Long id);
    Competition getById(Long id);
    Competition[] getAll();
}
