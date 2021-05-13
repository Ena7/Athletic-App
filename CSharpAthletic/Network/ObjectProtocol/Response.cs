using System;
using Domain.DTO;

namespace Network.ObjectProtocol
{
    public interface Response
    {
    }

    [Serializable]
    public class OkResponse : Response
    {
    }

    [Serializable]
    public class ErrorResponse: Response
    {
        public string ErrorMessage { get; set; }
    }

    [Serializable]
    public class ChildrenEventsResponse : Response
    {
        public ChildNoEventsDTO[] ChildNoEventsList { get; set; }
    }

    [Serializable]
    public class EventCountResponse : Response
    {
        public EventCountDTO[] EventCountList { get; set; }
    }

    public interface UpdateResponse : Response
    {
    }

    [Serializable]
    public class RefreshEventsResponse : UpdateResponse
    {
        public EventCountDTO[] EventCountList { get; set; }
    }
}
