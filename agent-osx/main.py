import util.yaml_reader as config
import util.log_reader as logs

if __name__ == "__main__":
    conf = config.read()

    regexes = ['([\d\-: ,])+([\[ \d\]])+(INFO|ERROR)([ \-])+']

    all_logs = logs.read(conf['paths'][0] + '/PyCharm2017.1/idea.log')
    logs.parse_logs(all_logs, regexes)
