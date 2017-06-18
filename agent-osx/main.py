import util.yaml_reader as config
from util.LogParser import LogParser


if __name__ == "__main__":
    conf = config.read()

    patterns = {
        'regexes':      conf['regexes'],
        'log_patterns': conf['patterns']
    }

    parser = LogParser(conf['defaultLevel'], patterns, project_id=conf['projectId'])

    for log_paths in conf['paths']:
        for log_file in parser.list_log_files(log_paths):
            parser.parse_logs(parser.read(log_file))
