# OSX agent

### Proposed technology

- Python 2.7.x
- YAML for configuration file

### Description
Agent has configuration file with list of directories/files of valuable logs. 
Each agent should have provided **unique** id/key.
It periodically needs to check for logs changing and, if there's change, send log message(s) to **main SIEP** application.

All types of logs (**operating system**, **web server**, **application logs**, etc.) should be handled with one agent.  

### Implementation
Simple script which reads provided _configuration file_ and runs in background. 
There's no need for GUI. 

Also, I would propose _config file_ in **.yaml** format. Config file should have **list of directories** and **supported types**. 

```
agent-key: 1df2d4c5-20ff-46a2-8369-b8966e51fa82
directories:
    - /Users/dmarjanovic/logs
    - /sys/.logs
types:
    - web
    - system
```
