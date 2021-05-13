using System;
using System.Collections.Generic;
using System.Data;
using System.Windows.Forms;
using Domain;
using Domain.DTO;

namespace Client
{
    public partial class MainWindow : Form
    {
        private readonly ClientController Controller;

        private DataTable EventSignedUpDataTable;
        private readonly DataTable AgeGroupEventDataTable;

        public MainWindow(ClientController controller)
        {
            InitializeComponent();

            Controller = controller;

            EventSignedUpDataTable = new DataTable();
            EventSignedUpDataTable.Columns.Add("Event", typeof(Event));
            EventSignedUpDataTable.Columns.Add("Age Group", typeof(AgeGroup));
            EventSignedUpDataTable.Columns.Add("Signed Up", typeof(long));

            AgeGroupEventDataTable = new DataTable();
            AgeGroupEventDataTable.Columns.Add("Name", typeof(string));
            AgeGroupEventDataTable.Columns.Add("Age", typeof(int));
            AgeGroupEventDataTable.Columns.Add("No. Events", typeof(int));

            foreach (AgeGroup a in Enum.GetValues(typeof(AgeGroup)))
            {
                AgeGroupComboBox.Items.Add(a);
            }
            FirstEventComboBox.Items.Add(Event.E50M);
            FirstEventComboBox.Items.Add(Event.E100M);

            AgeGroupEventGridView.DataSource = AgeGroupEventDataTable;

            foreach (EventCountDTO e in Controller.GetEventsNumber())
            {
                EventSignedUpDataTable.Rows.Add(e.Event, e.AgeGroup, e.Count);
            }
            EventSignedUpGridView.DataSource = EventSignedUpDataTable;

            Controller.UpdateEvent += UserUpdate;

        }

        private void LogoutButton_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void MainWindow_FormClosed(object sender, FormClosedEventArgs e)
        {
            Controller.Logout();
            Controller.UpdateEvent -= UserUpdate;
            Application.Exit();
        }

        public void UserUpdate(object sender, UserEventArgs e)
        {
            if (e.UserEventType == UserEvent.RefreshEvents)
            {
                IEnumerable<EventCountDTO> lst = (IEnumerable<EventCountDTO>)e.Data;

                EventSignedUpDataTable = new DataTable();
                EventSignedUpDataTable.Columns.Add("Event", typeof(Event));
                EventSignedUpDataTable.Columns.Add("Age Group", typeof(AgeGroup));
                EventSignedUpDataTable.Columns.Add("Signed Up", typeof(long));
                //EventSignedUpDataTable.Rows.Clear();

                foreach (EventCountDTO ec in lst)
                {
                    EventSignedUpDataTable.Rows.Add(ec.Event, ec.AgeGroup, ec.Count);
                }
                EventSignedUpGridView.BeginInvoke(new UpdateDGWCallback(UpdateDGW), new object[] { EventSignedUpGridView, EventSignedUpDataTable });
            }
        }

        private void UpdateDGW(DataGridView dataGridView, DataTable dataTable)
        {
            dataGridView.DataSource = null;
            dataGridView.DataSource = dataTable;
        }

        public delegate void UpdateDGWCallback(DataGridView dataGridView, DataTable dataTable);

        private void FilterButton_Click(object sender, EventArgs e)
        {
            AgeGroupEventDataTable.Rows.Clear();
            if (string.IsNullOrEmpty(EventComboBox.Text) || string.IsNullOrEmpty(AgeGroupComboBox.Text))
            {
                MessageBox.Show("Please select an age group and an event.", "Notification", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }
            Enum.TryParse(EventComboBox.Text, out Event eventt);
            Enum.TryParse(AgeGroupComboBox.Text, out AgeGroup ageGroup);
            foreach (ChildNoEventsDTO c in Controller.GetChildrenNoEvents(eventt, ageGroup))
            {
                AgeGroupEventDataTable.Rows.Add(c.Child.Name, c.Child.Age, c.NoEvents);
            }
            AgeGroupEventGridView.DataSource = AgeGroupEventDataTable;

        }

        private void AgeGroupComboBox_TextChanged(object sender, EventArgs e)
        {
            EventComboBox.Items.Clear();
            if (AgeGroupComboBox.Text == AgeGroup.A6_8Y.ToString())
            {
                EventComboBox.Items.Add(Event.E50M);
                EventComboBox.Items.Add(Event.E100M);
            }
            else
            {
                foreach (Event ev in Enum.GetValues(typeof(Event)))
                {
                    if (ev != Event.NONE)
                    {
                        EventComboBox.Items.Add(ev);
                    }

                }
            }
        }

        private void SignUpButton_Click(object sender, EventArgs e)
        {
            string errors = "";
            if (string.IsNullOrWhiteSpace(NameTextBox.Text))
                errors += "Name is empty.\n";
            if (string.IsNullOrEmpty(FirstEventComboBox.Text))
                errors += "Please choose the first event.\n";
            if (string.IsNullOrEmpty(SecondEventComboBox.Text))
                errors += "Please choose the second event.\n";
            if (errors != "")
            {
                MessageBox.Show(errors, "Notification", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }
            try
            {
                Enum.TryParse(FirstEventComboBox.Text, out Event event1);
                Enum.TryParse(SecondEventComboBox.Text, out Event event2);
                Controller.AddParticipant(NameTextBox.Text, Convert.ToInt32(AgeSpinner.Value), event1, event2);
                MessageBox.Show("Registration complete", "Notification", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.ToString(), "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void AgeSpinner_ValueChanged(object sender, EventArgs e)
        {
            FirstEventComboBox.Items.Clear();
            SecondEventComboBox.Items.Clear();
            if (Convert.ToInt32(AgeSpinner.Value) >= 6 && Convert.ToInt32(AgeSpinner.Value) <= 8)
            {
                FirstEventComboBox.Items.Add(Event.E50M);
                FirstEventComboBox.Items.Add(Event.E100M);
            }
            else
            {
                foreach (Event ev in Enum.GetValues(typeof(Event)))
                {
                    if (ev != Event.NONE)
                    {
                        FirstEventComboBox.Items.Add(ev);
                    }

                }
            }
        }

        private void FirstEventComboBox_TextChanged(object sender, EventArgs e)
        {
            SecondEventComboBox.Items.Clear();
            foreach (Event ev in Enum.GetValues(typeof(Event)))
            {
                if ((ev.ToString() != FirstEventComboBox.Text && FirstEventComboBox.Items.Contains(ev)) || ev == Event.NONE)
                {
                    SecondEventComboBox.Items.Add(ev);
                }
            }
        }

    }
}
