using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Threading;
using Google.Protobuf;
using Services;

namespace NetworkProto
{
    public class ProtoWorker : IObserver
    {
        private readonly IServices Server;
        private readonly TcpClient Connection;
        private readonly NetworkStream Stream;
        private volatile bool Connected;

        public ProtoWorker(IServices server, TcpClient connection)
        {
            Server = server;
            Connection = connection;
            try
            {
                Stream = connection.GetStream();
                Connected = true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        public virtual void RefreshEvents(IEnumerable<Domain.DTO.EventCountDTO> eventCountList)
        {
            try
            {
                Console.WriteLine("Refresh Event Count List Response");
                Domain.DTO.EventCountDTO[] lst = eventCountList.ToArray();
                SendResponse(ProtoUtils.GetEventsNumberResponse(lst, true));
            }
            catch (Exception e)
            {
                throw new Exception(e.StackTrace);
            }
        }

        public virtual void Run()
        {
            while (Connected)
            {
                try
                {
                    Request request = Request.Parser.ParseDelimitedFrom(Stream);
                    Response response = HandleRequest(request);
                    if (response != null)
                    {
                        SendResponse(response);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }

                try
                {
                    Thread.Sleep(1000);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
            try
            {
                Stream.Close();
                Connection.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        private Response HandleRequest(Request request)
        {
            if (request.Type is Request.Types.Type.Login)
            {
                Console.WriteLine("Login request");
                Domain.User user = ProtoUtils.GetUser(request);
                try
                {
                    lock (Server)
                    {
                        Server.Login(user, this);
                    }
                    return ProtoUtils.OkResponse();
                }
                catch (Exception e)
                {
                    Connected = false;
                    return ProtoUtils.ErrorResponse(e.Message);
                }
            }

            if (request.Type is Request.Types.Type.Logout)
            {
                Console.WriteLine("Logout request");
                Domain.User user = ProtoUtils.GetUser(request);
                try
                {
                    lock (Server)
                    {
                        Server.Logout(user, this);
                    }
                    Connected = false;
                    return ProtoUtils.OkResponse();
                }
                catch (Exception e)
                {
                    return ProtoUtils.ErrorResponse(e.Message);
                }
            }

            if (request.Type is Request.Types.Type.ChildrenEvents)
            {
                Console.WriteLine("Children Events Request");
                Domain.DTO.EventAgeGroupDTO eag = ProtoUtils.GetEventAgeGroupDTO(request);
                try
                {
                    Domain.DTO.ChildNoEventsDTO[] lst;
                    lock (Server)
                    {
                        lst = Server.GetChildrenNoEvents(eag.Event, eag.AgeGroup).ToArray();
                    }
                    return ProtoUtils.GetChildrenNoEventsResponse(lst);
                }
                catch (Exception e)
                {
                    return ProtoUtils.ErrorResponse(e.Message);
                }
            }

            if (request.Type is Request.Types.Type.AddParticipant)
            {
                Console.WriteLine("Add Participant Request");
                Domain.DTO.ParticipantDetailsDTO pd = ProtoUtils.GetParticipantDetailsDTO(request);
                try
                {
                    lock (Server)
                    {
                        Server.AddParticipant(pd.Name, pd.Age, pd.Event1, pd.Event2);
                    }
                    return ProtoUtils.OkResponse();
                }
                catch (Exception e)
                {
                    return ProtoUtils.ErrorResponse(e.Message);
                }
            }

            if (request.Type is Request.Types.Type.EventCount)
            {
                Console.WriteLine("Event Count Request");
                try
                {
                    Domain.DTO.EventCountDTO[] lst;
                    lock (Server)
                    {
                        lst = Server.GetEventsNumber().ToArray();
                    }
                    return ProtoUtils.GetEventsNumberResponse(lst, false);
                }
                catch (Exception e)
                {
                    return ProtoUtils.ErrorResponse(e.Message);
                }
            }
            return null;
        }

        private void SendResponse(Response response)
        {
            lock (Stream)
            {
                response.WriteDelimitedTo(Stream);
                Stream.Flush();
            }
        }
    }
}
