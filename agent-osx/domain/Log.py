class Log(object):
    def __init__(self, level, time, info, message, project):
        self.level = level
        self.time = time
        self.info = info
        self.message = message
        self.project = project


class LogInfo(object):
    def __init__(self, host, source, pid, gid, uid, errors=[]):
        self.host = host
        self.source = source
        self.pid = pid
        self.gid = gid
        self.uid = uid
        self.errors = errors


class LogError(object):
    def __init__(self, _type, error, errno, stack):
        self.type = _type
        self.error = error
        self.errno = errno
        self.stack = stack
