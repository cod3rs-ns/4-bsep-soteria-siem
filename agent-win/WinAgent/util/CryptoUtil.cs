using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinAgent.util
{
    class CryptoUtil
    {
        public static String CreateRequest(String data)
        {
            String privateKey = EnvUtil.Configuration.Properties.PrivateKey;
            String publicKey = EnvUtil.Configuration.Properties.PublicKey;
            String secretKey = EnvUtil.Configuration.Properties.SecretKey;

            Process process = new Process();
            process.StartInfo = new ProcessStartInfo("java", "-jar main.jar \"" + data + "\" " + privateKey + " " + secretKey + " " + publicKey);
            process.StartInfo.RedirectStandardOutput = true;
            process.StartInfo.UseShellExecute = false;
            process.Start();

            String request = process.StandardOutput.ReadToEnd();
            process.WaitForExit();

            return request;
        }
    }
}
