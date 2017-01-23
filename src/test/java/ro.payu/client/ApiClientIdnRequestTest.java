package ro.payu.client;

//public class ApiClientIdnRequestTest {
//
//    private static final String SECRET_KEY = "SECRET_KEY";
//    private static final String MERCHANT_CODE = "ITEST";
//    private static final String ORDER_REF = "537";
//    private static final double ORDER_AMOUNT = 1.99;
//    private static final String ORDER_CURRENCY = "TRY";
//    private static final String REQUEST_IDN_DATE = "2017-01-19 19:00:01";
//    private static final double CHARGE_AMOUNT = 1.99;
//    private static final String REF_URL = "http://some.url";
//    private static final String IDN_PRN = "IDN_PRN";
//
//    private static final String EXPECTED_REQUEST_SIGNATURE = "58c80797edc20d2ff2717c17c2677a70";
//
//    private static final String RESPONSE_CODE = "1";
//    private static final String RESPONSE_MSG = "Confirmed";
//    private static final String RESPONSE_IDN_DATE = "2017-01-19 22:28:59";
//    private static final String RESPONSE_SIGNATURE = "68b0856fe891167e35fd66305be0e018";
//
//    private ApiClient apiClient;
//
//    private ApiHttpClient httpClientMock;
//
//    @Before
//    public void setUp() {
//
//        httpClientMock = mock(ApiHttpClient.class);
//
//        apiClient = new ApiClient(
//                aluClient, new ApiAuthenticationService(
//                        SECRET_KEY,
//                        new AuthenticationService()
//                ),
//                httpClientMock,
//                new AluResponseXmlParser()
//        );
//    }
//
//    @Test
//    public void testCallIdnSuccessWithRealHttpCall() {
//
//        // setup
//        apiClient = new ApiClient(
//                aluClient, new ApiAuthenticationService(
//                        SECRET_KEY,
//                        new AuthenticationService()
//                ),
//                new ApiHttpClient("ro.payu.local", 80, "http"),
//                new AluResponseXmlParser()
//        );
//
//        final List<NameValuePair> requestParameters = getRequestParametersList();
//        final List<NameValuePair> actualResponseParameters;
//
//        // exercise
//        try {
//            actualResponseParameters = apiClient.callIDN(requestParameters);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//            return;
//        }
//
//        // verify
//        final List<NameValuePair> actualResponseParametersWithoutSignature = actualResponseParameters.stream()
//                .filter(nameValuePair -> !nameValuePair.getName().equals("ORDER_HASH")
//                        && !nameValuePair.getName().equals("IDN_DATE")
//                )
//                .collect(Collectors.toList());
//
//        final List<NameValuePair> expectedResponseParameters = getResponseParametersList();
//
//        assertEquals(expectedResponseParameters, actualResponseParametersWithoutSignature);
//    }
//
//    @Test
//    public void testCallIdnSuccess() throws HttpException, BadResponseSignatureException, InvalidXmlResponseParsingException, CommunicationException, IOException {
//
//        // setup
//        List<NameValuePair> expectedHttpRequestParameters = getRequestParametersListWithSignature();
//        HttpResponse mockedHttpResponse = getSuccessResponse();
//        when(httpClientMock.callHttp(any()))
//                .thenReturn(mockedHttpResponse);
//
//        List<NameValuePair> requestParameters = getRequestParametersList();
//        List<NameValuePair> actualResponseParameters;
//
//        // exercise
//        actualResponseParameters = apiClient.callIDN(requestParameters);
//
//        // verify
//        List<NameValuePair> expectedResponseParameters = getResponseParametersListWithSignature();
//        assertEquals(expectedResponseParameters, actualResponseParameters);
//
//        ArgumentCaptor<HttpPost> argument = ArgumentCaptor.forClass(HttpPost.class);
//        verify(httpClientMock).callHttp(argument.capture());
//        assertEquals("POST", argument.getValue().getMethod());
//        assertEquals(ApiClient.IDN_ENDPOINT, argument.getValue().getURI().getPath());
//        assertEquals("application/x-www-form-urlencoded", argument.getValue().getEntity().getContentType().getValue());
//        assertEquals(expectedHttpRequestParameters, URLEncodedUtils.parse(argument.getValue().getEntity()));
//    }
//
//    private List<NameValuePair> getRequestParametersList() {
//        List<NameValuePair> list = new ArrayList<>();
//        list.add(new BasicNameValuePair("MERCHANT", MERCHANT_CODE));
//        list.add(new BasicNameValuePair("ORDER_REF", ORDER_REF));
//        list.add(new BasicNameValuePair("ORDER_AMOUNT", String.valueOf(ORDER_AMOUNT)));
//        list.add(new BasicNameValuePair("ORDER_CURRENCY", ORDER_CURRENCY));
//        list.add(new BasicNameValuePair("IDN_DATE", REQUEST_IDN_DATE));
//        list.add(new BasicNameValuePair("CHARGE_AMOUNT", String.valueOf(CHARGE_AMOUNT)));
////        list.add(new BasicNameValuePair("REF_URL", REF_URL));
//        list.add(new BasicNameValuePair("IDN_PRN", IDN_PRN));
//
//        return list;
//    }
//
//    private List<NameValuePair> getRequestParametersListWithSignature() {
//        List<NameValuePair> list = getRequestParametersList();
//        list.add(new BasicNameValuePair("ORDER_HASH", EXPECTED_REQUEST_SIGNATURE));
//        return list;
//    }
//
//    private List<NameValuePair> getResponseParametersList() {
//        List<NameValuePair> list = new ArrayList<>();
//        list.add(new BasicNameValuePair("ORDER_REF", ORDER_REF));
//        list.add(new BasicNameValuePair("RESPONSE_CODE", String.valueOf(RESPONSE_CODE)));
//        list.add(new BasicNameValuePair("RESPONSE_MSG", RESPONSE_MSG));
//
//        return list;
//    }
//
//    private List<NameValuePair> getResponseParametersListWithSignature() {
//        List<NameValuePair> list = getResponseParametersList();
//        list.add(new BasicNameValuePair("IDN_DATE", RESPONSE_IDN_DATE));
//        list.add(new BasicNameValuePair("ORDER_HASH", RESPONSE_SIGNATURE));
//        return list;
//    }
//
//    private HttpResponse getSuccessResponse() {
//
//        String xmlResponse = "<EPAYMENT>537|1|Confirmed|2017-01-19 22:28:59|68b0856fe891167e35fd66305be0e018</EPAYMENT>";
//
//        HttpResponse response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
//        BasicHttpEntity httpEntity = new BasicHttpEntity();
//
//        byte[] bytesArray = xmlResponse.getBytes();
//        httpEntity.setContent(new ByteArrayInputStream(bytesArray));
//        response.setEntity(httpEntity);
//
//        return response;
//    }
//}
