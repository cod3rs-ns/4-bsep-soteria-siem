package bsep.sw.util;


public class AgentKeys {

    private String privateKey;
    private String publicKey;
    private String secretKey;

    public AgentKeys(final String privateKey, final String publicKey, final String secretKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
