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
        public static string watchLog = EnvUtil.Configuration.Properties.Types;

        public static void StartWatch()
        {
            EventLog myLog = new EventLog(watchLog);

            // set event handler
            myLog.EntryWritten += new EntryWrittenEventHandler(OnEntryWritten);
            myLog.EnableRaisingEvents = true;
        }

        private static void OnEntryWritten(object source, EntryWrittenEventArgs e)
        {
            Log log = GetLogEntryStats(watchLog);
            HttpClient.SendLogToSIEMServer(log);
        }

        private static Log GetLogEntryStats(string logName)
        {
            int e = 0;

            EventLog log = new EventLog(logName);
            e = log.Entries.Count - 1; // last entry

            LogLevel logLevel;
            Enum.TryParse(log.Entries[e].Category, true, out logLevel);
            var logTime = log.Entries[e].TimeGenerated;
            var logMessage = log.Entries[e].Message;
            var logHost = log.Entries[e].MachineName;
            var logSource = log.Entries[e].Source;
            var pid = log.Entries[e].InstanceId;

            log.Close();	// close log

            return new Log(logLevel, logTime, logMessage, logHost, logSource, pid.ToString());
        }
    }
}
