import requests

from LogRequest import LogRequest


class LogsProxy(object):
    def __init__(self, url='http://localhost:9091/api/logs'):
        self.SERVICE_URL = url

    def send_log(self, log):
        data = LogRequest(log).json()
        print data
        requests.post(self.SERVICE_URL, data=data, headers={'Content-Type': 'application/json'})
