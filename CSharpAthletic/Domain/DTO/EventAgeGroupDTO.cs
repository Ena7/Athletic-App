using System;

namespace Domain.DTO
{
    [Serializable]
    public class EventAgeGroupDTO
    {
        public Event Event { get; set; }
        public AgeGroup AgeGroup { get; set; }
    }
}
