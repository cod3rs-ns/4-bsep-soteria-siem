package bsep.sw.util;

import sun.security.x509.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CertificateGenerator {

    public static X509Certificate generateCertificate(final String dn, final KeyPair pair, final int days, final String algorithm)
            throws GeneralSecurityException, IOException {
        final PrivateKey privateKey = pair.getPrivate();
        final X509CertInfo info = new X509CertInfo();

        final Date from = new Date();
        final Date to = new Date(from.getTime() + days * 86400000L);

        final CertificateValidity interval = new CertificateValidity(from, to);
        final BigInteger sn = new BigInteger(64, new SecureRandom());
        final X500Name owner = new X500Name(dn, "Soteria", "FTN", "Novi Sad", "Serbia", "RS");

        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        info.set(X509CertInfo.SUBJECT, owner);
        info.set(X509CertInfo.ISSUER, owner);
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId alg = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(alg));

        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privateKey, algorithm);

        // Update the algorithm, and resign.
        alg = (AlgorithmId) cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, alg);
        cert = new X509CertImpl(info);
        cert.sign(privateKey, algorithm);
        return cert;
    }
}
