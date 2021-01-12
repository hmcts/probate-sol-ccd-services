package uk.gov.hmcts.probate.model.ccd.raw.response;

import uk.gov.hmcts.probate.model.ccd.raw.DynamicList;

public class ResponseCaseDataParent {

    protected DynamicList reprintDocument;

    protected String reprintNumberOfCopies;

    protected DynamicList solsAmendLegalStatmentSelect;

    protected String declarationCheckbox;
    protected String ihtGrossValueField;
    protected String ihtNetValueField;
    protected String deceasedForeignDeathCertTranslation;
    protected String deceasedForeignDeathCertInEnglish;
    protected String deceasedDiedEngOrWales;
    protected String deceasedDeathCertificate;
    protected Long numberOfExecutors;
    protected Long numberOfApplicants;
    protected String legalDeclarationJson;
    protected String checkAnswersSummaryJson;
    protected String registryAddress;
    protected String registryEmailAddress;
    protected String registrySequenceNumber;
    protected String iht217;

    ResponseCaseDataParent() {
    }

    ResponseCaseDataParent(DynamicList reprintDocument, String reprintNumberOfCopies, DynamicList solsAmendLegalStatmentSelect, String declarationCheckbox, String ihtGrossValueField, String ihtNetValueField, String deceasedDiedEngOrWales, String deceasedDeathCertificate, String deceasedForeignDeathCertInEnglish, String deceasedForeignDeathCertTranslation, Long numberOfExecutors, Long numberOfApplicants, String legalDeclarationJson,
                           String checkAnswersSummaryJson,
                           String registryAddress, String registryEmailAddress, String registrySequenceNumber, String iht217) {
        this.reprintDocument = reprintDocument;
        this.reprintNumberOfCopies = reprintNumberOfCopies;
        this.solsAmendLegalStatmentSelect = solsAmendLegalStatmentSelect;
        this.declarationCheckbox = declarationCheckbox;
        this.ihtGrossValueField = ihtGrossValueField;
        this.ihtNetValueField = ihtNetValueField;
        this.deceasedDiedEngOrWales = deceasedDiedEngOrWales;
        this.deceasedDeathCertificate = deceasedDeathCertificate;
        this.deceasedForeignDeathCertInEnglish = deceasedForeignDeathCertInEnglish;
        this.deceasedForeignDeathCertTranslation = deceasedForeignDeathCertTranslation;
        this.numberOfExecutors = numberOfExecutors;
        this.numberOfApplicants = numberOfApplicants;
        this.legalDeclarationJson = legalDeclarationJson;
        this.checkAnswersSummaryJson = checkAnswersSummaryJson;
        this.registryAddress = registryAddress;
        this.registryEmailAddress = registryEmailAddress;
        this.registrySequenceNumber = registrySequenceNumber;
        this.iht217 = iht217;
    }

    public DynamicList getReprintDocument() {
        return reprintDocument;
    }

    public String getReprintNumberOfCopies() {
        return reprintNumberOfCopies;
    }

    public DynamicList getSolsAmendLegalStatmentSelect() {
        return solsAmendLegalStatmentSelect;
    }

    public String getDeclarationCheckbox() {
        return declarationCheckbox;
    }
    
    public String getIhtNetValueField() { return ihtNetValueField; }

    public String getIhtGrossValueField() {
        return ihtGrossValueField;
    }

    public String getIht217() {
        return iht217;
    }

    public String getDeceasedDiedEngOrWales() {
        return deceasedDiedEngOrWales;
    }

    public String getDeceasedDeathCertificate() { return deceasedDeathCertificate; }

    public String getDeceasedForeignDeathCertInEnglish() { return deceasedForeignDeathCertInEnglish; }

    public String getDeceasedForeignDeathCertTranslation() { return deceasedForeignDeathCertTranslation; }

    public Long getNumberOfExecutors() {
        return numberOfExecutors;
    }

    public Long getNumberOfApplicants() {
        return numberOfApplicants;
    }

    public String getLegalDeclarationJson() {
        return legalDeclarationJson;
    }

