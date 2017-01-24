package ro.payu.example.irn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IrnRequestParametersBuilder {

    private final String merchantCode;

    public IrnRequestParametersBuilder(String merchantCode) {

        this.merchantCode = merchantCode;
    }

    public List<NameValuePair> build(List<NameValuePair> ipnRequestParameters) {
        String irnDate = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String orderRef = "";
        String amount = "";
        String currency = "";
        for (NameValuePair pair : ipnRequestParameters) {
            switch (pair.getName()) {
                case "REFNO":
                    orderRef = pair.getValue();
                    break;
                case "IPN_TOTALGENERAL":
                    amount = pair.getValue();
                    break;
                case "CURRENCY":
                    currency = pair.getValue();
                    break;
            }
        }

        final List<NameValuePair> irnRequestParameters = new ArrayList<>();
        irnRequestParameters.add(new BasicNameValuePair("MERCHANT", merchantCode));
        irnRequestParameters.add(new BasicNameValuePair("ORDER_REF", orderRef));
        irnRequestParameters.add(new BasicNameValuePair("ORDER_AMOUNT", amount));
        irnRequestParameters.add(new BasicNameValuePair("ORDER_CURRENCY", currency));
        irnRequestParameters.add(new BasicNameValuePair("IRN_DATE", irnDate));
        return irnRequestParameters;
    }
}
