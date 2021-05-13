using Domain;

namespace Persistence
{
    public interface IUserRepository : IRepository<long, User>
    {
        User GetByUsername(string username);
    }
}
