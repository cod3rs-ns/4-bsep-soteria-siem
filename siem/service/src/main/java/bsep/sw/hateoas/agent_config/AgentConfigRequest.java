package bsep.sw.hateoas.agent_config;

import bsep.sw.domain.Agent;
import bsep.sw.util.AgentKeys;
import net.minidev.json.JSONValue;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AgentConfigRequest {

    private AgentConfigData data;

    public AgentConfigData getData() {
        return data;
    }

    public void setData(final AgentConfigData data) {
        this.data = data;
    }
}
