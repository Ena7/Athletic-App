namespace NetworkProto
{
    // RESPONSES
    public static class ProtoUtils
    {
        public static Response OkResponse()
        {
            return new Response { Type = Response.Types.Type.Ok };
        }

        public static Response ErrorResponse(string error)
        {
            return new Response { Type = Response.Types.Type.Error, Error = error };
        }

        public static Response GetChildrenNoEventsResponse(Domain.DTO.ChildNoEventsDTO[] cnes)
        {
            Response response = new Response { Type = Response.Types.Type.ChildrenEvents };
            foreach(Domain.DTO.ChildNoEventsDTO cne in cnes)
            {
                response.Cne.Add(new ChildNoEventsDTO { Child = GetProtobufChild(cne.Child), NoEvents = cne.NoEvents });
            }
            return response;
        }

        public static Response GetEventsNumberResponse(Domain.DTO.EventCountDTO[] ecs, bool refresh)
        {
            Response response = null;
            if (refresh)
                response = new Response { Type = Response.Types.Type.RefreshEvents };
            else
                response = new Response { Type = Response.Types.Type.EventCount };
            foreach (Domain.DTO.EventCountDTO ec in ecs)
            {
                response.Ec.Add(new EventCountDTO { Event = GetProtobufEvent(ec.Event), AgeGroup = GetProtobufAgeGroup(ec.AgeGroup), Count = ec.Count });
            }
            return response;

        }

        // PROTOBUF REQUEST TO DOMAIN
        public static Domain.User GetUser(Request request)
        {
            return new Domain.User { Username = request.User.Username, Password = request.User.Password };
        }

        public static Domain.DTO.EventAgeGroupDTO GetEventAgeGroupDTO(Request request)
        {
            return new Domain.DTO.EventAgeGroupDTO { Event = GetProtobufEvent(request.Eag.Event), AgeGroup = GetProtobufAgeGroup(request.Eag.AgeGroup) };
        }

        public static Domain.DTO.ParticipantDetailsDTO GetParticipantDetailsDTO(Request request)
        {
            return new Domain.DTO.ParticipantDetailsDTO { Name = request.Pd.Name, Age = request.Pd.Age, Event1 = GetProtobufEvent(request.Pd.Event1), Event2 = GetProtobufEvent(request.Pd.Event2) };
        }

        // DOMAIN TO PROTOBUF
        private static Child GetProtobufChild(Domain.Child child)
        {
            return new Child { Name = child.Name, Age = child.Age };
        }

        private static Event GetProtobufEvent(Domain.Event eventt)
        {
            if(eventt == Domain.Event.NONE)
                return Event.None;
            if (eventt == Domain.Event.E50M)
                return Event.E50M;
            if (eventt == Domain.Event.E100M)
                return Event.E100M;
            if (eventt == Domain.Event.E1000M)
                return Event.E1000M;
            if (eventt == Domain.Event.E1500M)
                return Event.E1500M;
            return Event.None;
        }

        private static AgeGroup GetProtobufAgeGroup(Domain.AgeGroup ageGroup)
        {
            if (ageGroup == Domain.AgeGroup.A6_8Y)
                return AgeGroup.A68Y;
            if (ageGroup == Domain.AgeGroup.A9_11Y)
                return AgeGroup.A911Y;
            if (ageGroup == Domain.AgeGroup.A12_15Y)
                return AgeGroup.A1215Y;
            return AgeGroup.A68Y;
        }

        // PROTOBUF TO DOMAIN
        private static Domain.Child GetProtobufChild(Child child)
        {
            return new Domain.Child { Name = child.Name, Age = child.Age };
        }

        private static Domain.Event GetProtobufEvent(Event eventt)
        {
            if (eventt == Event.None)
                return Domain.Event.NONE;
            if (eventt == Event.E50M)
                return Domain.Event.E50M;
            if (eventt == Event.E100M)
                return Domain.Event.E100M;
            if (eventt == Event.E1000M)
                return Domain.Event.E1000M;
            if (eventt == Event.E1500M)
                return Domain.Event.E1500M;
            return Domain.Event.NONE;
        }

        private static Domain.AgeGroup GetProtobufAgeGroup(AgeGroup ageGroup)
        {
            if (ageGroup == AgeGroup.A68Y)
                return Domain.AgeGroup.A6_8Y;
            if (ageGroup == AgeGroup.A911Y)
                return Domain.AgeGroup.A9_11Y;
            if (ageGroup == AgeGroup.A1215Y)
                return Domain.AgeGroup.A12_15Y;
            return Domain.AgeGroup.A6_8Y;
        }
    }
}
