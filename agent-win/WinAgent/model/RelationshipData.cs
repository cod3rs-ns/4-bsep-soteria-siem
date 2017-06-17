using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class RelationshipData
    {
        public string Type { get; set; }
        public string Id { get; set; }

        public RelationshipData() { }

        public RelationshipData(string type, string id)
        {
            this.Type = type;
            this.Id = id;
        }

    }
}
