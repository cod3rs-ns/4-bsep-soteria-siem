import platform
import uuid
import os


class Log(object):
    def __init__(self, level, time, info, message, project):
        self.level = level
        self.time = time
        self.info = info
        self.message = message
        self.project = project


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
