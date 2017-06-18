import yaml


def read(path="config.yml"):
    with open(path, 'r') as stream:
        return yaml.load(stream)
