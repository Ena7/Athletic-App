using System;

namespace Domain
{
    [Serializable]
    public class User : Entity<long>
    {
        public string Username { get; set; }
        public string Password { get; set; }
    }
}
