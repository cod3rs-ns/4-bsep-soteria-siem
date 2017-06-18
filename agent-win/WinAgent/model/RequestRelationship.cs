using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinAgent.util;

namespace WinAgent.model
{
    class RequestRelationship
    {
        public RelationshipData Data { get; set; }

        public RequestRelationship() { }

        public RequestRelationship(RelationshipData data)
        {
            this.Data = data;
        }

    }
}
