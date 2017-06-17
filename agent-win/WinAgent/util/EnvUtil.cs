using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.util
{
    public class EnvUtil
    {
        private static EnvUtil instance;

        private EnvUtil()
        {
            DotEnv.DotEnvConfig.Install();
        }

        public static EnvUtil Configuration
        {
            get
            {
                if (instance == null) 
                {
                    instance = new EnvUtil();
                }
                return instance;
            }
        }

        public string Property(string key)
        {
            return Environment.GetEnvironmentVariable(key);
        }
    }
}
