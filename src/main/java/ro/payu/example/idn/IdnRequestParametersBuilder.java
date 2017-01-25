package ro.payu.example.idn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.money.Money;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IdnRequestParametersBuilder {

    private final String merchantCode;

    public IdnRequestParametersBuilder(String merchantCode) {

        this.merchantCode = merchantCode;
    }

    public List<NameValuePair> build(String payuOrderReference, Money amount) {
        String idnDate = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        final List<NameValuePair> idnRequestParameters = new ArrayList<>();
        idnRequestParameters.add(new BasicNameValuePair("MERCHANT", merchantCode));
        idnRequestParameters.add(new BasicNameValuePair("ORDER_REF", payuOrderReference));
        idnRequestParameters.add(new BasicNameValuePair("ORDER_AMOUNT", new DecimalFormat("#.##").format(amount.getAmount().doubleValue())));
        idnRequestParameters.add(new BasicNameValuePair("ORDER_CURRENCY", amount.getCurrencyUnit().getCode()));
        idnRequestParameters.add(new BasicNameValuePair("IDN_DATE", idnDate));
        return idnRequestParameters;
    }
}
