package bsep.sw.hateoas.agent_config;

public class AgentConfigRequest {

    private AgentConfigData data;

    public void toYmlFile() {
        // TODO Implement this
        System.out.println(data.getAttributes().getDefaultLevel());
        System.out.println(data.getAttributes().getOs());
        System.out.println(data.getAttributes().getPaths().size());
        System.out.println(data.getAttributes().getRegexes().size());
        System.out.println("Need to create YML file. :)");
    }

    public AgentConfigData getData() {
        return data;
    }

    public void setData(AgentConfigData data) {
        this.data = data;
    }
}
