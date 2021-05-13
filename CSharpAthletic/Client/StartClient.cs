using System;
using System.Windows.Forms;
using Network.ObjectProtocol;
using Services;

namespace Client
{
    static class StartClient
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            IServices server = new ServerProxy("127.0.0.1", 55555);

            ClientController controller = new ClientController(server);

            LoginWindow loginWindow = new LoginWindow(controller);
            Application.Run(loginWindow);
            
        }
    }
}
