package ro.payu.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ro.payu.authentication.AuthenticationService;

public final class ApiAuthenticationService {

    private static final String ALU_REQUEST_SIGNATURE_NAME = "ORDER_HASH";
    private static final String ALU_RESPONSE_SIGNATURE_NAME = "HASH";
    private static final String ALU_RESPONSE_URL_3DS_NAME = "URL_3DS";

    private final AuthenticationService authenticationService;
    private final String secretKey;

    public ApiAuthenticationService(String secretKey, AuthenticationService authenticationService) {
        this.secretKey = secretKey;

        this.authenticationService = authenticationService;
    }

    public List<NameValuePair> addAluRequestSignature(final List<NameValuePair> parameters) {

        final List<NameValuePair> parametersWithoutSignature = parameters.stream()
                .filter(nameValuePair -> !(nameValuePair.getName().equals(ALU_REQUEST_SIGNATURE_NAME)))
                .collect(Collectors.toList());


        final String signature = authenticationService.computeSignature(parametersWithoutSignature, secretKey);

        final List<NameValuePair> parametersWithSignature = new ArrayList<>(parametersWithoutSignature);
        parametersWithSignature.add(new BasicNameValuePair(ALU_REQUEST_SIGNATURE_NAME, signature));

        return parametersWithSignature;
    }

    public boolean verifyAluResponseSignature(final List<NameValuePair> parameters) {
        final List<NameValuePair> parametersWithoutSignature = new ArrayList<>();
        String signature = "";

        for (NameValuePair parameter : parameters) {
            if (parameter.getName().equals(ALU_RESPONSE_SIGNATURE_NAME)) {
                signature = parameter.getValue();
            } else {
                parametersWithoutSignature.add(parameter);
            }
        }

        if (signature.equals("")) {
            return false;
        }

        // Sadly, URL_3DS is not included in signature
        final List<NameValuePair> parametersWithoutSignatureAnd3DSUrlParameters = parametersWithoutSignature.stream()
                .filter(nameValuePair -> !(nameValuePair.getName().equals(ALU_RESPONSE_URL_3DS_NAME)))
                .collect(Collectors.toList());

        return authenticationService.computeSignature(
                parametersWithoutSignatureAnd3DSUrlParameters,
                getKeepSameParameterOrderComparator(),
                secretKey
        ).equals(signature.toLowerCase());
    }

    private Comparator<NameValuePair> getKeepSameParameterOrderComparator() {
        return (NameValuePair p1, NameValuePair p2) -> 0;
    }
}
