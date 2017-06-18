package bsep.sw.domain;


import bsep.sw.hateoas.agent_config.AgentConfigAttributes;
import bsep.sw.util.AgentKeys;
import net.minidev.json.JSONValue;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class AgentConfig {

    private static final String CONFIG_FILE_NAME_YML = "config.yml";
    private static final String CONFIG_FILE_NAME_JSON = "config.json";

    private String os;
    private String defaultLevel;
    private List<String> paths;
    private List<String> regexes;
    private List<String> patterns;
    private String privateKey;
    private String publicKey;
    private String secretKey;
    private Long projectId;
    private Long agentId;
    private List<String> types;

    public AgentConfig(final AgentConfigAttributes attributes, final Agent agent, final AgentKeys agentKeys) {
        this.os = attributes.getOs();
        this.defaultLevel = attributes.getDefaultLevel();
        this.paths = attributes.getPaths();
        this.regexes = attributes.getRegexes();
        this.patterns = attributes.getPatterns();
        this.privateKey = agentKeys.getPrivateKey();
        this.publicKey = agentKeys.getPublicKey();
        this.secretKey = agentKeys.getSecretKey();
        this.projectId = agent.getProject().getId();
        this.agentId = agent.getId();
        this.types = attributes.getTypes();
    }

    public String toYmlFile() throws FileNotFoundException {
        final Representer representer = new Representer();
        representer.addClassTag(AgentConfig.class, Tag.MAP);
        final Yaml yaml = new Yaml(representer);

        final PrintWriter out = new PrintWriter(CONFIG_FILE_NAME_YML);
        out.print(yaml.dump(this));
        out.close();

        return CONFIG_FILE_NAME_YML;
    }

    public String toJsonFile() throws FileNotFoundException {
        final PrintWriter out = new PrintWriter(CONFIG_FILE_NAME_JSON);
        out.print(JSONValue.toJSONString(this));
        out.close();

        return CONFIG_FILE_NAME_JSON;
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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(final Long projectId) {
        this.projectId = projectId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(final Long agentId) {
        this.agentId = agentId;
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
