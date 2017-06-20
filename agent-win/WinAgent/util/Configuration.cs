using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.util
{
    public class Configuration
    {
        public String Os { get; set; }
        public String DefaultLevel { get; set; }
        public List<String> Paths { get; set; }
        public List<String> Regexes { get; set; }
        public List<String> Patterns { get; set; }
        public String PrivateKey { get; set; }
        public String PublicKey { get; set; }
        public String SecretKey { get; set; }
        public long ProjectId { get; set; }
        public long AgentId { get; set; }
        public List<String> Types { get; set; }
    }
}
