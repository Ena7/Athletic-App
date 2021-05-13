using System;

namespace Domain
{
    [Serializable]
    public class Entity<ID>
    {
        public ID Id { get; set; }
    }
}
