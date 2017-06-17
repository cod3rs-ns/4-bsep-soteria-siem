package bsep.sw.hateoas.user;


import bsep.sw.domain.User;

public class UserResponse {

    private UserResponseData data;

    public UserResponse(UserResponseData data) {
        this.data = data;
    }

    public static UserResponse fromDomain(final User user) {
        return new UserResponse(UserResponseData.fromDomain(user));
    }

    public UserResponseData getData() {
        return data;
    }

    public void setData(UserResponseData data) {
        this.data = data;
    }
}
