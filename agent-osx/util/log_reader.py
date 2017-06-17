import re


def read(path):
    with open(path) as log_file:
        return log_file.readlines()


def parse_logs(logs, regexes):
    regex = re.compile(regexes[0])
    print regex
    for log in logs:
        print log
        print regex.match(log)


def export_log(regexes):
    pass
