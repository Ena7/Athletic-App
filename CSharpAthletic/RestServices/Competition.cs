namespace RestServices
{
    public class Competition
    {
        public long id { get; set; }
        public string @event { get; set; }
        public string ageGroup { get; set; }

        public override string ToString()
        {
            return "ID: " + id + " | Event: " + @event + " | Age Group: " + ageGroup;
        }
    }
}
