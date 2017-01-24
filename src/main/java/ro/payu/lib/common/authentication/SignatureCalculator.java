package ro.payu.lib.common.authentication;

import org.apache.http.NameValuePair;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;

public final class SignatureCalculator {

    public final String computeSignature(List<NameValuePair> parameters,
                                         Comparator<NameValuePair> comparator,
                                         String secretKey) {

        final String stringToHash = parameters.stream()
                .sorted(comparator)
                .map(nameValuePair -> String.valueOf(nameValuePair.getValue().getBytes().length) + nameValuePair.getValue())
                .collect(Collectors.joining(""));

        return hMacMD5(stringToHash, secretKey);
    }

    private String hMacMD5(String stringToHash, String secretKey) {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacMD5");
        final Mac hMacMD5;

        try {
            hMacMD5 = Mac.getInstance("HmacMD5");
            hMacMD5.init(secretKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        byte[] signatureBytes = hMacMD5.doFinal(stringToHash.getBytes(StandardCharsets.UTF_8));

        Formatter formatter = new Formatter();
        for (byte signatureByte : signatureBytes) {
            formatter.format("%02x", signatureByte);
        }

        return formatter.toString();
    }

    public static Comparator<NameValuePair> getParameterNameSortedComparator() {
        return Comparator.comparing(NameValuePair::getName);
    }

    public static Comparator<NameValuePair> getKeepSameParameterOrderComparator() {
        return (NameValuePair p1, NameValuePair p2) -> 0;
    }
}
