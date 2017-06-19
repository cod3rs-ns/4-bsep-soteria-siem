import random
import uuid
import shutil
import os
import datetime
import time

from domain.Log import Log


class LogsGenerator(object):
    """
    Logs generator serves to create random directories with random files in it.
    Then, we occasionally generates Log in advanced known format:

                LEVEL   TIME    PID     MESSAGE

    After log is added to file, it needs to be distributed through secured request
    to SIEM center. 
    """

    def __init__(self, root_directory):
        self.SUPPORTED_FILES = ['.txt', '.log']
        self._root = root_directory
        self._log_files = []

    def generate_log(self):
        levels = ['INFO', 'ERROR', 'WARN', 'TRACE', 'EMERG', 'DEBUG']

        level = levels[random.randint(0, len(levels) - 1)]
        time = str(datetime.datetime.today())
        pid = random.randint(1, 1337)
        message = self.get_random_log_message()

        return Log(pid, level, time, message)

    def save_log(self, log):
        log_format = "\n {} {} {} {}".format(log.level, log.time, log.info.pid, log.message)
        log_file_path = self._log_files[random.randint(0, len(self._log_files) - 1)]

        with open(log_file_path, "a") as log_file:
            log_file.write(log_format)

    def init(self):
        directory = os.path.dirname(self._root)
        if not os.path.exists(directory):
            os.makedirs(directory)

        directories_no = random.randint(1, 5)
        for i in xrange(directories_no):
            dir_name = self.get_random_name() + '/'
            os.makedirs(self._root + dir_name)

            for j in xrange(random.randint(1, 3)):
                self._generate_log_file(dir_name)

    def clean(self):
        shutil.rmtree(self._root)

    def _generate_log_file(self, dir_name):
        extension = self.SUPPORTED_FILES[random.randint(0, 1)]
        file_path = self._root + dir_name + self.get_random_name() + extension
        open(file_path, 'w+')
        self._log_files.append(file_path)

    @staticmethod
    def get_random_log_message():
        messages = [
            'Message 1', 'Message 2', 'Message 3', 'Message 4'
        ]
        return messages[random.randint(0, len(messages) - 1)]

    @staticmethod
    def get_random_name():
        return str(uuid.uuid4()).upper().replace("-", "")[0:7]


if __name__ == "__main__":
    generator = LogsGenerator('/Users/dmarjanovic/Desktop/test/')

    generator.init()

    number_logs_to_generate = random.randint(100, 1000)
    for i in xrange(number_logs_to_generate):
        log = generator.generate_log()
        generator.save_log(log)

        # Wait for another log
        time.sleep(random.randint(5, 30)/10)

    generator.clean()
