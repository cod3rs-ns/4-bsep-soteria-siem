import json


class LogRequest(object):
    def __init__(self, log):
        self.data = LogRequestData(log)

    def json(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)


class LogRequestData(object):
    def __init__(self, log):
        self.type = "logs"
        self.attributes = LogRequestAttributes(log)
        self.relationships = LogRequestRelationships(log)


class LogRequestAttributes(object):
    def __init__(self, log):
        self.level = log.level.upper()
        self.time = log.time
        self.info = LogInfo(log.info)
        self.message = log.message


class LogInfo(object):
    def __init__(self, info):
        self.host = info.host
        self.source = info.source
        self.pid = info.pid
        self.gid = info.gid
        self.uid = info.uid
        self.errors = [LogInfoError(error) for error in info.errors]


class LogInfoError(object):
    def __init__(self, error):
        self.type = error.type
        self.error = error.error
        self.errno = error.errno
        self.stack = error.stack


class LogRequestRelationships(object):
    def __init__(self, log):
        self.project = ProjectRequestRelationship(log.project)


class ProjectRequestRelationship(object):
    def __init__(self, project_id):
        self.data = RelationshipData(project_id)


class RelationshipData(object):
    def __init__(self, project_id):
        self.type = "projects"
        self.id = project_id
