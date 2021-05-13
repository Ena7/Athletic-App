
namespace Client
{
    partial class MainWindow
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.LogoutButton = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.EventSignedUpGridView = new System.Windows.Forms.DataGridView();
            this.tabControl1 = new System.Windows.Forms.TabControl();
            this.tabPage1 = new System.Windows.Forms.TabPage();
            this.FilterButton = new System.Windows.Forms.Button();
            this.EventComboBox = new System.Windows.Forms.ComboBox();
            this.AgeGroupComboBox = new System.Windows.Forms.ComboBox();
            this.label5 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.AgeGroupEventGridView = new System.Windows.Forms.DataGridView();
            this.tabPage2 = new System.Windows.Forms.TabPage();
            this.label13 = new System.Windows.Forms.Label();
            this.AgeSpinner = new System.Windows.Forms.NumericUpDown();
            this.SecondEventComboBox = new System.Windows.Forms.ComboBox();
            this.FirstEventComboBox = new System.Windows.Forms.ComboBox();
            this.NameTextBox = new System.Windows.Forms.TextBox();
            this.SignUpButton = new System.Windows.Forms.Button();
            this.label11 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label12 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.EventSignedUpGridView)).BeginInit();
            this.tabControl1.SuspendLayout();
            this.tabPage1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.AgeGroupEventGridView)).BeginInit();
            this.tabPage2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.AgeSpinner)).BeginInit();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Yi Baiti", 30F);
            this.label1.ForeColor = System.Drawing.Color.MidnightBlue;
            this.label1.Location = new System.Drawing.Point(12, 12);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(189, 40);
            this.label1.TabIndex = 1;
            this.label1.Text = "AthleticWare";
            // 
            // LogoutButton
            // 
            this.LogoutButton.BackColor = System.Drawing.Color.LightCoral;
            this.LogoutButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold);
            this.LogoutButton.Location = new System.Drawing.Point(721, 12);
            this.LogoutButton.Name = "LogoutButton";
            this.LogoutButton.Size = new System.Drawing.Size(90, 37);
            this.LogoutButton.TabIndex = 5;
            this.LogoutButton.Text = "Logout";
            this.LogoutButton.UseVisualStyleBackColor = false;
            this.LogoutButton.Click += new System.EventHandler(this.LogoutButton_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(606, 68);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(155, 26);
            this.label3.TabIndex = 7;
            this.label3.Text = "Number of participants grouped\r\n by event and age group\r\n";
            this.label3.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // EventSignedUpGridView
            // 
            this.EventSignedUpGridView.AllowUserToAddRows = false;
            this.EventSignedUpGridView.AllowUserToDeleteRows = false;
            this.EventSignedUpGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.EventSignedUpGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.EventSignedUpGridView.Location = new System.Drawing.Point(565, 109);
            this.EventSignedUpGridView.Name = "EventSignedUpGridView";
            this.EventSignedUpGridView.ReadOnly = true;
            this.EventSignedUpGridView.Size = new System.Drawing.Size(246, 302);
            this.EventSignedUpGridView.TabIndex = 8;
            // 
            // tabControl1
            // 
            this.tabControl1.Controls.Add(this.tabPage1);
            this.tabControl1.Controls.Add(this.tabPage2);
            this.tabControl1.Location = new System.Drawing.Point(19, 68);
            this.tabControl1.Name = "tabControl1";
            this.tabControl1.SelectedIndex = 0;
            this.tabControl1.Size = new System.Drawing.Size(524, 358);
            this.tabControl1.TabIndex = 9;
            // 
            // tabPage1
            // 
            this.tabPage1.Controls.Add(this.FilterButton);
            this.tabPage1.Controls.Add(this.EventComboBox);
            this.tabPage1.Controls.Add(this.AgeGroupComboBox);
            this.tabPage1.Controls.Add(this.label5);
            this.tabPage1.Controls.Add(this.label4);
            this.tabPage1.Controls.Add(this.label2);
            this.tabPage1.Controls.Add(this.AgeGroupEventGridView);
            this.tabPage1.Location = new System.Drawing.Point(4, 22);
            this.tabPage1.Name = "tabPage1";
            this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage1.Size = new System.Drawing.Size(516, 332);
            this.tabPage1.TabIndex = 0;
            this.tabPage1.Text = "Entries";
            this.tabPage1.UseVisualStyleBackColor = true;
            // 
            // FilterButton
            // 
            this.FilterButton.BackColor = System.Drawing.Color.LightYellow;
            this.FilterButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.FilterButton.Location = new System.Drawing.Point(53, 225);
            this.FilterButton.Name = "FilterButton";
            this.FilterButton.Size = new System.Drawing.Size(83, 38);
            this.FilterButton.TabIndex = 6;
            this.FilterButton.Text = "Filter";
            this.FilterButton.UseVisualStyleBackColor = false;
            this.FilterButton.Click += new System.EventHandler(this.FilterButton_Click);
            // 
            // EventComboBox
            // 
            this.EventComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.EventComboBox.FormattingEnabled = true;
            this.EventComboBox.Location = new System.Drawing.Point(33, 139);
            this.EventComboBox.Name = "EventComboBox";
            this.EventComboBox.Size = new System.Drawing.Size(133, 21);
            this.EventComboBox.TabIndex = 5;
            // 
            // AgeGroupComboBox
            // 
            this.AgeGroupComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.AgeGroupComboBox.FormattingEnabled = true;
            this.AgeGroupComboBox.Location = new System.Drawing.Point(33, 78);
            this.AgeGroupComboBox.Name = "AgeGroupComboBox";
            this.AgeGroupComboBox.Size = new System.Drawing.Size(133, 21);
            this.AgeGroupComboBox.TabIndex = 4;
            this.AgeGroupComboBox.TextChanged += new System.EventHandler(this.AgeGroupComboBox_TextChanged);
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(30, 163);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(123, 39);
            this.label5.TabIndex = 3;
            this.label5.Text = "*Only the 50m and 100m\r\nevents are available for\r\nthe 6-8 age group.";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(30, 123);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(35, 13);
            this.label4.TabIndex = 2;
            this.label4.Text = "Event";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(30, 62);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(56, 13);
            this.label2.TabIndex = 1;
            this.label2.Text = "Age group";
            // 
            // AgeGroupEventGridView
            // 
            this.AgeGroupEventGridView.AllowUserToAddRows = false;
            this.AgeGroupEventGridView.AllowUserToDeleteRows = false;
            this.AgeGroupEventGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.AgeGroupEventGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.AgeGroupEventGridView.Location = new System.Drawing.Point(196, 19);
            this.AgeGroupEventGridView.Name = "AgeGroupEventGridView";
            this.AgeGroupEventGridView.ReadOnly = true;
            this.AgeGroupEventGridView.Size = new System.Drawing.Size(303, 302);
            this.AgeGroupEventGridView.TabIndex = 0;
            // 
            // tabPage2
            // 
            this.tabPage2.Controls.Add(this.label13);
            this.tabPage2.Controls.Add(this.AgeSpinner);
            this.tabPage2.Controls.Add(this.SecondEventComboBox);
            this.tabPage2.Controls.Add(this.FirstEventComboBox);
            this.tabPage2.Controls.Add(this.NameTextBox);
            this.tabPage2.Controls.Add(this.SignUpButton);
            this.tabPage2.Controls.Add(this.label11);
            this.tabPage2.Controls.Add(this.label10);
            this.tabPage2.Controls.Add(this.label9);
            this.tabPage2.Controls.Add(this.label8);
            this.tabPage2.Controls.Add(this.label7);
            this.tabPage2.Controls.Add(this.label6);
            this.tabPage2.Location = new System.Drawing.Point(4, 22);
            this.tabPage2.Name = "tabPage2";
            this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage2.Size = new System.Drawing.Size(516, 332);
            this.tabPage2.TabIndex = 1;
            this.tabPage2.Text = "Sign up";
            this.tabPage2.UseVisualStyleBackColor = true;
            // 
            // label13
            // 
            this.label13.AutoSize = true;
            this.label13.Location = new System.Drawing.Point(417, 209);
            this.label13.Name = "label13";
            this.label13.Size = new System.Drawing.Size(50, 13);
            this.label13.TabIndex = 15;
            this.label13.Text = "*Optional";
            // 
            // AgeSpinner
            // 
            this.AgeSpinner.Location = new System.Drawing.Point(175, 119);
            this.AgeSpinner.Maximum = new decimal(new int[] {
            15,
            0,
            0,
            0});
            this.AgeSpinner.Minimum = new decimal(new int[] {
            6,
            0,
            0,
            0});
            this.AgeSpinner.Name = "AgeSpinner";
            this.AgeSpinner.ReadOnly = true;
            this.AgeSpinner.Size = new System.Drawing.Size(107, 20);
            this.AgeSpinner.TabIndex = 14;
            this.AgeSpinner.Value = new decimal(new int[] {
            6,
            0,
            0,
            0});
            this.AgeSpinner.ValueChanged += new System.EventHandler(this.AgeSpinner_ValueChanged);
            // 
            // SecondEventComboBox
            // 
            this.SecondEventComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.SecondEventComboBox.FormattingEnabled = true;
            this.SecondEventComboBox.Location = new System.Drawing.Point(175, 206);
            this.SecondEventComboBox.Name = "SecondEventComboBox";
            this.SecondEventComboBox.Size = new System.Drawing.Size(236, 21);
            this.SecondEventComboBox.TabIndex = 13;
            // 
            // FirstEventComboBox
            // 
            this.FirstEventComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.FirstEventComboBox.FormattingEnabled = true;
            this.FirstEventComboBox.Location = new System.Drawing.Point(175, 179);
            this.FirstEventComboBox.Name = "FirstEventComboBox";
            this.FirstEventComboBox.Size = new System.Drawing.Size(236, 21);
            this.FirstEventComboBox.TabIndex = 12;
            this.FirstEventComboBox.TextChanged += new System.EventHandler(this.FirstEventComboBox_TextChanged);
            // 
            // NameTextBox
            // 
            this.NameTextBox.Location = new System.Drawing.Point(175, 93);
            this.NameTextBox.MaxLength = 16;
            this.NameTextBox.Name = "NameTextBox";
            this.NameTextBox.Size = new System.Drawing.Size(236, 20);
            this.NameTextBox.TabIndex = 11;
            // 
            // SignUpButton
            // 
            this.SignUpButton.BackColor = System.Drawing.Color.PaleGreen;
            this.SignUpButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold);
            this.SignUpButton.Location = new System.Drawing.Point(216, 261);
            this.SignUpButton.Name = "SignUpButton";
            this.SignUpButton.Size = new System.Drawing.Size(90, 37);
            this.SignUpButton.TabIndex = 10;
            this.SignUpButton.Text = "Sign up";
            this.SignUpButton.UseVisualStyleBackColor = false;
            this.SignUpButton.Click += new System.EventHandler(this.SignUpButton_Click);
            // 
            // label11
            // 
            this.label11.AutoSize = true;
            this.label11.Location = new System.Drawing.Point(288, 123);
            this.label11.Name = "label11";
            this.label11.Size = new System.Drawing.Size(123, 39);
            this.label11.TabIndex = 5;
            this.label11.Text = "*Only the 50m and 100m\r\nevents are available for\r\nthe 6-8 age group.";
            // 
            // label10
            // 
            this.label10.AutoSize = true;
            this.label10.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label10.Location = new System.Drawing.Point(74, 211);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(75, 16);
            this.label10.TabIndex = 4;
            this.label10.Text = "2nd event";
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label9.Location = new System.Drawing.Point(79, 184);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(70, 16);
            this.label9.TabIndex = 3;
            this.label9.Text = "1st event";
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label8.Location = new System.Drawing.Point(114, 123);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(36, 16);
            this.label8.TabIndex = 2;
            this.label8.Text = "Age";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label7.Location = new System.Drawing.Point(101, 97);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(49, 16);
            this.label7.TabIndex = 1;
            this.label7.Text = "Name";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Font = new System.Drawing.Font("Constantia", 27F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label6.Location = new System.Drawing.Point(120, 19);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(291, 44);
            this.label6.TabIndex = 0;
            this.label6.Text = "Registration form";
            // 
            // label12
            // 
            this.label12.AutoSize = true;
            this.label12.Font = new System.Drawing.Font("MV Boli", 10F, System.Drawing.FontStyle.Italic);
            this.label12.ForeColor = System.Drawing.Color.MidnightBlue;
            this.label12.Location = new System.Drawing.Point(192, 31);
            this.label12.Name = "label12";
            this.label12.Size = new System.Drawing.Size(79, 18);
            this.label12.TabIndex = 10;
            this.label12.Text = "C# Edition";
            // 
            // MainWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.LightCyan;
            this.ClientSize = new System.Drawing.Size(823, 450);
            this.Controls.Add(this.label12);
            this.Controls.Add(this.tabControl1);
            this.Controls.Add(this.EventSignedUpGridView);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.LogoutButton);
            this.Controls.Add(this.label1);
            this.Name = "MainWindow";
            this.Text = "Control Panel";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.MainWindow_FormClosed);
            ((System.ComponentModel.ISupportInitialize)(this.EventSignedUpGridView)).EndInit();
            this.tabControl1.ResumeLayout(false);
            this.tabPage1.ResumeLayout(false);
            this.tabPage1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.AgeGroupEventGridView)).EndInit();
            this.tabPage2.ResumeLayout(false);
            this.tabPage2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.AgeSpinner)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button LogoutButton;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.DataGridView EventSignedUpGridView;
        private System.Windows.Forms.TabControl tabControl1;
        private System.Windows.Forms.TabPage tabPage1;
        private System.Windows.Forms.TabPage tabPage2;
        private System.Windows.Forms.DataGridView AgeGroupEventGridView;
        private System.Windows.Forms.Button FilterButton;
        private System.Windows.Forms.ComboBox EventComboBox;
        private System.Windows.Forms.ComboBox AgeGroupComboBox;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.ComboBox SecondEventComboBox;
        private System.Windows.Forms.ComboBox FirstEventComboBox;
        private System.Windows.Forms.TextBox NameTextBox;
        private System.Windows.Forms.Button SignUpButton;
        private System.Windows.Forms.Label label11;
        private System.Windows.Forms.Label label10;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.NumericUpDown AgeSpinner;
        private System.Windows.Forms.Label label12;
        private System.Windows.Forms.Label label13;
    }
}