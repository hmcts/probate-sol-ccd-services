package uk.gov.hmcts.probate.validator;
import uk.gov.hmcts.probate.model.ccd.caveat.request.CaveatDetails;

public interface CaveatEmailValidationRule {
    String REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]{1,30}(?:\\.[^.\\n]{1,30}){0,30}@[a-z0-9](?:[a-z0-9-.]{0,30}[a-z0-9])?\\.[a-z0-9](?:[a-z0-9-]{0,10}[a-z0-9])?";
    void validate(CaveatDetails caveatDetails);
}
