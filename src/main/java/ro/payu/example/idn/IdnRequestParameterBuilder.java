package ro.payu.example.idn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IdnRequestParameterBuilder {


    private final String merchantCode;

    public IdnRequestParameterBuilder(String merchantCode) {

        this.merchantCode = merchantCode;
    }

    public List<NameValuePair> build(List<NameValuePair> aluResponseParameters) {
        String idnDate = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String orderRef = "";
        String amount = "";
        String currency = "";
        for (NameValuePair pair : aluResponseParameters) {
            switch (pair.getName()) {
                case "REFNO":
                    orderRef = pair.getValue();
                    break;
                case "AMOUNT":
                    amount = pair.getValue();
                    break;
                case "CURRENCY":
                    currency = pair.getValue();
                    break;
            }
        }

        final List<NameValuePair> idnRequestParameters = new ArrayList<>();
        idnRequestParameters.add(new BasicNameValuePair("MERCHANT", merchantCode));
        idnRequestParameters.add(new BasicNameValuePair("ORDER_REF", orderRef));
        idnRequestParameters.add(new BasicNameValuePair("ORDER_AMOUNT", amount));
        idnRequestParameters.add(new BasicNameValuePair("ORDER_CURRENCY", currency));
        idnRequestParameters.add(new BasicNameValuePair("IDN_DATE", idnDate));
        idnRequestParameters.add(new BasicNameValuePair("CHARGE_AMOUNT", amount));
        idnRequestParameters.add(new BasicNameValuePair("IDN_PRN", "IDN_PRN"));
        return idnRequestParameters;
    }
}
