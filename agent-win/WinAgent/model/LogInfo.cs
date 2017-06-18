using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinAgent.util;

namespace WinAgent.model
{
    class LogInfo
    {
        public string Host { get; set; }
        public string Source { get; set; }
        public string Pid { get; set; }
        public string Gid { get; set; }
        public string Uid { get; set; }
        public string Platform { get; set; }
        public List<LogError> Errors { get; set; }

        public LogInfo()
        {
            this.Errors = new List<LogError>();
        }

        public LogInfo(Log log)
        {
            this.Host = EnvUtil.Configuration.MacAddress;
            this.Source = log.Host;
            this.Errors = new List<LogError>();
            this.Platform = EnvUtil.Configuration.Properties.Os;
            this.Pid = log.Pid;
        }
    }

}
