package bsep.sw.hateoas.agent_config;

import java.util.List;

public class AgentConfigAttributes {

    private String os;
    private String defaultLevel;
    private List<String> paths;
    private List<String> regexes;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(String defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public List<String> getRegexes() {
        return regexes;
    }

    public void setRegexes(List<String> regexes) {
        this.regexes = regexes;
    }
}
