using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace RestServices
{
    public class StartRestClient
    {
        static readonly HttpClient Client = new HttpClient();
        static readonly string URI = "http://localhost:8080/athletic/competitions/";

        public static void Main(string[] args)
        {
            Run().Wait();
        }

        static async Task Run()
        {
            // test save
            await Save(URI, new Competition { @event = "3000m", ageGroup = "15-17 years" });

            // test getAll
            Competition[] competitions = await GetAll(URI);
            foreach (Competition c in competitions)
            {
                Console.WriteLine(c);
            }

            // test update
            long lastAddedId = competitions[competitions.Length - 1].id;
            await Update(URI, new Competition { id = lastAddedId, @event = "4000m", ageGroup = "16-18 years" });

            // test getById
            Console.WriteLine(await GetById(URI, lastAddedId));

            // test delete
            await Delete(URI, lastAddedId);

            // check if the new competition is deleted 
            foreach (Competition c in await GetAll(URI))
            {
                Console.WriteLine(c);
            }

            Console.ReadLine();
        }

        static async Task<Competition> GetById(string URI, long id)
        {
            Competition competition = null;
            HttpResponseMessage response = await Client.GetAsync(URI + id);
            if (response.IsSuccessStatusCode)
            {
                competition = await response.Content.ReadAsAsync<Competition>();
                Console.WriteLine("GETBYID OK.");
            }
            return competition;
        }

        static async Task<Competition[]> GetAll(string URI)
        {
            Competition[] competitions = null;
            HttpResponseMessage response = await Client.GetAsync(URI);
            if (response.IsSuccessStatusCode)
            {
                competitions = await response.Content.ReadAsAsync<Competition[]>();
                Console.WriteLine("GETALL OK.");
            }
            return competitions;
        }

        static async Task Save(string URI, Competition competition)
        {
            string jsonCompetition = JsonConvert.SerializeObject(competition);
            StringContent dataCompetition = new StringContent(jsonCompetition, Encoding.UTF8, "application/json");
            HttpResponseMessage response = await Client.PostAsync(URI, dataCompetition);
            if (response.IsSuccessStatusCode)
            {
                Console.WriteLine("SAVE OK.");
            }
        }

        static async Task Update(string URI, Competition competition)
        {
            string jsonCompetition = JsonConvert.SerializeObject(competition);
            StringContent dataCompetition = new StringContent(jsonCompetition, Encoding.UTF8, "application/json");
            HttpResponseMessage response = await Client.PutAsync(URI, dataCompetition);
            if (response.IsSuccessStatusCode)
            {
                Console.WriteLine("UPDATE OK.");
            }
        }

        static async Task Delete(string URI, long id)
        {
            HttpResponseMessage response = await Client.DeleteAsync(URI + id);
            if (response.IsSuccessStatusCode)
            {
                Console.WriteLine("DELETE OK.");
            }
        }
    }
}
