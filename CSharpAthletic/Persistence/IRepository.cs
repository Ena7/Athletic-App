using System.Collections.Generic;
using Domain;

namespace Persistence
{
    public interface IRepository<ID, E> where E : Entity<ID>
    {
        ID Save(E entity);
        IEnumerable<E> GetAll();
        E GetById(ID id);
    }
}
