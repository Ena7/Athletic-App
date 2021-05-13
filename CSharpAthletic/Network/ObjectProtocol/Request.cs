using System;
using Domain;
using Domain.DTO;

namespace Network.ObjectProtocol
{
    public interface Request 
    { 
    }

    [Serializable]
    public class LoginRequest : Request
    {
        public User User { get; set; }
    }

    [Serializable]
    public class LogoutRequest : Request
    {
        public User User { get; set; }
    }

    [Serializable]
    public class ChildrenEventsRequest : Request
    {
        public EventAgeGroupDTO EventAgeGroup { get; set; }
    }

    [Serializable]
    public class AddParticipantRequest : Request
    {
        public ParticipantDetailsDTO ParticipantDetails { get; set; }
    }

    [Serializable]
    public class EventCountRequest : Request
    {
    }


}
