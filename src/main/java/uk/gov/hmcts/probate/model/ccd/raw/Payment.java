package uk.gov.hmcts.probate.model.ccd.raw;

import lombok.Data;

@Data
public class Payment {

    private final String status;
    private final String date;
    private final String reference;
    private final String amount;
    private final String method;
    private final String transactionId;
    private final String siteId;
}
