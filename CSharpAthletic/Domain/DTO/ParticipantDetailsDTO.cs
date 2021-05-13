using System;

namespace Domain.DTO
{
    [Serializable]
    public class ParticipantDetailsDTO
    {
        public string Name { get; set; }
        public int Age { get; set; }
        public Event Event1 { get; set; }
        public Event Event2 { get; set; }
    }
}
