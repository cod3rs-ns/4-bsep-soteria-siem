using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class LogInfo
    {
        public string Host { get; set; }
        public string Source { get; set; }
        public string Pid { get; set; }
        public string Gid { get; set; }
        public string Uid { get; set; }
        public List<LogError> Errors { get; set; }

        public LogInfo()
        {
            this.Errors = new List<LogError>();
        }

        public LogInfo(Log log)
        {
            this.Host = log.Host;
            this.Source = log.Source;
            this.Errors = new List<LogError>();
        }
    }
}
