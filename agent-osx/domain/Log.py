import platform
import uuid
import os


class Log(object):
    # TODO Set default Log Level
    def __init__(self, pid=None, level=None, time=None, message=None, project=1, agent=1):
        self.level = level
        self.time = time
        self.info = LogInfo(pid)
        self.message = message

        self.project = project
        self.agent = agent

    def __setattr__(self, key, value):
        if key == 'pid':
            # Extract only numbers from 'pid' field
            try:
                self.info = LogInfo(int(filter(str.isdigit, value)))
            except ValueError:
                pass

        super(Log, self).__setattr__(key, value)


class LogInfo(object):
    def __init__(self, pid, errors=[]):
        self.host = uuid.getnode()
        self.source = platform.node()
        self.pid = pid
        self.gid = os.getgid()
        self.uid = os.getuid()
        self.errors = errors


class LogError(object):
    def __init__(self, _type, error, errno, stack):
        self.type = _type
        self.error = error
        self.errno = errno
        self.stack = stack
