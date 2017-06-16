using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class LogRequestData
    {
        public string Type { get; set; }
        public LogAttributes Attributes { get; set; }
        public LogRequestRelationships Relationships { get; set; }

        public LogRequestData()
        {
        }

        public LogRequestData(Log log)
        {
            this.Type = "logs";
            this.Attributes = new LogAttributes(log);
            this.Relationships = new LogRequestRelationships();
        }
    }
}
