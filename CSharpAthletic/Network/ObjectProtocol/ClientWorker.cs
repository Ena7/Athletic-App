using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;
using Domain;
using Domain.DTO;
using Services;

namespace Network.ObjectProtocol
{
    public class ClientWorker : IObserver
    {
        private readonly IServices Server;
        private readonly TcpClient Connection;
        private readonly NetworkStream Stream;
        private readonly IFormatter Formatter;
        private volatile bool Connected;

        public ClientWorker(IServices server, TcpClient connection)
        {
            Server = server;
            Connection = connection;
            try
            {
                Stream = connection.GetStream();
                Formatter = new BinaryFormatter();
                Connected = true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        public virtual void RefreshEvents(IEnumerable<EventCountDTO> eventCountList)
        {
            try
            {
                Console.WriteLine("Refresh Event Count List Response");
                EventCountDTO[] lst = eventCountList.ToArray();
                SendResponse(new RefreshEventsResponse { EventCountList = lst });
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
                    object request = Formatter.Deserialize(Stream);
                    object response = HandleRequest((Request)request);
                    if (response != null)
                    {
                        SendResponse((Response)response);
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
            if (request is LoginRequest)
            {
                Console.WriteLine("Login request");
                LoginRequest logReq = (LoginRequest)request;
                User user = logReq.User;
                try
                {
                    lock (Server)
                    {
                        Server.Login(user, this);
                    }
                    return new OkResponse();
                }
                catch (Exception e)
                {
                    Connected = false;
                    return new ErrorResponse { ErrorMessage = e.Message };
                }
            }

            if (request is LogoutRequest)
            {
                Console.WriteLine("Logout request");
                LogoutRequest logReq = (LogoutRequest)request;
                User user = logReq.User;
                try
                {
                    lock (Server)
                    {
                        Server.Logout(user, this);
                    }
                    Connected = false;
                    return new OkResponse();

                }
                catch (Exception e)
                {
                    return new ErrorResponse { ErrorMessage = e.Message };
                }
            }

            if (request is ChildrenEventsRequest)
            {
                Console.WriteLine("Children Events Request");
                ChildrenEventsRequest ceReq = (ChildrenEventsRequest)request;
                EventAgeGroupDTO eag = ceReq.EventAgeGroup;
                try
                {
                    ChildNoEventsDTO[] lst;
                    lock (Server)
                    {
                        lst = Server.GetChildrenNoEvents(eag.Event, eag.AgeGroup).ToArray();
                    }
                    return new ChildrenEventsResponse { ChildNoEventsList = lst };
                }
                catch (Exception e)
                {
                    return new ErrorResponse { ErrorMessage = e.Message };
                }
            }

            if (request is AddParticipantRequest)
            {
                Console.WriteLine("Add Participant Request");
                AddParticipantRequest apReq = (AddParticipantRequest)request;
                ParticipantDetailsDTO pd = apReq.ParticipantDetails;
                try
                {
                    lock (Server) 
                    {
                        Server.AddParticipant(pd.Name, pd.Age, pd.Event1, pd.Event2);
                    }
                    return new OkResponse();
                }
                catch (Exception e)
                {
                    return new ErrorResponse { ErrorMessage = e.Message };
                }
            }

            if (request is EventCountRequest)
            {
                Console.WriteLine("Event Count Request");
                try
                {
                    EventCountDTO[] lst;
                    lock (Server)
                    {
                        lst = Server.GetEventsNumber().ToArray();
                    }
                    return new EventCountResponse { EventCountList = lst };
                }
                catch (Exception e)
                {
                    return new ErrorResponse { ErrorMessage = e.Message };
                }
            }

            return null;
        }

        private void SendResponse(Response response)
        {
            lock (Stream)
            {
                Formatter.Serialize(Stream, response);
                Stream.Flush();
            }
        }
    }
}
