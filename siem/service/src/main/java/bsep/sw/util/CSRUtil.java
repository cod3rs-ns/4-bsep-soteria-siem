package bsep.sw.util;


import bsep.sw.hateoas.log.LogRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;
import java.security.Signature;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CSRUtil {

    private final Logger logger = Logger.getLogger(CSRUtil.class);

    private static final String START_CSR = "-----BEGIN CERTIFICATE REQUEST-----";
    private static final String END_CSR = "-----END CERTIFICATE REQUEST-----";
    private static final String START_SECRET = "-----BEGIN SECRET KEY-----";
    private static final String END_SECRET = "-----END SECRET KEY-----";

    private final KeyStoreUtil keyStoreUtil;

    @Autowired
    public CSRUtil(final KeyStoreUtil keyStoreUtil) {
        this.keyStoreUtil = keyStoreUtil;
    }

    private byte[] decrypt(final byte[] inputData, final byte[] key) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES");
        final SecretKey secKey = new SecretKeySpec(key, "AES");

        cipher.init(Cipher.DECRYPT_MODE, secKey);
        return cipher.doFinal(inputData);
    }

    private boolean verify(final byte[] data, final byte[] signature, final PublicKey publicKey) {
        try {
            final Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(signature);
        } catch (final Exception e) {
            logger.error("Problem occurred while verifying request", e);
        }
        return false;
    }

    private byte[] decryptSymmetricKey(final byte[] key, final String agentId) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyStoreUtil.readPrivateKey(agentId));
            return cipher.doFinal(key);
        } catch (final Exception e) {
            logger.error("Problem occurred while verifying request", e);
        }
        return new byte[]{};
    }

    public LogRequest parseRequest(final String request, final String agentId) throws Exception {
        final Pattern pattern = Pattern.compile(START_CSR + "(.*)" + END_CSR + "(.*)" + START_SECRET + "(.*)" + END_SECRET + "(.*)", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(request);
        if (matcher.matches()) {
            final byte[] cipherData = Base64.decodeBase64(matcher.group(1).trim());
            final byte[] secretKey = decryptSymmetricKey(Base64.decodeBase64(matcher.group(3).trim()), agentId);
            final byte[] signature = Base64.decodeBase64(matcher.group(4).trim());

            if (verify(cipherData, signature, keyStoreUtil.readPublicKey(agentId))) {
                try {
                    final String decrypted = new String(decrypt(cipherData, secretKey));
                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    mapper.registerModule(new JodaModule());
                    return mapper.readValue(decrypted, LogRequest.class);
                } catch (final Exception e) {
                    logger.debug("Error occurred while validating request", e);
                }
            }
        }
        return null;
    }
}
