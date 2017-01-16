package ro.payu.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ro.payu.authentication.AuthenticationService;

public final class ApiAuthenticationService {

    private static final String ALU_REQUEST_SIGNATURE_NAME = "ORDER_HASH";
    private static final String ALU_RESPONSE_SIGNATURE_NAME = "HASH";
    private static final String ALU_RESPONSE_URL_3DS_NAME = "URL_3DS";

    private static final Map<String, Integer> ALU_RESPONSE_PARAMETER_ORDER;
    static {
        Map<String, Integer> aluResponseParameterOrder = new HashMap<>();

        // ALUv1+ main result variables
        aluResponseParameterOrder.put("REFNO", 100);
        aluResponseParameterOrder.put("ALIAS", 101);
        aluResponseParameterOrder.put("STATUS", 102);
        aluResponseParameterOrder.put("RETURN_CODE", 103);
        aluResponseParameterOrder.put("RETURN_MESSAGE", 104);
        aluResponseParameterOrder.put("DATE", 105);

        // ALUv1+ TR specific
        aluResponseParameterOrder.put("AMOUNT", 200);
        aluResponseParameterOrder.put("CURRENCY", 201);
        aluResponseParameterOrder.put("INSTALLMENTS_NO", 202);
        aluResponseParameterOrder.put("CARD_PROGRAM_NAME", 203);

        // ALUv2+ Card extra
        aluResponseParameterOrder.put("ORDER_REF", 300);
        aluResponseParameterOrder.put("AUTH_CODE", 301);
        aluResponseParameterOrder.put("RRN", 302);

        // ALUv2+ Card plugins
        aluResponseParameterOrder.put("PROCRETURNCODE", 400);
        aluResponseParameterOrder.put("ERRORMESSAGE", 401);
        aluResponseParameterOrder.put("BANK_MERCHANT_ID", 402);
        aluResponseParameterOrder.put("PAN", 403);
        aluResponseParameterOrder.put("EXPYEAR", 404);
        aluResponseParameterOrder.put("EXPMONTH", 405);
        aluResponseParameterOrder.put("CLIENTID", 406);
        aluResponseParameterOrder.put("HOSTREFNUM", 407);
        aluResponseParameterOrder.put("OID", 408);
        aluResponseParameterOrder.put("RESPONSE", 409);
        aluResponseParameterOrder.put("TERMINAL_BANK", 410);
        aluResponseParameterOrder.put("MDSTATUS", 411);
        aluResponseParameterOrder.put("MDERRORMSG", 412);
        aluResponseParameterOrder.put("TXSTATUS", 413);
        aluResponseParameterOrder.put("XID", 414);
        aluResponseParameterOrder.put("ECI", 415);
        aluResponseParameterOrder.put("CAVV", 416);
        aluResponseParameterOrder.put("TRANSID", 417);

        // ALUv3+ Card token created value
        aluResponseParameterOrder.put("TOKEN_HASH", 500);

        // ALUv3+ Wire account data
        aluResponseParameterOrder.put("WIRE_ACCOUNTS", 600);

        // ALUv3+ Reference for offline payments
        aluResponseParameterOrder.put("TX_REFNO", 700);

        // ALUv3+ PayByLink url
        aluResponseParameterOrder.put("URL_REDIRECT", 800);

        ALU_RESPONSE_PARAMETER_ORDER = Collections.unmodifiableMap(aluResponseParameterOrder);
    }

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
                getParameterSpecificOrderComparator(),
                secretKey
        ).equals(signature.toLowerCase());
    }

    /**
     * This builds a specific comparator to sort the ALU response parameters in a specific order
     */
    private Comparator<NameValuePair> getParameterSpecificOrderComparator() {
        return (NameValuePair p1, NameValuePair p2) -> {
            final String k1 = p1.getName();
            final String k2 = p2.getName();

            if (!ALU_RESPONSE_PARAMETER_ORDER.containsKey(k1) || !ALU_RESPONSE_PARAMETER_ORDER.containsKey(k2)) {
                return 0;
            }
            if (k1.equals(k2)) {
                return p1.getValue().compareTo(p2.getValue());
            }
            return ALU_RESPONSE_PARAMETER_ORDER.get(k1).compareTo(ALU_RESPONSE_PARAMETER_ORDER.get(k2));
        };
    }
}
