using System;

namespace Client
{
    public enum UserEvent
    {
        RefreshEvents
    }

    public class UserEventArgs : EventArgs
    {
        private readonly UserEvent UserEvent;
        private readonly object Content;

        public UserEventArgs(UserEvent userEvent, object content)
        {
            UserEvent = userEvent;
            Content = content;
        }

        public UserEvent UserEventType
        {
            get { return UserEvent; }
        }

        public object Data
        {
            get { return Content; }
        }
    }
}
