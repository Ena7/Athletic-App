using System;
using System.Windows.Forms;

namespace Client
{
    public partial class LoginWindow : Form
    {
        private readonly ClientController Controller;

        public LoginWindow(ClientController controller)
        {
            InitializeComponent();
            Controller = controller;
        }

        private void LoginButton_Click(object sender, EventArgs e)
        {
            string username = UsernameTextField.Text;
            string password = PasswordTextField.Text;
            try
            {
                Controller.Login(username, password);

                MainWindow mainWindow = new MainWindow(Controller) { Text = "User: " + username };
                mainWindow.Show();
                Hide();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Notification", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

    }
}
