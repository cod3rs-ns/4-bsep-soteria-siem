package bsep.sw.hateoas.user;


import bsep.sw.domain.User;


public class UserRequest {

    private UserRequestData data;

    public User toDomain() {
        final UserAttributes attributes = data.getAttributes();

        return new User()
                .username(attributes.getUsername())
                .email(attributes.getEmail())
                .password(attributes.getPassword())
                .firstName(attributes.getFirstName())
                .lastName(attributes.getLastName())
                .phoneNumber(attributes.getPhoneNumber())
                .role(attributes.getRole())
                .imagePath(attributes.getImagePath());
    }

    public UserRequestData getData() {
        return data;
    }

    public void setData(UserRequestData data) {
        this.data = data;
    }
}
