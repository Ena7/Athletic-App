using System;

namespace Domain.DTO
{
    [Serializable]
    public class EventCountDTO
    {
        public Event Event { get; set; }
        public AgeGroup AgeGroup { get; set; }
        public long Count { get; set; }
    }
}
