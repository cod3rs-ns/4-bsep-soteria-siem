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
            var request = (HttpWebRequest)WebRequest.Create("https://localhost:8443/api/logs/agent/" + EnvUtil.Configuration.Properties.AgentId.ToString());

            var postData = Newtonsoft.Json.JsonConvert.SerializeObject(new LogRequest(log),
                new JsonSerializerSettings
                {
                    ContractResolver = new CamelCasePropertyNamesContractResolver()
                });
            
            var encryptedRequest = CryptoUtil.CreateRequest(postData.Replace("\"", "\\\""));
            var data = System.Text.Encoding.UTF8.GetBytes(encryptedRequest);

            request.Method = "POST";
            request.ContentType = "text/plain";
            request.ContentLength = data.Length;

            using (var stream = request.GetRequestStream())
            {
                stream.Write(data, 0, data.Length);
            }

            try
            {
                using (WebResponse response = request.GetResponse())
                {
                    Console.WriteLine(new StreamReader(response.GetResponseStream()).ReadToEnd());
                }
            }
            catch (WebException e)
            {
                using (WebResponse response = e.Response)
                {
                    HttpWebResponse httpResponse = (HttpWebResponse)response;
                    Console.WriteLine("Error code: {0}", httpResponse.StatusCode);
                    using (Stream dataError = response.GetResponseStream())
                    using (var reader = new StreamReader(dataError))
                    {
                        string errorText = reader.ReadToEnd();
                        Console.WriteLine(errorText);
                    }
                }
            }
        }
    }
}
