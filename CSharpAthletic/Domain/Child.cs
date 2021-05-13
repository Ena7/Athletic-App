using System;

namespace Domain
{
    [Serializable]
    public class Child : Entity<long>
    {
        public string Name { get; set; }
        public int Age { get; set; }
    }
}
