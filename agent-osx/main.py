import time
import util.yaml_reader as config

from watchdog.observers import Observer
from util.LogParser import LogParser
from external.LogsProxy import LogsProxy
from observer.LogsFileChangeHandler import LogsFileChangeHandler


if __name__ == "__main__":
    conf = config.read()

    patterns = {
        'regexes':      conf['regexes'],
        'log_patterns': conf['patterns']
    }

    parser = LogParser(conf['defaultLevel'], patterns, project_id=conf['projectId'], agent_id=conf['agentId'])

    event_handler = LogsFileChangeHandler(parser, log_proxy=LogsProxy(config=conf))
    observer = Observer()
    for log_path in conf['paths']:
        try:
            observer.schedule(event_handler, path=log_path, recursive=True)
        except OSError:
            print "No such file or directory: {}".format(log_path)
    observer.start()

    print "Started observing for incoming logs on the following paths: "
    for path in conf['paths']:
        print ' - {}'.format(path)

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()
