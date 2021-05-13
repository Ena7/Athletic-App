using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;
using Domain;
using Domain.DTO;
using Services;

namespace Network.ObjectProtocol
{
    public class ServerProxy : IServices
    {
        private readonly string Host;
        private readonly int Port;
        private IObserver Client;
        private NetworkStream Stream;
        private IFormatter Formatter;
        private TcpClient Connection;
        private readonly Queue<Response> Responses;
        private volatile bool Finished;
        private EventWaitHandle WaitHandle;

        public ServerProxy(string host, int port)
        {
            Host = host;
            Port = port;
            Responses = new Queue<Response>();
        }

        public virtual void AddParticipant(string name, int age, Event event1, Event event2)
        {
            ParticipantDetailsDTO pd = new ParticipantDetailsDTO { Name = name, Age = age, Event1 = event1, Event2 = event2 };
            SendRequest(new AddParticipantRequest { ParticipantDetails = pd });
            Response response = ReadResponse();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new Exception(err.ErrorMessage);
            }
        }

        public virtual IEnumerable<ChildNoEventsDTO> GetChildrenNoEvents(Event e, AgeGroup a)
        {
            EventAgeGroupDTO eag = new EventAgeGroupDTO { Event = e, AgeGroup = a };
            SendRequest(new ChildrenEventsRequest { EventAgeGroup = eag });
            Response response = ReadResponse();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new Exception(err.ErrorMessage);
            }
            ChildrenEventsResponse resp = (ChildrenEventsResponse)response;
            return resp.ChildNoEventsList;
        }

        public virtual IEnumerable<EventCountDTO> GetEventsNumber()
        {
            SendRequest(new EventCountRequest { });
            Response response = ReadResponse();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new Exception(err.ErrorMessage);
            }
            EventCountResponse ecr = (EventCountResponse)response;
            return ecr.EventCountList;
        }

        public virtual void Login(User user, IObserver client)
        {
            InitializeConnection();
            SendRequest(new LoginRequest { User = user });
            Response response = ReadResponse();
            if (response is OkResponse)
            {
                Client = client;
                return;
            }
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                CloseConnection();
                throw new Exception(err.ErrorMessage);
            }
        }

        public virtual void Logout(User user, IObserver client)
        {
            SendRequest(new LogoutRequest { User = user });
            Response response = ReadResponse();
            CloseConnection();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new Exception(err.ErrorMessage);
            }
        }

        private void CloseConnection()
        {
            Finished = true;
            try
            {
                Stream.Close();
                Connection.Close();
                WaitHandle.Close();
                Client = null;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        private void SendRequest(Request request)
        {
            try
            {
                Formatter.Serialize(Stream, request);
                Stream.Flush();
            }
            catch (Exception e)
            {
                throw new Exception(e.StackTrace);
            }
        }

        private Response ReadResponse()
        {
            Response response = null;
            try
            {
                WaitHandle.WaitOne();
                lock (Responses)
                {
                    response = Responses.Dequeue();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            return response;
        }

        private void InitializeConnection()
        {
            try
            {
                Connection = new TcpClient(Host, Port);
                Stream = Connection.GetStream();
                Formatter = new BinaryFormatter();
                Finished = false;
                WaitHandle = new AutoResetEvent(false);
                StartReader();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        private void StartReader()
        {
            Thread tw = new Thread(Run);
            tw.Start();
        }

        public virtual void Run()
        {
            while (!Finished)
            {
                try
                {
                    object response = Formatter.Deserialize(Stream);
                    if (response is UpdateResponse)
                    {
                        HandleUpdate((UpdateResponse)response);
                    }
                    else
                    {
                        lock (Responses)
                        {
                            Responses.Enqueue((Response)response);
                        }
                        WaitHandle.Set();
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
        }

        private void HandleUpdate(UpdateResponse update)
        {
            if (update is RefreshEventsResponse)
            {
                RefreshEventsResponse ecr = (RefreshEventsResponse)update;
                try
                {
                    Client.RefreshEvents(ecr.EventCountList);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
        }

    }
}
