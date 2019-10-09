package uk.gov.hmcts.probate.service.docmosis.assembler;

import org.junit.Test;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorApplying;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.ParagraphDetail;
import uk.gov.hmcts.probate.model.ccd.raw.SolsAddress;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AssembleEntitlementTest {

    private static final String YES = "Yes";
    private AssemblerBase assemblerBase = new AssemblerBase();

    private AssembleEntitlement assembleEntitlement = new AssembleEntitlement(assemblerBase);

    @Test
    public void testExecutorNotAccountedForSingleApplicant() {

        CaseData caseData = CaseData.builder().primaryApplicantForenames("primary fn").primaryApplicantSurname("primary sn").build();

        List<ParagraphDetail> response = assembleEntitlement.executorNotAccountedFor(ParagraphCode.IHT421Await, caseData);
        assertEquals(response.get(0).getCode(), "IHT421Await");
        assertEquals(response.get(0).getTemplateName(), "FL-PRB-GNO-ENG-00125.docx");
        assertEquals(response.get(0).getEnableType().name(), "Text");
        assertEquals(response.get(0).getLabel(), "Awaiting IHT421");
        assertEquals(response.get(0).getTextValue(), "primary fn primary sn");
        assertEquals(response.get(0).getTextAreaValue(), null);
    }

    @Test
    public void testExecutorNotAccountedForMultipleApplicant() {

        CollectionMember<AdditionalExecutorApplying> additionalExecutor =
                new CollectionMember<>(AdditionalExecutorApplying.builder().applyingExecutorName("Bob Smith")
                        .applyingExecutorAddress(SolsAddress.builder().addressLine1("123 Fake street")
                                .addressLine3("North West East Field")
                                .postCode("AB2 3CD")
                                .build()).build());
        List<CollectionMember<AdditionalExecutorApplying>> additionalExecutors = new ArrayList<>(1);
        additionalExecutors.add(additionalExecutor);

        CaseData caseData = CaseData.builder()
                .primaryApplicantForenames("primary fn")
                .primaryApplicantSurname("primary sn")
                .additionalExecutorsApplying(additionalExecutors)
                .build();


        List<ParagraphDetail> response = assembleEntitlement.executorNotAccountedFor(ParagraphCode.IHT421Await, caseData);
        assertEquals(response.get(0).getCode(), "IHT421Await");
        assertEquals(response.get(0).getTemplateName(), "FL-PRB-GNO-ENG-00125.docx");
        assertEquals(response.get(0).getEnableType().name(), "Text");
        assertEquals(response.get(0).getLabel(), "Awaiting IHT421");
        assertEquals(response.get(0).getTextValue(), "primary fn primary sn,Bob Smith");
        assertEquals(response.get(0).getTextAreaValue(), null);
    }

}
