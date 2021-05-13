using System;

namespace Domain.DTO
{
    [Serializable]
    public class ChildNoEventsDTO
    {
        public Child Child { get; set; }
        public int NoEvents { get; set; }
    }
}
