package ro.payu.lib.common.authentication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ApiCommonAuthenticationService {

    private static final String DEFAULT_SECRET_KEY = "SECRET_KEY";

    final private AuthenticationService authenticationService;
    final private String secretKey;

    public ApiCommonAuthenticationService(AuthenticationService authenticationService) {
        this(authenticationService, DEFAULT_SECRET_KEY);
    }

    public ApiCommonAuthenticationService(AuthenticationService authenticationService, String secretKey) {
        this.authenticationService = authenticationService;
        this.secretKey = secretKey;
    }

    public List<NameValuePair> addRequestSignature(
            final List<NameValuePair> requestParameters,
            final List<String> fieldsExcludedFromSignature,
            Comparator<NameValuePair> parametersSortingComparator,
            final String signatureParameterName
    ) {

        final List<NameValuePair> parametersToBeSigned = requestParameters.stream()
                .filter(nameValuePair -> !fieldsExcludedFromSignature.contains(nameValuePair.getName()))
                .collect(Collectors.toList());

        final String signature = authenticationService.computeSignature(
                parametersToBeSigned,
                parametersSortingComparator,
                secretKey
        );

        final List<NameValuePair> parametersWithSignature = new ArrayList<>(requestParameters);
        parametersWithSignature.add(new BasicNameValuePair(signatureParameterName, signature));

        return parametersWithSignature;
    }

    public void verifyResponseSignature(
            final List<NameValuePair> responseParameters,
            final List<String> fieldsExcludedFromSignature,
            Comparator<NameValuePair> parametersSortingComparator,
            final String signatureParameterName
    ) throws BadResponseSignatureException {

        String signature = "";
        for (NameValuePair parameter : responseParameters) {
            if (parameter.getName().equals(signatureParameterName)) {
                signature = parameter.getValue();
            }
        }

        if (signature.equals("")) {
            throw new BadResponseSignatureException("Missing response signature parameter");
        }

        final List<NameValuePair> parametersToBeSigned = responseParameters.stream()
                .filter(nameValuePair -> !fieldsExcludedFromSignature.contains(nameValuePair.getName()))
                .collect(Collectors.toList());

        final String expectedSignature = authenticationService.computeSignature(
                parametersToBeSigned,
                parametersSortingComparator,
                secretKey
        );

        if (!expectedSignature.equals(signature.toLowerCase())) {
            throw new BadResponseSignatureException("Wrong response signature");
        }
    }

    public Comparator<NameValuePair> getParameterNameSortedComparator() {
        return Comparator.comparing(NameValuePair::getName);
    }

    public Comparator<NameValuePair> getKeepSameParameterOrderComparator() {
        return (NameValuePair p1, NameValuePair p2) -> 0;
    }
}
