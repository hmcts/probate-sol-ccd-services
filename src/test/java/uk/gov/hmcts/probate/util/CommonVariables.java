package uk.gov.hmcts.probate.util;

import uk.gov.hmcts.probate.model.ccd.raw.SolsAddress;

public class CommonVariables {

    public static final String YES = "Yes";
    public static final String NO = "No";

    public static final String SOLICITOR_SOT_FORENAME = "Andy Middlename";
    public static final String SOLICITOR_SOT_SURNAME = "Test";
    public static final String SOLICITOR_FIRM_EMAIL = "solicitor@probate-test.com";
    public static final String SOLICITOR_FIRM_PHONE = "0123456789";
    public static final String SOLICITOR_FIRM_NAME = "Sol Firm Name";
    public static final String SOLICITOR_FIRM_LINE1 = "Sols Add Line 1";
    public static final String SOLICITOR_FIRM_POSTCODE = "SW13 6EA";
    public static final SolsAddress SOLICITOR_ADDRESS = SolsAddress.builder().addressLine1(CommonVariables.SOLICITOR_FIRM_LINE1)
            .postCode(CommonVariables.SOLICITOR_FIRM_POSTCODE).build();
    public static final String SOLICITOR_SOT_NOT_APPLYING_REASON = "Power reserved";

    public static final String PRIMARY_EXEC_ALIAS_NAMES = "Alias names";


}
