package uk.gov.hmcts.probate.transformer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorApplying;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorNotApplying;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.SolsAddress;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseDetails;
import uk.gov.hmcts.probate.model.ccd.raw.response.ResponseCaseData;
import uk.gov.hmcts.probate.service.SolicitorExecutorService;
import uk.gov.hmcts.probate.util.CommonVariables;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SolicitorExecutorTransformerTest {

    private CaseData.CaseDataBuilder caseDataBuilder;

    private ResponseCaseData.ResponseCaseDataBuilder responseCaseDataBuilder;

    @Mock
    private CaseDetails caseDetailsMock;

    @InjectMocks
    private SolicitorExecutorService solicitorExecutorService;

    @InjectMocks
    private SolicitorExecutorTransformer solicitorExecutorTransformerMock;

    List<CollectionMember<AdditionalExecutorApplying>> additionalExecutorApplying;
    List<CollectionMember<AdditionalExecutorNotApplying>> additionalExecutorNotApplying;


    @Before
    public void setUp() {
        caseDataBuilder = CaseData.builder()
            .solsSolicitorIsExec(CommonVariables.YES)
            .solsSolicitorIsMainApplicant(CommonVariables.YES)
            .solsSOTForenames(CommonVariables.SOLICITOR_SOT_FORENAME)
            .solsSOTSurname(CommonVariables.SOLICITOR_SOT_SURNAME)
            .solsSolicitorPhoneNumber(CommonVariables.SOLICITOR_FIRM_PHONE)
            .solsSolicitorEmail(CommonVariables.SOLICITOR_FIRM_EMAIL)
            .solsSolicitorAddress(CommonVariables.SOLICITOR_ADDRESS);

        responseCaseDataBuilder = ResponseCaseData.builder();

        additionalExecutorApplying = new ArrayList<>();
        additionalExecutorNotApplying = new ArrayList<>();

        AdditionalExecutorApplying execApplying = AdditionalExecutorApplying.builder()
                .applyingExecutorName(CommonVariables.SOLICITOR_SOT_FORENAME + " " + CommonVariables.SOLICITOR_SOT_SURNAME)
                .applyingExecutorPhoneNumber(CommonVariables.SOLICITOR_FIRM_PHONE)
                .applyingExecutorEmail(CommonVariables.SOLICITOR_FIRM_EMAIL)
                .applyingExecutorAddress(CommonVariables.SOLICITOR_ADDRESS)
                .build();
        additionalExecutorApplying.add(new CollectionMember<>(CommonVariables.SOL_AS_EXEC_ID, execApplying));

        AdditionalExecutorNotApplying execNotApplying = AdditionalExecutorNotApplying.builder()
                .notApplyingExecutorName(CommonVariables.SOLICITOR_SOT_FORENAME + " " + CommonVariables.SOLICITOR_SOT_SURNAME)
                .notApplyingExecutorReason(CommonVariables.SOLICITOR_SOT_NOT_APPLYING_REASON)
                .build();
        additionalExecutorNotApplying.add(new CollectionMember<>(CommonVariables.SOL_AS_EXEC_ID, execNotApplying));
    }

    @Test
    public void shouldSetMainApplicantDetailsWithSolicitorInfo(){
        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.mainApplicantTransformation(caseDetailsMock.getData(), responseCaseDataBuilder);

        assertEquals(CommonVariables.SOLICITOR_SOT_FORENAME, responseCaseDataBuilder.build().getPrimaryApplicantForenames());
        assertEquals(CommonVariables.SOLICITOR_SOT_SURNAME, responseCaseDataBuilder.build().getPrimaryApplicantSurname());
        assertEquals(CommonVariables.SOLICITOR_FIRM_PHONE, responseCaseDataBuilder.build().getPrimaryApplicantPhoneNumber());
        assertEquals(CommonVariables.SOLICITOR_FIRM_EMAIL, responseCaseDataBuilder.build().getPrimaryApplicantEmailAddress());
        assertEquals(CommonVariables.SOLICITOR_ADDRESS, responseCaseDataBuilder.build().getPrimaryApplicantAddress());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantAlias());
        assertEquals(CommonVariables.NO, responseCaseDataBuilder.build().getPrimaryApplicantHasAlias());
        assertEquals(CommonVariables.YES, responseCaseDataBuilder.build().getPrimaryApplicantIsApplying());
        assertEquals(CommonVariables.YES, responseCaseDataBuilder.build().getSolsSolicitorIsApplying());
        assertEquals(null, responseCaseDataBuilder.build().getSolsSolicitorNotApplyingReason());
        assertEquals(null, responseCaseDataBuilder.build().getSolsPrimaryExecutorNotApplyingReason());
    }

    @Test
    public void shouldRemoveSolicitorAsMainApplicant(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.NO)
                .solsSolicitorIsMainApplicant(CommonVariables.NO)
                .solsSolicitorIsApplying(CommonVariables.NO)
                .solsSolicitorNotApplyingReason("Not applying");

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.mainApplicantTransformation(caseDetailsMock.getData(), responseCaseDataBuilder);

        assertEquals(null, responseCaseDataBuilder.build().getSolsSolicitorIsMainApplicant());
        assertEquals(null, responseCaseDataBuilder.build().getSolsSolicitorIsApplying());
        assertEquals(null, responseCaseDataBuilder.build().getSolsSolicitorNotApplyingReason());
    }



    @Test
    public void shouldRemoveSolicitorPrimaryApplicantDetails(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.YES)
                .solsSolicitorIsMainApplicant(CommonVariables.NO)
                .solsSOTForenames("Forename")
                .solsSOTSurname("Surname")
                .primaryApplicantForenames("Forename")
                .primaryApplicantSurname("Surname")
                .primaryApplicantEmailAddress("email@mail.com")
                .primaryApplicantPhoneNumber("1234567890")
                .primaryApplicantAddress(mock(SolsAddress.class))
                .primaryApplicantAlias("Alias")
                .primaryApplicantHasAlias(CommonVariables.YES)
                .primaryApplicantIsApplying(CommonVariables.YES)
                .solsSolicitorIsApplying(CommonVariables.NO)
                .solsSolicitorNotApplyingReason("Not applying");

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.mainApplicantTransformation(caseDetailsMock.getData(), responseCaseDataBuilder);

        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantForenames());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantSurname());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantPhoneNumber());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantEmailAddress());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantAddress());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantAlias());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantHasAlias());
        assertEquals(null, responseCaseDataBuilder.build().getPrimaryApplicantIsApplying());
        assertEquals(null, responseCaseDataBuilder.build().getSolsPrimaryExecutorNotApplyingReason());
    }

    @Test
    public void shouldRemoveSolicitorPrimaryApplicantDetailsIsApplying(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.YES)
                .solsSolicitorIsMainApplicant(CommonVariables.NO)
                .solsSOTForenames("Forename")
                .solsSOTSurname("Surname")
                .primaryApplicantIsApplying(CommonVariables.YES)
                .solsSolicitorIsApplying(CommonVariables.YES)
                .solsSolicitorNotApplyingReason("Not applying");


        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.mainApplicantTransformation(caseDetailsMock.getData(), responseCaseDataBuilder);

        assertEquals(null, responseCaseDataBuilder.build().getSolsPrimaryExecutorNotApplyingReason());
    }

    @Test
    public void shouldSetPrimaryExecutorNotApplyingReasonToNULLForNULLSolsApplyAsExecTransform(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.YES)
                .solsSolicitorIsMainApplicant(CommonVariables.NO)
                .solsSolicitorIsApplying(null)
                .solsPrimaryExecutorNotApplyingReason(CommonVariables.SOLICITOR_SOT_NOT_APPLYING_REASON);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.mainApplicantTransformation(caseDetailsMock.getData(), responseCaseDataBuilder);

        assertEquals(null, responseCaseDataBuilder.build().getSolsSolicitorNotApplyingReason());
    }

    @Test
    public void shouldUpdateExecutorListsSolsIsExecNotMainApplicantIsApplyingTransform(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.YES)
                .solsSolicitorIsMainApplicant(CommonVariables.NO)
                .solsSolicitorIsApplying(CommonVariables.YES);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.solicitorExecutorTransformation(caseDetailsMock.getData(), solicitorExecutorService, responseCaseDataBuilder);

        assertEquals(additionalExecutorApplying, responseCaseDataBuilder.build().getAdditionalExecutorsApplying());
        assertTrue(responseCaseDataBuilder.build().getAdditionalExecutorsNotApplying().isEmpty());
    }

    @Test
    public void shouldUpdateExecutorListsSolsIsExecNotMainApplicantIsNotApplyingTransform(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.YES)
                .solsSolicitorIsMainApplicant(CommonVariables.NO)
                .solsSolicitorIsApplying(CommonVariables.NO)
                .solsSolicitorNotApplyingReason(CommonVariables.SOLICITOR_SOT_NOT_APPLYING_REASON);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.solicitorExecutorTransformation(caseDetailsMock.getData(), solicitorExecutorService, responseCaseDataBuilder);

        assertTrue(responseCaseDataBuilder.build().getAdditionalExecutorsApplying().isEmpty());
        assertEquals(additionalExecutorNotApplying, responseCaseDataBuilder.build().getAdditionalExecutorsNotApplying());
    }

    @Test
    public void shouldUpdateExecutorListsSolsIsNotExecTransform(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.NO)
                .solsSolicitorNotApplyingReason(CommonVariables.SOLICITOR_SOT_NOT_APPLYING_REASON);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.solicitorExecutorTransformation(caseDetailsMock.getData(), solicitorExecutorService, responseCaseDataBuilder);

        assertTrue(responseCaseDataBuilder.build().getAdditionalExecutorsApplying().isEmpty());
        assertTrue(responseCaseDataBuilder.build().getAdditionalExecutorsNotApplying().isEmpty());
    }

    @Test
    public void shouldUpdateExecutorListsSolsIsNotExecIsMainApplicantTransform(){
        caseDataBuilder
                .solsSolicitorIsExec(CommonVariables.NO)
                .solsSolicitorIsMainApplicant(CommonVariables.YES)
                .solsSolicitorNotApplyingReason(CommonVariables.SOLICITOR_SOT_NOT_APPLYING_REASON);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());

        solicitorExecutorTransformerMock.solicitorExecutorTransformation(caseDetailsMock.getData(), solicitorExecutorService, responseCaseDataBuilder);

        assertTrue(responseCaseDataBuilder.build().getAdditionalExecutorsApplying().isEmpty());
        assertTrue(responseCaseDataBuilder.build().getAdditionalExecutorsNotApplying().isEmpty());
    }

}
