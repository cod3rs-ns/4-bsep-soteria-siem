using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinAgent.model;
using System.Net;
using System.IO;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace WinAgent.util
{
    class HttpClient
    {
        public static void SendLogToSIEMServer(Log log)
        {
            var request = (HttpWebRequest)WebRequest.Create(EnvUtil.Configuration.Property("SIEM_CENTER_URL"));

            var postData = Newtonsoft.Json.JsonConvert.SerializeObject(log, 
                new JsonSerializerSettings { 
                    ContractResolver = new CamelCasePropertyNamesContractResolver()
                });
            var data = Encoding.ASCII.GetBytes(postData);

            request.Method = "POST";
            request.ContentType = "application/json";
            request.ContentLength = data.Length;

            using (var stream = request.GetRequestStream())
            {
                stream.Write(data, 0, data.Length);
            }

            var response = (HttpWebResponse)request.GetResponse();
            var responseString = new StreamReader(response.GetResponseStream()).ReadToEnd();

            Console.WriteLine(responseString);
        }
    }
}
