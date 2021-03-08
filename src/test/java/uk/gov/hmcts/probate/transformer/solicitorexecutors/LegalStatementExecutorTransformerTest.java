package uk.gov.hmcts.probate.transformer.solicitorexecutors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutor;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorApplying;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorNotApplying;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorNotApplyingPowerReserved;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorPartners;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorTrustCorps;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseDetails;
import uk.gov.hmcts.probate.service.solicitorexecutor.ExecutorListMapperService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.probate.util.CommonVariables.DISPENSE_WITH_NOTICE_EXEC;
import static uk.gov.hmcts.probate.util.CommonVariables.EXECUTOR_APPLYING;
import static uk.gov.hmcts.probate.util.CommonVariables.EXECUTOR_NOT_APPLYING;
import static uk.gov.hmcts.probate.util.CommonVariables.EXEC_ADDRESS;
import static uk.gov.hmcts.probate.util.CommonVariables.EXEC_FIRST_NAME;
import static uk.gov.hmcts.probate.util.CommonVariables.EXEC_ID;
import static uk.gov.hmcts.probate.util.CommonVariables.EXEC_SURNAME;
import static uk.gov.hmcts.probate.util.CommonVariables.EXEC_TRUST_CORP_POS;
import static uk.gov.hmcts.probate.util.CommonVariables.NO;
import static uk.gov.hmcts.probate.util.CommonVariables.PARTNER_EXEC;
import static uk.gov.hmcts.probate.util.CommonVariables.PRIMARY_EXEC_ALIAS_NAMES;
import static uk.gov.hmcts.probate.util.CommonVariables.SOLS_EXEC_APPLYING;
import static uk.gov.hmcts.probate.util.CommonVariables.SOLS_EXEC_NOT_APPLYING;
import static uk.gov.hmcts.probate.util.CommonVariables.YES;

@RunWith(MockitoJUnitRunner.class)
public class LegalStatementExecutorTransformerTest {

    private final CaseData.CaseDataBuilder<?, ?> caseDataBuilder = CaseData.builder();

    @Mock
    private CaseDetails caseDetailsMock;

    @Mock
    private ExecutorListMapperService executorListMapperServiceMock;

    @InjectMocks
    private LegalStatementExecutorTransformer legalStatementExecutorTransformerMock;

    private List<CollectionMember<AdditionalExecutorApplying>> additionalExecutorApplying;
    private List<CollectionMember<AdditionalExecutorNotApplying>> additionalExecutorNotApplying;
    private List<CollectionMember<AdditionalExecutor>> solsAdditionalExecutorList;
    private List<CollectionMember<AdditionalExecutorTrustCorps>> trustCorpsExecutorList;
    private List<CollectionMember<AdditionalExecutorPartners>> partnerExecutorList;
    private List<CollectionMember<AdditionalExecutorNotApplyingPowerReserved>> dispenseWithNoticeExecList;

    @Before
    public void setUp() {
        additionalExecutorApplying = new ArrayList<>();
        additionalExecutorApplying.add(new CollectionMember<>(EXEC_ID, EXECUTOR_APPLYING));

        additionalExecutorNotApplying = new ArrayList<>();
        additionalExecutorNotApplying.add(new CollectionMember<>(EXEC_ID, EXECUTOR_NOT_APPLYING));

        solsAdditionalExecutorList = new ArrayList<>();
        solsAdditionalExecutorList.add(SOLS_EXEC_APPLYING);
        solsAdditionalExecutorList.add(SOLS_EXEC_NOT_APPLYING);

        trustCorpsExecutorList = new ArrayList<>();
        trustCorpsExecutorList.add(new CollectionMember(EXEC_ID,
                AdditionalExecutorTrustCorps.builder()
                        .additionalExecForenames(EXEC_FIRST_NAME)
                        .additionalExecLastname(EXEC_SURNAME)
                        .additionalExecutorTrustCorpPosition(EXEC_TRUST_CORP_POS)
                        .additionalExecAddress(EXEC_ADDRESS)
                        .build()));

        partnerExecutorList = new ArrayList<>();
        partnerExecutorList.add(PARTNER_EXEC);

        dispenseWithNoticeExecList = new ArrayList<>();
        dispenseWithNoticeExecList.add(DISPENSE_WITH_NOTICE_EXEC);
    }


