using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.util
{
    public class Configuration
    {
        public string Os { get; set; }
        public string DefaultLevel { get; set; }
        public List<string> Paths { get; set; }
        public List<string> Regexes { get; set; }
        public List<string> Patterns { get; set; }
        public string PrivateKey { get; set; }
        public string PublicKey { get; set; }
        public string SecretKey { get; set; }
        public long ProjectId { get; set; }
        public long AgentId { get; set; }
        public List<string> Types { get; set; }
    }
}
