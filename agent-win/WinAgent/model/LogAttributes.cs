using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class LogAttributes
    {
        [JsonConverter(typeof(StringEnumConverter))]
        public LogLevel Level { get; set; }
        public string Time { get; set; }
        public LogInfo Info { get; set; }
        public string Message { get; set; }

        public LogAttributes() { }

        public LogAttributes(Log log)
        {
            this.Message = log.Message;
            this.Level = log.Level;
            this.Info = new LogInfo(log);
            this.Time = log.Time.ToString("yyyy-MM-dd'T'HH:mm:ssZ");
        }

    }
}
