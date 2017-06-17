package bsep.sw.hateoas.agent_config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AgentConfigRequest {

    private static final String CONFIG_FILE_NAME = "config.yml";

    private AgentConfigData data;

    public String toYmlFile() throws FileNotFoundException {
        final Map<String, Object> map = new HashMap<>();
        final Yaml yaml = new Yaml();

        map.put("os", data.getAttributes().getOs());
        map.put("defaultLevel", data.getAttributes().getDefaultLevel());
        map.put("paths", data.getAttributes().getPaths());
        map.put("regexes", data.getAttributes().getRegexes());

        final PrintWriter out = new PrintWriter(CONFIG_FILE_NAME);
        out.print(yaml.dump(map));
        out.close();

        return CONFIG_FILE_NAME;
    }

    public AgentConfigData getData() {
        return data;
    }

    public void setData(AgentConfigData data) {
        this.data = data;
    }
}
