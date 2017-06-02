package bsep.sw.hateoas.user;


public class UserRequestData {

    private String type;
    private UserAttributes attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(UserAttributes attributes) {
        this.attributes = attributes;
    }
}
