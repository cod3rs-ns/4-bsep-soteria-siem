package bsep.sw.hateoas.user;


import java.util.Date;

import static bsep.sw.hateoas.ResourceTypes.TOKENS_TYPE;

public class AuthResponseData {

    private String id;
    private String type;
    private AuthResponseAttributes attributes;

    public AuthResponseData(String id, String type, AuthResponseAttributes attributes) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
    }

    public static AuthResponseData fromDomain(String token, Long expiresIn) {
        return new AuthResponseData((new Date()).toString(), TOKENS_TYPE, AuthResponseAttributes.fromDomain(token, expiresIn));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuthResponseAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(AuthResponseAttributes attributes) {
        this.attributes = attributes;
    }
}