    public String getCheckAnswersSummaryJson() {
        return checkAnswersSummaryJson;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public String getRegistryEmailAddress() {
        return registryEmailAddress;
    }

    public String getRegistrySequenceNumber() {
        return registrySequenceNumber;
    }

    public static ResponseCaseDataParentBuilder builder() {
        return new ResponseCaseDataParentBuilder();
    }

    public static class ResponseCaseDataParentBuilder {
        protected DynamicList reprintDocument;
        protected String reprintNumberOfCopies;
        protected DynamicList solsAmendLegalStatmentSelect;
        protected String declarationCheckbox;
        protected String ihtNetValueField;
        protected String ihtGrossValueField;
        protected String deceasedDiedEngOrWales;
        protected String deceasedDeathCertificate;
        protected String deceasedForeignDeathCertInEnglish;
        protected String deceasedForeignDeathCertTranslation;
        protected Long numberOfExecutors;
        protected Long numberOfApplicants;
        protected String legalDeclarationJson;
        protected String checkAnswersSummaryJson;
        protected String registryAddress;
        protected String registryEmailAddress;
        protected String registrySequenceNumber;
        protected String iht217;

        ResponseCaseDataParentBuilder() {
        }

        public ResponseCaseDataParentBuilder reprintDocument(DynamicList reprintDocument) {
            this.reprintDocument = reprintDocument;
            return this;
        }

        public ResponseCaseDataParentBuilder reprintNumberOfCopies(String reprintNumberOfCopies) {
            this.reprintNumberOfCopies = reprintNumberOfCopies;
            return this;
        }

        public ResponseCaseDataParentBuilder solsAmendLegalStatmentSelect(DynamicList solsAmendLegalStatmentSelect) {
            this.solsAmendLegalStatmentSelect = solsAmendLegalStatmentSelect;
            return this;
        }

        public ResponseCaseDataParentBuilder declarationCheckbox(String declarationCheckbox) {
            this.declarationCheckbox = declarationCheckbox;
            return this;
        }

        public ResponseCaseDataParentBuilder ihtNetValueField(String ihtNetValueField) {
            this.ihtNetValueField = ihtNetValueField;
            return this;
        }

        public ResponseCaseDataParentBuilder iht217(String iht217) {
            this.iht217 = iht217;
            return this;
        }

        public ResponseCaseDataParentBuilder ihtGrossValueField(String ihtGrossValueField) {
            this.ihtGrossValueField = ihtGrossValueField;
            return this;
        }

        public ResponseCaseDataParentBuilder deceasedDiedEngOrWales(String deceasedDiedEngOrWales) {
            this.deceasedDiedEngOrWales = deceasedDiedEngOrWales;
            return this;
        }

        public ResponseCaseDataParentBuilder deceasedDeathCertificate(String deceasedDeathCertificate) {
            this.deceasedDeathCertificate = deceasedDeathCertificate;
            return this;
        }

        public ResponseCaseDataParentBuilder deceasedForeignDeathCertInEnglish(String deceasedForeignDeathCertInEnglish) {
            this.deceasedForeignDeathCertInEnglish = deceasedForeignDeathCertInEnglish;
            return this;
        }

        public ResponseCaseDataParentBuilder deceasedForeignDeathCertTranslation(String deceasedForeignDeathCertTranslation) {
            this.deceasedForeignDeathCertTranslation = deceasedForeignDeathCertTranslation;
            return this;
        }

        public ResponseCaseDataParentBuilder numberOfExecutors(Long numberOfExecutors) {
            this.numberOfExecutors = numberOfExecutors;
            return this;
        }
        public ResponseCaseDataParentBuilder numberOfApplicants(Long numberOfApplicants) {
            this.numberOfApplicants = numberOfApplicants;
            return this;
        }
        public ResponseCaseDataParentBuilder legalDeclarationJson(String legalDeclarationJson) {
            this.legalDeclarationJson = legalDeclarationJson;
            return this;
        }
        public ResponseCaseDataParentBuilder checkAnswersSummaryJson(String checkAnswersSummaryJson) {
            this.checkAnswersSummaryJson = checkAnswersSummaryJson;
            return this;
        }

        public ResponseCaseDataParentBuilder registryAddress(String registryAddress) {
            this.registryAddress = registryAddress;
            return this;
        }

        public ResponseCaseDataParentBuilder registryEmailAddress(String registryEmailAddress) {
            this.registryEmailAddress = registryEmailAddress;
            return this;
        }

        public ResponseCaseDataParentBuilder registrySequenceNumber(String registrySequenceNumber) {
            this.registrySequenceNumber = registrySequenceNumber;
            return this;
        }

        public ResponseCaseDataParent build() {
            return new ResponseCaseDataParent(reprintDocument, reprintNumberOfCopies, 
                solsAmendLegalStatmentSelect, declarationCheckbox, ihtGrossValueField, ihtNetValueField,
                    deceasedDiedEngOrWales, deceasedDeathCertificate, deceasedForeignDeathCertInEnglish,
                    deceasedForeignDeathCertTranslation, numberOfExecutors, numberOfApplicants,
                    legalDeclarationJson, checkAnswersSummaryJson, registryAddress,
                    registryEmailAddress, registrySequenceNumber, iht217);
        }
    }
}
