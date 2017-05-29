package bsep.sw.util;


public class AuthResponse {
    private String token;

    public AuthResponse(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}