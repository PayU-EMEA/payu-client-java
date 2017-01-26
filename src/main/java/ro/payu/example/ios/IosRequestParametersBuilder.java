package ro.payu.example.ios;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IosRequestParametersBuilder {

    private final String merchantCode;

    public IosRequestParametersBuilder(String merchantCode) {

        this.merchantCode = merchantCode;
    }

    public List<NameValuePair> build(String orderReference) {
        final List<NameValuePair> iosRequestParameters = new ArrayList<>();
        iosRequestParameters.add(new BasicNameValuePair("MERCHANT", merchantCode));
        iosRequestParameters.add(new BasicNameValuePair("REFNOEXT", orderReference));
        return iosRequestParameters;
    }
}
