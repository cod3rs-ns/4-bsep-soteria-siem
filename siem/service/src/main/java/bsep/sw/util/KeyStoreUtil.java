package bsep.sw.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import static bsep.sw.util.CertificateGenerator.generateCertificate;


@Component
public class KeyStoreUtil {

    private final Logger logger = Logger.getLogger(KeyStoreUtil.class);

    private String keyStorePath;
    private String password;
    private String secretStorePath;

    private KeyStore keyStore;

    @Autowired
    public KeyStoreUtil(@Value("${keystore.path}") final String path,
                        @Value("${keystore.password}") final String password,
                        @Value("${secretStore.path}") final String secretStorePath) {
        try {
            this.keyStorePath = path;
            this.password = password;
            this.secretStorePath = secretStorePath;
            this.keyStore = KeyStore.getInstance("JKS", "SUN");
            loadKeyStore();
        } catch (final Exception e) {
            logger.error("Problem occurred while loading key store", e);
        }
    }

    private void loadKeyStore() throws Exception {
        final FileInputStream inputStream = new FileInputStream(this.keyStorePath);
        keyStore.load(inputStream, this.password.toCharArray());
        inputStream.close();
    }

    public PublicKey readPublicKey(final String username) {
        try {
            if (keyStore.isKeyEntry(username)) {
                return keyStore.getCertificate(username).getPublicKey();
            }
        } catch (final Exception e) {
            logger.error("Problem occurred while reading private key from store", e);
        }

        return null;
    }

    public PrivateKey readPrivateKey(final String username) {
        try {
            if (keyStore.isKeyEntry(username)) {
                return (PrivateKey) keyStore.getKey(username, this.password.toCharArray());
            }
        } catch (final Exception e) {
            logger.error("Problem occurred while reading private key from store", e);
        }

        return null;
    }

    private void saveKeyStore() throws Exception {
        final FileOutputStream out = new FileOutputStream(this.keyStorePath);
        keyStore.store(out, this.password.toCharArray());
        out.close();
    }

    public SecretKey loadSecretAsymmetricKey() throws Exception {
        final KeyStore store = KeyStore.getInstance("JCEKS");

        final FileInputStream in = new FileInputStream(this.secretStorePath);
        store.load(in, this.password.toCharArray());
        in.close();

        return (SecretKey) store.getKey("asymetric", this.password.toCharArray());
    }

    public KeyPair generateKeys() throws NoSuchAlgorithmException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public void addCertificate(final String username, final PrivateKey privateKey, final X509Certificate certificate) {
        try {
            keyStore.setKeyEntry(username, privateKey, password.toCharArray(), new Certificate[]{certificate});
            saveKeyStore();
        } catch (final Exception e) {
            logger.error("Problem occurred while adding new certificate in key store", e);
        }
    }

    public void generateAndSaveCertificate(final String username) {
        try {
            final KeyPair keypair = generateKeys();
            final X509Certificate certificate = generateCertificate(username, keypair, 30, "MD5WithRSA");
            addCertificate(username, keypair.getPrivate(), certificate);
        } catch (final Exception e) {
            logger.error("Problem occurred while generating new certificate", e);
        }
    }

    public AgentKeys findKeys(final String username) throws Exception {
        final String privateKey = Base64.encodeBase64String(readPrivateKey(username).getEncoded());
        final String publicKey = Base64.encodeBase64String(readPublicKey(username).getEncoded());
        final String secretKey = Base64.encodeBase64String(loadSecretAsymmetricKey().getEncoded());

        return new AgentKeys(privateKey, publicKey, secretKey);
    }
}
