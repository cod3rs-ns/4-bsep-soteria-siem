import requests
import subprocess

from LogRequest import LogRequest


class LogsProxy(object):
    # FIXME Change Base service URL
    def __init__(self, config, url):
        self.SERVICE_URL = url
        self.PRIVATE_KEY = config['privateKey']
        self.SECRET_KEY = config['secretKey']
        self.PUBLIC_KEY = config['publicKey']

    def send_log(self, log):
        data = self.get_encrypted_data(LogRequest(log).json())
        requests.post(self.SERVICE_URL, data=data, headers={'Content-Type': 'text/plain'})

    def get_encrypted_data(self, data):
        data.replace('\"', '\\\"')
        command = ["java", "-jar", "request_encryptor.jar", data, self.PRIVATE_KEY, self.SECRET_KEY, self.PUBLIC_KEY]

        return "".join(self.run_command(command))

    @staticmethod
    def run_command(command):
        return iter(subprocess.Popen(command,
                             stdout=subprocess.PIPE,
                             stderr=subprocess.STDOUT)
                    .stdout.readline, b'')
