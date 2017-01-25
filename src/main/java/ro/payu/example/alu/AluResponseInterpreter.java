package ro.payu.example.alu;

import org.apache.http.NameValuePair;
import org.joda.money.Money;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class AluResponseInterpreter {

    public boolean isSuccess(List<NameValuePair> responseParameters) {
        for (NameValuePair pair : responseParameters) {
            if (pair.getName().equals("STATUS") && pair.getValue().equals("SUCCESS")) {
                return true;
            }
        }
        return false;
    }

    public Map.Entry<String, Money> interpretResponseParameters(List<NameValuePair> responseParameters, Money originalAmount) {
        System.out.println();
        System.out.println("ALU response:");
        System.out.println(responseParameters);

        String amount = "";
        String currency = "";
        String payuOrderReference = "";
        for (NameValuePair parameter : responseParameters) {
            switch (parameter.getName()) {
                case "AMOUNT":
                    amount = parameter.getValue();
                    break;
                case "CURRENCY":
                    currency = parameter.getValue();
                    break;
                case "REFNO":
                    payuOrderReference = parameter.getValue();
                    break;
            }
        }

        if (!amount.equals("")) {
            return new AbstractMap.SimpleImmutableEntry<>(payuOrderReference, Money.parse(currency + " " + amount));
        }
        return new AbstractMap.SimpleImmutableEntry<>(payuOrderReference, originalAmount);
    }
}
