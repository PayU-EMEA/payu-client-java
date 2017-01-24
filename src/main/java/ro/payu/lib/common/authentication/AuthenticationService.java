package ro.payu.lib.common.authentication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationService {

    private static final String DEFAULT_SECRET_KEY = "SECRET_KEY";

    final private SignatureCalculator signatureCalculator;
    final private String secretKey;

    public AuthenticationService(SignatureCalculator signatureCalculator) {
        this(signatureCalculator, DEFAULT_SECRET_KEY);
    }

    public AuthenticationService(SignatureCalculator signatureCalculator, String secretKey) {
        this.signatureCalculator = signatureCalculator;
        this.secretKey = secretKey;
    }

    public List<NameValuePair> addSignature(
            final List<NameValuePair> parameters,
            final List<String> fieldsExcludedFromSignature,
            final Comparator<NameValuePair> parametersSortingComparator,
            final String signatureParameterName
    ) {

        final List<NameValuePair> parametersToBeSigned = parameters.stream()
                .filter(nameValuePair -> !fieldsExcludedFromSignature.contains(nameValuePair.getName()))
                .collect(Collectors.toList());

        final String signature = signatureCalculator.computeSignature(
                parametersToBeSigned,
                parametersSortingComparator,
                secretKey
        );

        final List<NameValuePair> parametersWithSignature = new ArrayList<>(parameters);
        parametersWithSignature.add(new BasicNameValuePair(signatureParameterName, signature));

        return parametersWithSignature;
    }

    public String computeSignature(final List<NameValuePair> parametersToBeSigned, final Comparator<NameValuePair> parametersSortingComparator) {
        return signatureCalculator.computeSignature(
                parametersToBeSigned,
                parametersSortingComparator,
                secretKey
        );
    }

    public void verifySignature(
            final List<NameValuePair> parameters,
            final List<String> fieldsExcludedFromSignature,
            final Comparator<NameValuePair> parametersSortingComparator,
            final String signatureParameterName
    ) throws InvalidSignatureException {

        String signature = "";
        for (NameValuePair parameter : parameters) {
            if (parameter.getName().equals(signatureParameterName)) {
                signature = parameter.getValue();
            }
        }

        if (signature.equals("")) {
            throw new InvalidSignatureException("Missing signature parameter " + signatureParameterName);
        }

        final List<NameValuePair> parametersToBeSigned = parameters.stream()
                .filter(nameValuePair -> !fieldsExcludedFromSignature.contains(nameValuePair.getName()))
                .collect(Collectors.toList());

        final String expectedSignature = signatureCalculator.computeSignature(
                parametersToBeSigned,
                parametersSortingComparator,
                secretKey
        );

        if (!expectedSignature.equals(signature.toLowerCase())) {
            throw new InvalidSignatureException("Signature mismatch. Expected: " + expectedSignature + "; Actual: " + signature);
        }
    }
}
