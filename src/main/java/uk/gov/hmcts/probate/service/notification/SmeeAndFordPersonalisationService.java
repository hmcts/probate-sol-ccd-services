package uk.gov.hmcts.probate.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.model.DocumentType;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorApplying;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.Document;
import uk.gov.hmcts.probate.model.ccd.raw.ProbateAliasName;
import uk.gov.hmcts.probate.model.ccd.raw.ScannedDocument;
import uk.gov.hmcts.probate.model.ccd.raw.SolsAddress;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.model.ccd.raw.request.ReturnedCaseDetails;
import uk.gov.hmcts.probate.service.FileSystemResourceService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.hmcts.probate.model.ApplicationType.SOLICITOR;
import static uk.gov.hmcts.probate.model.Constants.DOC_SUBTYPE_WILL;
import static uk.gov.hmcts.probate.model.Constants.YES;
import static uk.gov.hmcts.probate.model.DocumentType.ADMON_WILL_GRANT;
import static uk.gov.hmcts.probate.model.DocumentType.DIGITAL_GRANT;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmeeAndFordPersonalisationService {
    private static final DateTimeFormatter CONTENT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String PERSONALISATION_SMEE_AND_FORD_NAME = "smeeAndFordName";
    private static final String PERSONALISATION_CASE_DATA = "caseData";
    private static final String COLDICILS_FLAG = "Codicils";
    private static final String SEP = ",";
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";
    private static final String PDF_EXT = ".pdf";
    private static final DocumentType[] GRANT_TYPES = {DIGITAL_GRANT, ADMON_WILL_GRANT};
    private static final String SUBJECT = "Smee And Ford Data extract from :fromDate to :toDate";
    private static final String HEADER_ROW_FILE = "templates/dataExtracts/SmeeAndFordHeaderRow.csv";

    private final FileSystemResourceService fileSystemResourceService;

    public Map<String, String> getSmeeAndFordPersonalisation(List<ReturnedCaseDetails> cases, String fromDate,
                                                             String toDate) {
        HashMap<String, String> personalisation = new HashMap<>();

        StringBuilder data = getSmeeAndFordBuiltData(cases);

        personalisation.put(PERSONALISATION_SMEE_AND_FORD_NAME, getSubject(fromDate, toDate));
        personalisation.put(PERSONALISATION_CASE_DATA, data.toString());

        return personalisation;
    }

    private String getSubject(String fromDate, String toDate) {
        return SUBJECT.replace(":fromDate", fromDate)
            .replace(":toDate", toDate);
    }

    private StringBuilder getSmeeAndFordBuiltData(List<ReturnedCaseDetails> cases) {
        StringBuilder data = new StringBuilder();
        addHeaderRow(data);
        data.append(NEW_LINE);

        for (ReturnedCaseDetails retCase : cases) {
            CaseData currentCaseData = retCase.getData();
            try {
                data.append(currentCaseData.getRegistryLocation());
                data.append(SEP);
                data.append(CONTENT_DATE.format(LocalDate.parse(currentCaseData.getGrantIssuedDate())));
                data.append(SEP);
                data.append(retCase.getId().toString());
                data.append(SEP);
                data.append(getDeceasedNameWithHonours(currentCaseData));
                data.append(SEP);
                data.append(getDeceasedAliasNames(currentCaseData));
                data.append(SEP);
                data.append(getCaseType(currentCaseData));
                data.append(SEP);
                data.append(CONTENT_DATE.format(currentCaseData.getDeceasedDateOfDeath()));
                data.append(SEP);
                data.append(getAddress(currentCaseData.getDeceasedAddress()));
                data.append(SEP);
                data.append(getApplyingExecutorDetails(currentCaseData.getAdditionalExecutorsApplying()));
                data.append(SEP);
                data.append(getPrimaryApplicantDetails(currentCaseData));
                data.append(SEP);
                data.append(currentCaseData.getIhtGrossValue().toString());
                data.append(SEP);
                data.append(currentCaseData.getIhtNetValue().toString());
                data.append(SEP);
                data.append(getSolicitorDetails(currentCaseData));
                data.append(SEP);
                data.append(CONTENT_DATE.format(currentCaseData.getDeceasedDateOfBirth()));
                data.append(SEP);
                data.append(hasCodicil(currentCaseData));
                data.append(SEP);
                data.append(getWillFileName(currentCaseData));
                data.append(SEP);
                data.append(getGrantFileName(currentCaseData));
                data.append(NEW_LINE);
            } catch (Exception e) {
                data.append(retCase.getId().toString());
                data.append(", ");
                data.append(e.toString());
                data.append("\n");
            }
        }
        return data;
    }

    private void addHeaderRow(StringBuilder data) {
        String header = fileSystemResourceService.getFileFromResourceAsString(HEADER_ROW_FILE);
        data.append(header);
    }

    private String getCaseType(CaseData data) {
        return data.getCaseType();
    }

    private String getGrantFileName(CaseData data) {
        List<DocumentType> documentTypes = Arrays.asList(GRANT_TYPES);
        for (CollectionMember<Document> document : data.getProbateDocumentsGenerated()) {
            if (documentTypes.contains(document.getValue().getDocumentType())) {
                return document.getValue().getDocumentType().getTemplateName() + PDF_EXT;
            }
        }
        return "";
    }

    private String getWillFileName(CaseData data) {
        if (data.getScannedDocuments() != null) {
            for (CollectionMember<ScannedDocument> document : data.getScannedDocuments()) {
                if (DocumentType.OTHER.name().equalsIgnoreCase(document.getValue().getType()) 
                    && DOC_SUBTYPE_WILL.equals(document.getValue().getSubtype())) {
                    return document.getValue().getFileName();
                }
            }
        }
        return "";
    }

    private String hasCodicil(CaseData data) {
        if (YES.equals(data.getWillHasCodicils())) {
            return COLDICILS_FLAG;
        } else {
            return "";
        }
    }

    private String getSolicitorDetails(CaseData data) {
        String sol = "";
        if (SOLICITOR.equals(data.getApplicationType())) {
            sol = sol + ifNotEmpty(data.getSolsSolicitorFirmName());
            sol = sol + SPACE + ifNotEmpty(getAddress(data.getSolsSolicitorAddress()));
            sol = sol + SPACE + ifNotEmpty(data.getSolsSolicitorAppReference());
        }
        
        return sol;
    }

    private String getPrimaryApplicantDetails(CaseData data) {
        String primary = "";
        primary = primary + ifNotEmpty(data.getPrimaryApplicantForenames());
        primary = primary + SPACE + ifNotEmpty(data.getPrimaryApplicantSurname());
        primary = primary + SPACE + ifNotEmpty(data.getPrimaryApplicantAlias());
        primary = primary + SPACE + ifNotEmpty(getAddress(data.getPrimaryApplicantAddress()));
        
        return primary;
    }

    private String getApplyingExecutorDetails(List<CollectionMember<AdditionalExecutorApplying>> 
                                                  additionalExecutorsApplying) {
        int execCount = 0;
        StringBuilder allExecs = new StringBuilder();
        if (additionalExecutorsApplying != null) {
            for (CollectionMember<AdditionalExecutorApplying> applying : additionalExecutorsApplying) {
                allExecs.append(SPACE + ifNotEmpty(applying.getValue().getApplyingExecutorName()));
                allExecs.append(SPACE + ifNotEmpty(applying.getValue().getApplyingExecutorFirstName()));
                allExecs.append(SPACE + ifNotEmpty(applying.getValue().getApplyingExecutorLastName()));
                allExecs.append(SPACE + ifNotEmpty(applying.getValue().getApplyingExecutorOtherNames()));
                allExecs.append(SPACE + ifNotEmpty(getAddress(applying.getValue().getApplyingExecutorAddress())));
                allExecs.append(SEP);
                execCount++;
                if (execCount > 3) {
                    break;
                }
            }

        }
        while (execCount < 3) {
            allExecs.append(SEP);
            execCount++;
        }

        String execsData = allExecs.toString();
        execsData = removeLastSeparator(execsData);
        return execsData;
    }

    private String getAddress(SolsAddress address) {
        StringBuilder addBuilder = new StringBuilder();
        if (address != null) {
            addBuilder.append(SPACE + ifNotEmpty(address.getAddressLine1()));
            addBuilder.append(SPACE + ifNotEmpty(address.getAddressLine2()));
            addBuilder.append(SPACE + ifNotEmpty(address.getAddressLine3()));
            addBuilder.append(SPACE + ifNotEmpty(address.getCounty()));
            addBuilder.append(SPACE + ifNotEmpty(address.getPostTown()));
            addBuilder.append(SPACE + ifNotEmpty(address.getPostCode()));
            addBuilder.append(SPACE + ifNotEmpty(address.getCountry()));
        }
        
        return addBuilder.toString();
    }

    private String ifNotEmpty(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        } else {
            return value + SPACE;
        }
    }

    private String getDeceasedAliasNames(CaseData data) {
        StringBuilder aliases = new StringBuilder();
        if (data.getDeceasedAliasNameList() != null)  {
            for (CollectionMember<ProbateAliasName> alias : data.getDeceasedAliasNameList()) {
                aliases.append(ifNotEmpty(alias.getValue().getForenames()) + SPACE 
                    + ifNotEmpty(alias.getValue().getLastName()));
            }
        }
        return aliases.toString();
    }

    private String getDeceasedNameWithHonours(CaseData data) {
        return ifNotEmpty(data.getDeceasedForenames()) + SPACE + ifNotEmpty(data.getDeceasedSurname()) 
            + SPACE + ifNotEmpty(data.getBoDeceasedHonours());
    }

    private String removeLastSeparator(String data) {
        return data.substring(0, data.lastIndexOf(SEP));
    }

}
