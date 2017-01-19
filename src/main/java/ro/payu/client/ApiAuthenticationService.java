package ro.payu.client;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ro.payu.authentication.AuthenticationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class ApiAuthenticationService {

    private static final String ALU_REQUEST_SIGNATURE_NAME = "ORDER_HASH";
    private static final String ALU_RESPONSE_SIGNATURE_NAME = "HASH";
    private static final String ALU_RESPONSE_URL_3DS_NAME = "URL_3DS";

    private static final String IDN_REQUEST_SIGNATURE_NAME = "ORDER_HASH";
    private static final List IDN_PARAMETERS_EXCLUDED_FROM_SIGNATURE = Arrays.asList("REF_URL", "IDN_PRN", IDN_REQUEST_SIGNATURE_NAME);

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


        final String signature = authenticationService.computeSignature(
                parametersWithoutSignature,
                getParameterNameSortedComparator(),
                secretKey
        );

        final List<NameValuePair> parametersWithSignature = new ArrayList<>(parametersWithoutSignature);
        parametersWithSignature.add(new BasicNameValuePair(ALU_REQUEST_SIGNATURE_NAME, signature));

        return parametersWithSignature;
    }

    public void verifyAluResponseSignature(final List<NameValuePair> parameters) throws BadResponseSignatureException {
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
            throw new BadResponseSignatureException("Missing response signature parameter");
        }

        // Sadly, URL_3DS is not included in signature
        final List<NameValuePair> parametersWithoutSignatureAnd3DSUrlParameters = parametersWithoutSignature.stream()
                .filter(nameValuePair -> !(nameValuePair.getName().equals(ALU_RESPONSE_URL_3DS_NAME)))
                .collect(Collectors.toList());

        String expectedSignature = authenticationService.computeSignature(
                parametersWithoutSignatureAnd3DSUrlParameters,
                getKeepSameParameterOrderComparator(),
                secretKey
        );
        if (!expectedSignature.equals(signature.toLowerCase())) {
            throw new BadResponseSignatureException("Wrong response signature");
        }
    }

    public List<NameValuePair> addIdnRequestSignature(final List<NameValuePair> parameters) {

        final String signature = computeIdnSignature(
                getIdnParametersToBeSigned(parameters)
        );

        final List<NameValuePair> parametersWithSignature = new ArrayList<>(parameters);
        parametersWithSignature.add(new BasicNameValuePair(IDN_REQUEST_SIGNATURE_NAME, signature));

        return parametersWithSignature;
    }

    public void verifyIdnResponseSignature(final List<NameValuePair> parameters) throws BadResponseSignatureException {

        String signature = "";
        for (NameValuePair parameter : parameters) {
            if (parameter.getName().equals(IDN_REQUEST_SIGNATURE_NAME)) {
                signature = parameter.getValue();
            }
        }

        if (signature.equals("")) {
            throw new BadResponseSignatureException("Missing response signature parameter");
        }

        String expectedSignature = computeIdnSignature(
                getIdnParametersToBeSigned(parameters)
        );

        if (!expectedSignature.equals(signature.toLowerCase())) {
            throw new BadResponseSignatureException("Wrong response signature");
        }
    }

    private List<NameValuePair> getIdnParametersToBeSigned(List<NameValuePair> parameters) {
        return parameters.stream()
                .filter(nameValuePair -> !IDN_PARAMETERS_EXCLUDED_FROM_SIGNATURE.contains(nameValuePair.getName()))
                .collect(Collectors.toList());
    }

    private String computeIdnSignature(List<NameValuePair> parametersToBeSigned) {
        return authenticationService.computeSignature(
                    parametersToBeSigned,
                    getKeepSameParameterOrderComparator(),
                    secretKey
            );
    }

    private Comparator<NameValuePair> getParameterNameSortedComparator() {
        return Comparator.comparing(NameValuePair::getName);
    }

    private Comparator<NameValuePair> getKeepSameParameterOrderComparator() {
        return (NameValuePair p1, NameValuePair p2) -> 0;
    }
}
