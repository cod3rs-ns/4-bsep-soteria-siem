using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class Log
    {
        [JsonConverter(typeof(StringEnumConverter))]
        public LogLevel Level { get; set; }
        public DateTime Time { get; set; }
        public string Message { get; set; }
        public string Host { get; set; }
        public string Source { get; set; }
        
        public Log(LogLevel level, DateTime time, string message, string host, string source)
        {
            this.Level = level;
            this.Time = time;
            this.Message = message;
            this.Host = host;
            this.Source = source;
        }
    }
}
