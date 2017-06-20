using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Configuration;
using System.Threading.Tasks;
using WinAgent.model;
using WinAgent.util;
using System.IO;
using System.Net;

namespace WinAgent
{
    class Program
    {
        private static void Main(string[] args)
        {
            Console.WriteLine("---------- Start SIEM agent -----------");
            ServicePointManager.ServerCertificateValidationCallback += (sender, cert, chain, sslPolicyErrors) => true;

            LogMonitoring.StartWatch();

            LogGenerator.GenerateLogs();

            Console.ReadLine();
        }       
    }
}
