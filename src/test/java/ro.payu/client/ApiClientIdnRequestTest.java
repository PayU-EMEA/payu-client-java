package ro.payu.client;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;
import ro.payu.authentication.AuthenticationService;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ApiClientIdnRequestTest {

    private static final String SECRET_KEY = "SECRET_KEY";
    private static final String MERCHANT_CODE = "ITEST";
    private static final String ORDER_REF = "537";
    private static final double ORDER_AMOUNT = 1.99;
    private static final String ORDER_CURRENCY = "TRY";
    private static final String REQUEST_IDN_DATE = "2017-01-19 19:00:01";
    private static final double CHARGE_AMOUNT = 1.99;
    private static final String REF_URL = "http://some.url";
    private static final String IDN_PRN = "IDN_PRN";
    private static final String REQUEST_SIGNATURE = "8bec22c328b24c8bf94eea0a9d175bb4";

    private static final String RESPONSE_CODE = "1";
    private static final String RESPONSE_MSG = "Confirmed";
    private static final String RESPONSE_IDN_DATE = "2017-01-19 22:28:59";
    private static final String RESPONSE_SIGNATURE = "b427c8b83dba1a885e058e428679895d";

    private ApiClient apiClient;

    private ApiHttpClient httpClientMock;

    @Before
    public void setUp() {

        httpClientMock = mock(ApiHttpClient.class);

        apiClient = new ApiClient(
                new ApiAuthenticationService(
                        SECRET_KEY,
                        new AuthenticationService()
                ),
                httpClientMock,
                new AluResponseXmlParser()
        );
    }

    @Test
    public void testCallIdnSuccessWithRealHttpCall() {

        // setup
        apiClient = new ApiClient(
                new ApiAuthenticationService(
                        SECRET_KEY,
                        new AuthenticationService()
                ),
                new ApiHttpClient("ro.payu.local", 80, "http"),
                new AluResponseXmlParser()
        );

        final List<NameValuePair> requestParameters = getRequestParametersList();
        final List<NameValuePair> actualResponseParameters;

        // exercise
        try {
            actualResponseParameters = apiClient.callIDN(requestParameters);

        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        // verify
        final List<NameValuePair> actualResponseParametersWithoutSignature = actualResponseParameters.stream()
                .filter(nameValuePair -> !nameValuePair.getName().equals("ORDER_HASH")
                        && !nameValuePair.getName().equals("IDN_DATE")
                )
                .collect(Collectors.toList());

        final List<NameValuePair> expectedResponseParameters = getResponseParametersList();

        assertEquals(expectedResponseParameters, actualResponseParametersWithoutSignature);
    }

    @Test
    public void testCallIdnSuccess() throws HttpException, BadResponseSignatureException, InvalidXmlResponseException, CommunicationException, UnsupportedEncodingException {

        // setup
        HttpPost expectedHttpRequest = getSuccessRequest();
        HttpResponse mockedHttpResponse = getSuccessResponse();
        when(httpClientMock.callHttp(any()))
                .thenReturn(mockedHttpResponse);

        List<NameValuePair> requestParameters = getRequestParametersList();
        List<NameValuePair> actualResponseParameters;

        // exercise
        actualResponseParameters = apiClient.callIDN(requestParameters);

        // verify
        verify(httpClientMock).callHttp(expectedHttpRequest);

        List<NameValuePair> expectedResponseParameters = getResponseParametersListWithSignature();
        assertEquals(expectedResponseParameters, actualResponseParameters);
    }

    private List<NameValuePair> getRequestParametersList() {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("MERCHANT", MERCHANT_CODE));
        list.add(new BasicNameValuePair("ORDER_REF", ORDER_REF));
        list.add(new BasicNameValuePair("ORDER_AMOUNT", String.valueOf(ORDER_AMOUNT)));
        list.add(new BasicNameValuePair("ORDER_CURRENCY", ORDER_CURRENCY));
        list.add(new BasicNameValuePair("IDN_DATE", REQUEST_IDN_DATE));
        list.add(new BasicNameValuePair("CHARGE_AMOUNT", String.valueOf(CHARGE_AMOUNT)));
//        list.add(new BasicNameValuePair("REF_URL", REF_URL));
        list.add(new BasicNameValuePair("IDN_PRN", IDN_PRN));

        return list;
    }

    private List<NameValuePair> getRequestParametersListWithSignature() {
        List<NameValuePair> list = getRequestParametersList();
        list.add(new BasicNameValuePair("ORDER_HASH", REQUEST_SIGNATURE));
        return list;
    }

    private List<NameValuePair> getResponseParametersList() {
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("ORDER_REF", ORDER_REF));
        list.add(new BasicNameValuePair("RESPONSE_CODE", String.valueOf(RESPONSE_CODE)));
        list.add(new BasicNameValuePair("RESPONSE_MSG", RESPONSE_MSG));

        return list;
    }

    private List<NameValuePair> getResponseParametersListWithSignature() {
        List<NameValuePair> list = getResponseParametersList();
        list.add(new BasicNameValuePair("IDN_DATE", RESPONSE_IDN_DATE));
        list.add(new BasicNameValuePair("ORDER_HASH", RESPONSE_SIGNATURE));
        return list;
    }

    private HttpResponse getSuccessResponse() {

        String xmlResponse = "<EPAYMENT>340|1|Confirmed|2017-01-19 22:28:59|b427c8b83dba1a885e058e428679895d</EPAYMENT>";

        HttpResponse response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
        BasicHttpEntity httpEntity = new BasicHttpEntity();

        byte[] bytesArray = xmlResponse.getBytes();
        httpEntity.setContent(new ByteArrayInputStream(bytesArray));
        response.setEntity(httpEntity);

        return response;
    }

    private HttpPost getSuccessRequest() throws UnsupportedEncodingException {

        List<NameValuePair> paramsList = getRequestParametersListWithSignature();

        HttpPost request = new HttpPost(ApiClient.IDN_ENDPOINT);
        request.setEntity(new UrlEncodedFormEntity(paramsList));

        return request;
    }
}
