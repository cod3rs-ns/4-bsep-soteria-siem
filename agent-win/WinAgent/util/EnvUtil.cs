using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net.NetworkInformation;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.util
{
    public class EnvUtil
    {
        private static EnvUtil Instance;
        public Configuration Properties {get; set;}
        public string MacAddress { get; set; }

        private EnvUtil()
        {
            string path = "config.json";

            if (File.Exists(path))
            {
                if (File.Exists("licenseDecryptor.jar") && File.Exists("license"))
                {
                    string data = File.ReadAllText(path);
                    string key = File.ReadAllText("license");

                    Process process = new Process();

                    var escapedData = data.Trim().Replace("\\", "\\\\").Replace("\"", "\\\"");
                    var escapedKey = key.Trim().Replace("\\", "\\\\").Replace("\"", "\\\"");

                    process.StartInfo = new ProcessStartInfo("java", "-jar licenseDecryptor.jar \"" + escapedData + "\" " + "\"" + escapedKey + "\"");
                    process.StartInfo.RedirectStandardOutput = true;
                    process.StartInfo.UseShellExecute = false;
                    process.Start();

                    String decryptedJson = process.StandardOutput.ReadToEnd();
                    process.WaitForExit();

                    try {
                        Properties = JsonConvert.DeserializeObject<Configuration>(decryptedJson,
                            new JsonSerializerSettings
                            {
                                ContractResolver = new CamelCasePropertyNamesContractResolver()
                            });
                    } catch (Exception e) {
                        Console.WriteLine("Wrong configuration specified");
                    }
                }
            }

            MacAddress =
            (
                from nic in NetworkInterface.GetAllNetworkInterfaces()
                where nic.OperationalStatus == OperationalStatus.Up
                select nic.GetPhysicalAddress().ToString()
            ).FirstOrDefault();
        }

        public static EnvUtil Configuration
        {
            get
            {
                if (Instance == null) 
                {
                    Instance = new EnvUtil();
                }
                return Instance;
            }
        }
    }
}
