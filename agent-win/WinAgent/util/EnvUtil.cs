using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using System;
using System.Collections.Generic;
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
                string json = File.ReadAllText(path);
                
                Properties = JsonConvert.DeserializeObject<Configuration>(json,
                    new JsonSerializerSettings
                    {
                        ContractResolver = new CamelCasePropertyNamesContractResolver()
                    });    
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
