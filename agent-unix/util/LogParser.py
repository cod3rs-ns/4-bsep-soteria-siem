import re
import os
import subprocess

from domain.Log import Log


class LogParser(object):
    def __init__(self, default_log_level, patterns, project_id, agent_id):
        self.DEFAULT_LOG_LEVEL = default_log_level
        self.SUPPORTED_TYPES = ['.txt', '.log']

        self._regexes = patterns['regexes']
        self._log_patterns = patterns['log_patterns']
        self._project_id = project_id
        self._agent_id = agent_id

    def parse_logs(self, logs):
        for log in logs:
            self.parse_log(log)

    def parse_log(self, log):
        return self.export_log(log)

    def export_log(self, log):
        for regex_id, pattern in enumerate(self._regexes):
            regex = re.compile(pattern)

            if regex.match(log) is None:
                continue

            formatted_log = [x.strip() for x in re.split(regex, log) if ('' != x)]

            log = Log(level=self.DEFAULT_LOG_LEVEL, project=self._project_id, agent=self._agent_id)
            for idx, key in enumerate(self._log_patterns[regex_id].split("|")):
                if key != '-':
                    setattr(log, key, formatted_log[idx])

            return log

    @staticmethod
    def read(path):
        with open(path) as log_file:
            return log_file.readlines()

    @staticmethod
    def read_last(path):
        return subprocess.check_output(['tail', '-1', path])

    def list_log_files(self, directory_base_path):
        logs = []

        def add_paths(base_path):
            if not os.path.isdir(base_path):
                if any(ext in base_path for ext in self.SUPPORTED_TYPES):
                    logs.append(os.path.abspath(base_path))
                return

            for path in os.listdir(base_path):
                add_paths(os.path.join(base_path, path))

        add_paths(directory_base_path)

        return logs
