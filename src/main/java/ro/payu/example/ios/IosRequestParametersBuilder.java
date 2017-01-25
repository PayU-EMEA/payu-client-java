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

    public List<NameValuePair> build(List<NameValuePair> ipnRequestParameters) {
        String iosDate = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String orderRefExternal = "";
        for (NameValuePair pair : ipnRequestParameters) {
            switch (pair.getName()) {
                case "ORDER_REF":
                    orderRefExternal = pair.getValue();
                    break;
            }
        }

        final List<NameValuePair> iosRequestParameters = new ArrayList<>();
        iosRequestParameters.add(new BasicNameValuePair("MERCHANT", merchantCode));
        iosRequestParameters.add(new BasicNameValuePair("REFNOEXT", orderRefExternal));
        return iosRequestParameters;
    }
}
