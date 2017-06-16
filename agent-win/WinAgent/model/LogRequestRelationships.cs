using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class LogRequestRelationships
    {
        public RequestRelationship Project { get; set; }

        public LogRequestRelationships()
        {
            this.Project = new RequestRelationship();
        }
    }
}