    @Test
    public void shouldSetLegalStatementFieldsWithApplyingExecutorInfo() {
        caseDataBuilder
                .additionalExecutorsTrustCorpList(trustCorpsExecutorList)
                .solsAdditionalExecutorList(solsAdditionalExecutorList);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());
        when(executorListMapperServiceMock.mapFromTrustCorpExecutorsToApplyingExecutors(
                caseDetailsMock.getData())).thenReturn(additionalExecutorApplying);
        when(executorListMapperServiceMock.mapFromSolsAdditionalExecutorListToApplyingExecutors(
                caseDetailsMock.getData())).thenReturn(additionalExecutorApplying);

        legalStatementExecutorTransformerMock.mapSolicitorExecutorFieldsToLegalStatementExecutorFields(
                caseDetailsMock.getData());

        List<CollectionMember<AdditionalExecutorApplying>> legalStatementExecutors = new ArrayList<>();
        legalStatementExecutors.addAll(additionalExecutorApplying);
        legalStatementExecutors.addAll(additionalExecutorApplying);

        CaseData caseData = caseDetailsMock.getData();
        assertEquals(legalStatementExecutors, caseData.getExecutorsApplyingLegalStatement());
        assertEquals(new ArrayList<>(), caseData.getExecutorsNotApplyingLegalStatement());
    }


    @Test
    public void shouldSetTrustCorpAddressForLegalStatement() {

        caseDataBuilder
                .additionalExecutorsTrustCorpList(trustCorpsExecutorList)
                .solsAdditionalExecutorList(solsAdditionalExecutorList)
                .trustCorpAddress(EXEC_ADDRESS);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());
        when(executorListMapperServiceMock.mapFromTrustCorpExecutorsToApplyingExecutors(
                caseDetailsMock.getData())).thenReturn(additionalExecutorApplying);
        when(executorListMapperServiceMock.mapFromSolsAdditionalExecutorListToApplyingExecutors(
                caseDetailsMock.getData())).thenReturn(additionalExecutorApplying);

        legalStatementExecutorTransformerMock.mapSolicitorExecutorFieldsToLegalStatementExecutorFields(
                caseDetailsMock.getData());

        List<CollectionMember<AdditionalExecutorApplying>> legalStatementExecutors = new ArrayList<>();
        legalStatementExecutors.addAll(additionalExecutorApplying);
        legalStatementExecutors.addAll(additionalExecutorApplying);

        CaseData caseData = caseDetailsMock.getData();
        assertEquals(legalStatementExecutors, caseData.getExecutorsApplyingLegalStatement());
        assertEquals(EXEC_ADDRESS, caseData.getAdditionalExecutorsTrustCorpList().get(0)
                .getValue().getAdditionalExecAddress());
        assertEquals(new ArrayList<>(), caseData.getExecutorsNotApplyingLegalStatement());
    }

    @Test
    public void shouldSetLegalStatementFieldsWithNotApplyingExecutorInfo() {
        caseDataBuilder
                .dispenseWithNoticeOtherExecsList(dispenseWithNoticeExecList)
                .solsAdditionalExecutorList(solsAdditionalExecutorList);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());
        when(executorListMapperServiceMock.mapFromDispenseWithNoticeExecsToNotApplyingExecutors(
                caseDetailsMock.getData())).thenReturn(additionalExecutorNotApplying);
        when(executorListMapperServiceMock.mapFromSolsAdditionalExecsToNotApplyingExecutors(
                caseDetailsMock.getData())).thenReturn(additionalExecutorNotApplying);

        legalStatementExecutorTransformerMock.mapSolicitorExecutorFieldsToLegalStatementExecutorFields(
                caseDetailsMock.getData());

        List<CollectionMember<AdditionalExecutorNotApplying>> legalStatementExecutors = new ArrayList<>();
        legalStatementExecutors.addAll(additionalExecutorNotApplying);
        legalStatementExecutors.addAll(additionalExecutorNotApplying);

        CaseData caseData = caseDetailsMock.getData();
        assertEquals(legalStatementExecutors, caseData.getExecutorsNotApplyingLegalStatement());
        assertEquals(new ArrayList<>(), caseData.getExecutorsApplyingLegalStatement());
    }

    @Test
    public void shouldSetLegalStatementFieldsWithApplyingExecutorInfo_PrimaryApplicantApplying() {
        caseDataBuilder
            .primaryApplicantForenames(EXEC_FIRST_NAME)
            .primaryApplicantSurname(EXEC_SURNAME)
            .primaryApplicantAlias(PRIMARY_EXEC_ALIAS_NAMES)
            .primaryApplicantAddress(EXEC_ADDRESS)
            .primaryApplicantIsApplying(YES);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());
        when(executorListMapperServiceMock.mapFromPrimaryApplicantToApplyingExecutor(
                caseDetailsMock.getData())).thenReturn(new CollectionMember<>(EXEC_ID, EXECUTOR_APPLYING));

        legalStatementExecutorTransformerMock.mapSolicitorExecutorFieldsToLegalStatementExecutorFields(
                caseDetailsMock.getData());

        CaseData caseData = caseDetailsMock.getData();
        assertEquals(additionalExecutorApplying, caseData.getExecutorsApplyingLegalStatement());
        assertEquals(new ArrayList<>(), caseData.getExecutorsNotApplyingLegalStatement());
    }

    @Test
    public void shouldSetLegalStatementFieldsWithApplyingExecutorInfo_PrimaryApplicantNotApplying() {
        caseDataBuilder
                .primaryApplicantIsApplying(NO)
                .solsSolicitorIsApplying(NO)
                .solsSolicitorIsExec(YES);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());
        when(executorListMapperServiceMock.mapFromPrimaryApplicantToNotApplyingExecutor(
                caseDetailsMock.getData())).thenReturn(new CollectionMember<>(EXEC_ID, EXECUTOR_NOT_APPLYING));

        legalStatementExecutorTransformerMock.mapSolicitorExecutorFieldsToLegalStatementExecutorFields(
                caseDetailsMock.getData());

        CaseData caseData = caseDetailsMock.getData();
        assertEquals(additionalExecutorNotApplying, caseData.getExecutorsNotApplyingLegalStatement());
        assertEquals(new ArrayList<>(), caseData.getExecutorsApplyingLegalStatement());
    }

    @Test
    public void shouldSetLegalStatementFieldsWithApplyingExecutorInfo_SolicitorIsPrimaryApplicant() {
        caseDataBuilder
                .solsSolicitorIsApplying(YES)
                .solsSolicitorIsExec(YES);

        when(caseDetailsMock.getData()).thenReturn(caseDataBuilder.build());
        when(executorListMapperServiceMock.mapFromSolicitorToApplyingExecutor(
                caseDetailsMock.getData())).thenReturn(new CollectionMember<>(EXEC_ID, EXECUTOR_APPLYING));

        legalStatementExecutorTransformerMock.mapSolicitorExecutorFieldsToLegalStatementExecutorFields(
                caseDetailsMock.getData());

        CaseData caseData = caseDetailsMock.getData();
        assertEquals(additionalExecutorApplying, caseData.getExecutorsApplyingLegalStatement());
        assertEquals(new ArrayList<>(), caseData.getExecutorsNotApplyingLegalStatement());
    }

}