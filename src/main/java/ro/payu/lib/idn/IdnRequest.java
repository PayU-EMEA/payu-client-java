package ro.payu.lib.idn;

public class IdnRequest {

    private String merchantCode;
    private String orderReference;
    private String orderAmount;
    private String orderCurrency;
    private String requestDate;
    private String signature;
    private String chargeAmount;

    private IdnRequest() {
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getSignature() {
        return signature;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public static class IdnRequestBuilder {
        private String merchantCode;
        private String orderReference;
        private String orderAmount;
        private String chargeAmount;
        private String orderCurrency;
        private String requestDate;
        private String signature;

        public IdnRequest build() {
            IdnRequest request = new IdnRequest();
            request.merchantCode = this.merchantCode;
            request.orderReference = this.orderReference;
            request.orderAmount = this.orderAmount;
            request.chargeAmount = this.chargeAmount;
            request.orderCurrency = this.orderCurrency;
            request.requestDate = this.requestDate;
            request.signature = this.signature;

            return request;
        }

        public IdnRequestBuilder setMerchantCode(String merchantCode) {
            this.merchantCode = merchantCode;
            return this;
        }

        public IdnRequestBuilder setOrderReference(String orderReference) {
            this.orderReference = orderReference;
            return this;
        }

        public IdnRequestBuilder setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
            return this;
        }

        public IdnRequestBuilder setOrderCurrency(String orderCurrency) {
            this.orderCurrency = orderCurrency;
            return this;
        }

        public IdnRequestBuilder setRequestDate(String requestDate) {
            this.requestDate = requestDate;
            return this;
        }

        public IdnRequestBuilder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public IdnRequestBuilder setChargeAmount(String chargeAmount) {
            this.chargeAmount = chargeAmount;
            return this;
        }
    }
}
