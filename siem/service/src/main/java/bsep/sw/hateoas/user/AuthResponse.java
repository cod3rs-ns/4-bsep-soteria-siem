package bsep.sw.hateoas.user;


public class AuthResponse {

    private AuthResponseData data;

    public AuthResponse(AuthResponseData data) {
        this.data = data;
    }

    public static AuthResponse fromDomain(final String token, final Long expiresIn) {
        return new AuthResponse(AuthResponseData.fromDomain(token, expiresIn));
    }

    public AuthResponseData getData() {
        return data;
    }

    public void setData(AuthResponseData data) {
        this.data = data;
    }
}