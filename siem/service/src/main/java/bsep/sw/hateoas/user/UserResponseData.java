package bsep.sw.hateoas.user;


import bsep.sw.domain.User;

import static bsep.sw.hateoas.ResourceTypes.USERS_TYPE;

public class UserResponseData {

    private String type;
    private String id;
    private UserAttributes attributes;
    private UserResponseRelationships relationships;

    public UserResponseData(String type, String id, UserAttributes attributes, UserResponseRelationships relationships) {
        this.type = type;
        this.id = id;
        this.attributes = attributes;
        this.relationships = relationships;
    }

    public static UserResponseData fromDomain(final User user) {
        return new UserResponseData(USERS_TYPE, user.getId().toString(), createAttributes(user), createRelationships(user));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(UserAttributes attributes) {
        this.attributes = attributes;
    }

    public UserResponseRelationships getRelationships() {
        return relationships;
    }

    public void setRelationships(UserResponseRelationships relationships) {
        this.relationships = relationships;
    }

    private static UserAttributes createAttributes(final User user) {
        return new UserAttributes()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(user.getRole())
                .imagePath(user.getImagePath());
    }

    private static UserResponseRelationships createRelationships(final User user) {
        return UserResponseRelationships.fromDomain(user);
    }

}
