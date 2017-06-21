using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinAgent.model;

namespace WinAgent.util
{
    class LogMonitoring
    {
        public static List<string> types = EnvUtil.Configuration.Properties.Types;
        
        public static void StartWatch()
        {
            foreach(string type in types) {
                var logName = type;
                if (type.Equals("Firewall")) {
                    logName = "Security";
                }

                EventLog myLog = new EventLog(logName);
                // set event handler
                myLog.EntryWritten += new EntryWrittenEventHandler(OnEntryWritten);
                myLog.EnableRaisingEvents = true;
            }
        }

        private static void OnEntryWritten(object source, EntryWrittenEventArgs e)
        {
            LogLevel logLevel;
            var logEntry = e.Entry;
            if (!Enum.TryParse(logEntry.Category, true, out logLevel)) {
                Enum.TryParse(EnvUtil.Configuration.Properties.DefaultLevel, true, out logLevel);
            }
            var logTime = logEntry.TimeGenerated;
            var logMessage = logEntry.Message;
            var logHost = logEntry.MachineName;
            var logSource = logEntry.Source;
            var pid = logEntry.InstanceId;

            var log = new Log(logLevel, logTime, logMessage, logHost, logSource, pid.ToString());
            HttpClient.SendLogToSIEMServer(log);
        }
    }
}
