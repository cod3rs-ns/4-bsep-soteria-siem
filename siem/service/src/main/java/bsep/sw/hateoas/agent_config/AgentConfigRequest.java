package bsep.sw.hateoas.agent_config;

import net.minidev.json.JSONValue;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AgentConfigRequest {

    private static final String CONFIG_FILE_NAME_YML = "config.yml";
    private static final String CONFIG_FILE_NAME_JSON = "config.json";

    private AgentConfigData data;

    public String toYmlFile() throws FileNotFoundException {
        final Map<String, Object> map = new HashMap<>();
        final Yaml yaml = new Yaml();

        map.put("os", data.getAttributes().getOs());
        map.put("defaultLevel", data.getAttributes().getDefaultLevel());
        map.put("paths", data.getAttributes().getPaths());
        map.put("regexes", data.getAttributes().getRegexes());

        final PrintWriter out = new PrintWriter(CONFIG_FILE_NAME_YML);
        out.print(yaml.dump(map));
        out.close();

        return CONFIG_FILE_NAME_YML;
    }

    public String toJsonFile() throws FileNotFoundException {
        final PrintWriter out = new PrintWriter(CONFIG_FILE_NAME_JSON);
        out.print(JSONValue.toJSONString(data.getAttributes()));
        out.close();

        return CONFIG_FILE_NAME_JSON;
    }

    public AgentConfigData getData() {
        return data;
    }

    public void setData(AgentConfigData data) {
        this.data = data;
    }
}
