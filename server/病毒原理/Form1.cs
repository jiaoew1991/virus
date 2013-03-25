using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets; 


namespace 病毒原理
{
    public partial class Form1 : Form
    {
        public string serverIP;
        public int serverPort;
        public IPAddress serverIPAddress;
        public Socket tcpClient; 
        public Form1()
        {
            InitializeComponent();
        }

        /*
         * 初始化socket
         */
        private void btn_submit_Click(object sender, System.EventArgs e)
        {
            serverIP = ipBox.Text;
            serverIPAddress = IPAddress.Parse(serverIP);
            serverPort = int.Parse(portBox.Text);
            tcpClient = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            tcpClient.Connect(serverIPAddress, serverPort);

            //若不能连接，报错
            if (tcpClient == null)
            {
                MessageBox.Show("无法连接到服务器，请重试！",
                                "错误",
                                MessageBoxButtons.OK,
                                MessageBoxIcon.Exclamation);
            }
            else
            {
                MessageBox.Show("成功");
            }
    
        }

        /*
         * 操作1：
         */
        private void btn1_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("1");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }


        /*
         * 操作2：
         */
        private void btn2_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("2");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }

        /*
         * 操作3：
         */
        private void btn3_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("3");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }

        /*
         * 操作4：
         */
        private void btn4_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("4");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }

        /*
         * 操作5：
         */
        private void btn5_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("5");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }

        /*
         * 操作6：
         */
        private void btn6_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("6");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }

        /*
         * 操作7：
         */
        private void btn7_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("7");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }

        /*
         * 操作8：
         */
        private void btn8_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("8");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }

        /*
         * 操作9：
         */
        private void btn9_Click(object sender, EventArgs e)
        {
            byte[] buffer = Encoding.ASCII.GetBytes("9");
            tcpClient.Send(buffer, buffer.Length, 0); 
        }


    }
}
