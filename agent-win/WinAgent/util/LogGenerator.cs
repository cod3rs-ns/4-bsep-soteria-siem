using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.util
{
    public class LogGenerator
    {
        private static Random random = new Random();

        public static void GenerateLogs() {
            var t = new System.Threading.Timer(o => GenerateSingleLog(), null, 0, 10000);
        }

        private static void GenerateSingleLog() {
            var type = GenerateType();
            var level = (EventLogEntryType) Math.Pow(2, random.Next(0,5));

            EventLog myLog = new EventLog(type);
            EventLog.WriteEntry("AnySource", String.Format("Generate {0} on windows agent demo.", level), level, random.Next(0, 1000));
        }

        private static string GenerateType() {
            switch (random.Next(0, 3)) {
                case 0:
                    return "System";
                case 1:
                    return "Security";
                default:
                    return "Application";
            }
        }
    }

}
