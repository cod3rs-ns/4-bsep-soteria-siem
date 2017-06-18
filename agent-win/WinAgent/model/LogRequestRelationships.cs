using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinAgent.util;

namespace WinAgent.model
{
    class LogRequestRelationships
    {
        public RequestRelationship Project { get; set; }
        public RequestRelationship Agent { get; set; }

        public LogRequestRelationships()
        {
            this.Project = new RequestRelationship(new RelationshipData("projects", EnvUtil.Configuration.Properties.ProjectId.ToString()));
            this.Agent = new RequestRelationship(new RelationshipData("agents", EnvUtil.Configuration.Properties.ProjectId.ToString()));
        }

    }
}