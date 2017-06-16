using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.model
{
    class LogError
    {
        public string Type { get; set; }
        public string Error { get; set; }
        public string Errno { get; set; }
        public string Stack { get; set; }
    }

}