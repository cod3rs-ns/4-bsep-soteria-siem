package bsep.sw.hateoas.agent_config;

import java.util.List;

public class AgentConfigAttributes {

    private Long agentId;
    private String os;
    private String defaultLevel;
    private List<String> paths;
    private List<String> regexes;
    private List<String> patterns;
    private List<String> types;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(final Long agentId) {
        this.agentId = agentId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(final String os) {
        this.os = os;
    }

    public String getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(final String defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(final List<String> paths) {
        this.paths = paths;
    }

    public List<String> getRegexes() {
        return regexes;
    }

    public void setRegexes(final List<String> regexes) {
        this.regexes = regexes;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(final List<String> patterns) {
        this.patterns = patterns;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(final List<String> types) {
        this.types = types;
    }
}
