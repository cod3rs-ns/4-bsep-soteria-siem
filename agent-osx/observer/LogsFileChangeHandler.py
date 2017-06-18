import os
from watchdog.events import FileSystemEventHandler

from util.LogParser import LogParser


class LogsFileChangeHandler(FileSystemEventHandler):

    def __init__(self, parser, log_proxy):
        self.SUPPORTED_TYPES = ['.txt', '.log']
        self._parser = parser
        self._log_proxy = log_proxy

    def on_modified(self, event):
        path = event.src_path
        if not os.path.isdir(path) and any(ext in path for ext in self.SUPPORTED_TYPES):
            self.handle_new_log(path)

    def handle_new_log(self, file_path):
        log = LogParser.read_last(file_path)
        formatted_log = self._parser.parse_log(log)

        if formatted_log is not None:
            self._log_proxy.send_log(formatted_log)
        else:
            print "Log '{}' hasn't proper format!".format(log)
