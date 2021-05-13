package repo;

import domain.User;

public interface IUserRepository extends IRepository<Long, User> {
    User getByUsername(String s);
}
