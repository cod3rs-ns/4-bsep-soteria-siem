using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class LogRequest
    {
        public LogRequestData Data { get; set; }

        public LogRequest() { }

        public LogRequest(Log log)
        {
            this.Data = new LogRequestData(log);
        }
    }
}
