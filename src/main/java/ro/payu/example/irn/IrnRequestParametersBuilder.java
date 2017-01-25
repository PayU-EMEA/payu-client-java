package ro.payu.example.irn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.money.Money;

import java.text.DecimalFormat;
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

    public List<NameValuePair> build(String payuOrderReference, Money amount) {
        String irnDate = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        final List<NameValuePair> irnRequestParameters = new ArrayList<>();
        irnRequestParameters.add(new BasicNameValuePair("MERCHANT", merchantCode));
        irnRequestParameters.add(new BasicNameValuePair("ORDER_REF", payuOrderReference));
        irnRequestParameters.add(new BasicNameValuePair("ORDER_AMOUNT", new DecimalFormat("#.##").format(amount.getAmount().doubleValue())));
        irnRequestParameters.add(new BasicNameValuePair("ORDER_CURRENCY", amount.getCurrencyUnit().getCode()));
        irnRequestParameters.add(new BasicNameValuePair("IRN_DATE", irnDate));
        return irnRequestParameters;
    }
}
