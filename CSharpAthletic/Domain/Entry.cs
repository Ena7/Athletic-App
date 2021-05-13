using System;

namespace Domain
{
    [Serializable]
    public enum Event
    {
        NONE,
        E50M,
        E100M,
        E1000M,
        E1500M
    }

    [Serializable]
    public enum AgeGroup
    {
        A6_8Y,
        A9_11Y,
        A12_15Y
    }

    [Serializable]
    public class Entry : Entity<long>
    {
        public Child Child { get; set; }

        public Event Event1 { get; set; }

        public Event Event2 { get; set; }

        public AgeGroup AgeGroup { get; set; }
    }
}
