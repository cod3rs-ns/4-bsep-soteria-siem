import yaml
import subprocess


def read_license(path="license"):
    with open(path, 'r') as stream:
        return stream.readline()


def read_raw_config(path="config.yml"):
    with open(path, 'r') as stream:
        return "".join(stream.readlines())


def read(path="config.yml"):
    license_key = read_license()

    with open(path, 'r') as stream:
        config_data = "".join(stream.readlines())

    yaml_data = get_encrypted_data(config_data, license_key)
    return yaml.load(yaml_data)


def get_encrypted_data(raw_data, license_key):
    command = ["java", "-jar", "license_decrypter.jar", raw_data, license_key]
    return "".join(run_command(command))


def run_command(command):
    return iter(subprocess.Popen(command,
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT).
                stdout.readline, b'')
