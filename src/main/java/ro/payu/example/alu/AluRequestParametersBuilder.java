package ro.payu.example.alu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AluRequestParametersBuilder {

    private String merchantCode;

    public AluRequestParametersBuilder(String merchantCode) {
        this.merchantCode = merchantCode;

    }

    public List<NameValuePair> buildRequestParameters() {
        final List<NameValuePair> parameters = new ArrayList<>();

        String orderReference = UUID.randomUUID().toString().substring(0, 17);

        String orderData = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        parameters.add(new BasicNameValuePair("MERCHANT", merchantCode));
        parameters.add(new BasicNameValuePair("ORDER_REF", orderReference));
        parameters.add(new BasicNameValuePair("ORDER_DATE", orderData));
        parameters.add(new BasicNameValuePair("BACK_REF", "http://dev.indispus.ro/emag/return.php"));
        parameters.add(new BasicNameValuePair("ORDER_TIMEOUT", "3000"));

        parameters.add(new BasicNameValuePair("PRICES_CURRENCY", "RON"));

        parameters.add(new BasicNameValuePair("DISCOUNT", "0"));
        parameters.add(new BasicNameValuePair("ORDER_SHIPPING", "0"));

        parameters.add(new BasicNameValuePair("ORDER_PCODE[0]", "TestProductCode"));
        parameters.add(new BasicNameValuePair("ORDER_PINFO[0]", "TestProductInfo"));
        parameters.add(new BasicNameValuePair("ORDER_PNAME[0]", "Test product name"));
        parameters.add(new BasicNameValuePair("ORDER_PRICE[0]", "128.32"));
        parameters.add(new BasicNameValuePair("ORDER_QTY[0]", "2"));
        parameters.add(new BasicNameValuePair("ORDER_VAT[0]", "0"));


        parameters.add(new BasicNameValuePair("PAY_METHOD", "CCVISAMC"));

        parameters.add(new BasicNameValuePair("CC_CVV", "123"));
        parameters.add(new BasicNameValuePair("CC_NUMBER", "4111111111111111"));
        parameters.add(new BasicNameValuePair("CC_NUMBER_TIME", "10.34"));
        parameters.add(new BasicNameValuePair("CC_OWNER", "Andrei Susanu"));
        parameters.add(new BasicNameValuePair("CC_OWNER_TIME", "9.72"));
        parameters.add(new BasicNameValuePair("CC_TYPE", "VISA"));
        parameters.add(new BasicNameValuePair("CLIENT_IP", "91.220.167.100"));
        parameters.add(new BasicNameValuePair("EXP_MONTH", "11"));
        parameters.add(new BasicNameValuePair("EXP_YEAR", "2019"));


        parameters.add(new BasicNameValuePair("BILL_ADDRESS", "Acasă"));
        parameters.add(new BasicNameValuePair("BILL_CIISSUER", "SPCLEP"));
        parameters.add(new BasicNameValuePair("BILL_CISERIAL", "RT"));
        parameters.add(new BasicNameValuePair("BILL_CINUMBER", "696318"));
        parameters.add(new BasicNameValuePair("BILL_CITY", "Bucuresti"));
        parameters.add(new BasicNameValuePair("BILL_CNP", "1840329460055"));
        parameters.add(new BasicNameValuePair("BILL_COUNTRYCODE", "RO"));
        parameters.add(new BasicNameValuePair("BILL_EMAIL", "andrei.susanu@payu.ro"));
        parameters.add(new BasicNameValuePair("BILL_FNAME", "Andrei"));
        parameters.add(new BasicNameValuePair("BILL_LNAME", "Susanu"));
        parameters.add(new BasicNameValuePair("BILL_PHONE", "+40721234567"));
        parameters.add(new BasicNameValuePair("BILL_STATE", "Bucharest"));
        parameters.add(new BasicNameValuePair("BILL_ZIPCODE", "062173"));

        parameters.add(new BasicNameValuePair("DELIVERY_ADDRESS", "Strada Ardealului"));
        parameters.add(new BasicNameValuePair("DELIVERY_CITY", "Bucuresti"));
        parameters.add(new BasicNameValuePair("DELIVERY_COUNTRYCODE", "RO"));
        parameters.add(new BasicNameValuePair("DELIVERY_FNAME", "Susanu"));
        parameters.add(new BasicNameValuePair("DELIVERY_LNAME", "Andrei"));
        parameters.add(new BasicNameValuePair("DELIVERY_PHONE", "+40721234987"));
        parameters.add(new BasicNameValuePair("DELIVERY_STATE", "Sector 1"));
        parameters.add(new BasicNameValuePair("DELIVERY_ZIPCODE", "062173"));


        return parameters;
    }
}
