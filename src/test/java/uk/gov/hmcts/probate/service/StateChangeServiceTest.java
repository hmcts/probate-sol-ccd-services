package uk.gov.hmcts.probate.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.changerule.DiedOrNotApplyingRule;
import uk.gov.hmcts.probate.changerule.DomicilityRule;
import uk.gov.hmcts.probate.changerule.EntitledMinorityRule;
import uk.gov.hmcts.probate.changerule.ExecutorsRule;
import uk.gov.hmcts.probate.changerule.LifeInterestRule;
import uk.gov.hmcts.probate.changerule.MinorityInterestRule;
import uk.gov.hmcts.probate.changerule.ApplicantSiblingsRule;
import uk.gov.hmcts.probate.changerule.NoOriginalWillRule;
import uk.gov.hmcts.probate.changerule.RenouncingRule;
import uk.gov.hmcts.probate.changerule.ResiduaryRule;
import uk.gov.hmcts.probate.changerule.SpouseOrCivilRule;
import uk.gov.hmcts.probate.changerule.UpdateApplicationRule;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
public class StateChangeServiceTest {

    @InjectMocks
    private StateChangeService underTest;

    @Mock
    private ApplicantSiblingsRule applicantSiblingsRule;
    @Mock
    private DiedOrNotApplyingRule diedOrNotApplyingRule;
    @Mock
    private DomicilityRule domicilityRule;
    @Mock
    private EntitledMinorityRule entitledMinorityRule;
    @Mock
    private ExecutorsRule executorsStateRule;
    @Mock
    private LifeInterestRule lifeInterestRule;
    @Mock
    private MinorityInterestRule minorityInterestRule;
    @Mock
    private NoOriginalWillRule noOriginalWillRule;
    @Mock
    private RenouncingRule renouncingRule;
    @Mock
    private ResiduaryRule residuaryRule;
    @Mock
    private SpouseOrCivilRule spouseOrCivilRule;
    @Mock
    private UpdateApplicationRule updateApplicationRule;

    @Mock
    private CaseData caseDataMock;

    private static final String WILL_TYPE_PROBATE = "WillLeft";
    private static final String WILL_TYPE_INTESTACY = "NoWill";
    private static final String WILL_TYPE_ADMON = "WillLeftAnnexed";
    private static final String STATE_GRANT_TYPE_PROBATE = "SolProbateCreated";
    private static final String STATE_GRANT_TYPE_INTESTACY = "SolIntestacyCreated";
    private static final String STATE_GRANT_TYPE_ADMON = "SolAdmonCreated";

    @Before
    public void setup() {
        initMocks(this);

        underTest = new StateChangeService(applicantSiblingsRule, diedOrNotApplyingRule, domicilityRule,
                entitledMinorityRule, executorsStateRule, lifeInterestRule, minorityInterestRule, noOriginalWillRule,
                renouncingRule, residuaryRule, spouseOrCivilRule, updateApplicationRule);
    }

    @Test
    public void shouldChangeStateForAnyRuleValid() {
        when(domicilityRule.isChangeNeeded(caseDataMock)).thenReturn(true);
        when(noOriginalWillRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldChangeStateForApplicantSiblingsRuleValid() {
        when(applicantSiblingsRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForIntestacyUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForApplicantSiblingsRule() {
        when(applicantSiblingsRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForDiedOrNotApplyingRuleValid() {
        when(diedOrNotApplyingRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForDiedOrNotApplyingRule() {
        when(diedOrNotApplyingRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForDomicilityRuleValid() {
        when(domicilityRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForDomicilityRule() {
        when(domicilityRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForEntitledMinorityRuleValid() {
        when(entitledMinorityRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForEntitledMinority() {
        when(entitledMinorityRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForExecutorsRuleValid() {
        when(executorsStateRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForExecutors() {
        when(executorsStateRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForLifeInterestRuleValid() {
        when(lifeInterestRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForIntestacyUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForLifeInterestRule() {
        when(lifeInterestRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForMinorityInterestRuleValid() {
        when(minorityInterestRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForIntestacyUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForMinorityInterestRule() {
        when(minorityInterestRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForOriginalWillRuleValid() {
        when(noOriginalWillRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForOriginalWillRule() {
        when(noOriginalWillRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForRenouncingRuleValid() {
        when(renouncingRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForIntestacyUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForRenouncingRule() {
        when(renouncingRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForResiduaryRuleValid() {
        when(residuaryRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForIntestacyUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForResiduaryRule() {
        when(residuaryRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldChangeStateForSpouseOrCivilRuleValid() {
        when(spouseOrCivilRule.isChangeNeeded(caseDataMock)).thenReturn(true);

        Optional<String> newState = underTest.getChangedStateForIntestacyUpdate(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals("Stopped", newState.get());
    }

    @Test
    public void shouldNOTChangeStateForSpouseOrCivilRule() {
        when(spouseOrCivilRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertEquals(Optional.empty(), newState);
    }

    @Test
    public void shouldNOTChangeStateForAllRulesInvalid() {
        when(minorityInterestRule.isChangeNeeded(caseDataMock)).thenReturn(false);
        when(noOriginalWillRule.isChangeNeeded(caseDataMock)).thenReturn(false);
        when(executorsStateRule.isChangeNeeded(caseDataMock)).thenReturn(false);
        when(domicilityRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseUpdate(caseDataMock);

        assertFalse(newState.isPresent());
    }

    @Test
    public void shouldChangeStateProbateForCaseReview() {
        when(updateApplicationRule.isChangeNeeded(caseDataMock)).thenReturn(true);
        when(caseDataMock.getSolsWillType()).thenReturn(WILL_TYPE_PROBATE);

        Optional<String> newState = underTest.getChangedStateForCaseReview(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals(STATE_GRANT_TYPE_PROBATE, newState.get());
    }

    @Test
    public void shouldChangeStateIntestacyForCaseReview() {
        when(updateApplicationRule.isChangeNeeded(caseDataMock)).thenReturn(true);
        when(caseDataMock.getSolsWillType()).thenReturn(WILL_TYPE_INTESTACY);

        Optional<String> newState = underTest.getChangedStateForCaseReview(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals(STATE_GRANT_TYPE_INTESTACY, newState.get());
    }

    @Test
    public void shouldChangeStateAdmonForCaseReview() {
        when(updateApplicationRule.isChangeNeeded(caseDataMock)).thenReturn(true);
        when(caseDataMock.getSolsWillType()).thenReturn(WILL_TYPE_ADMON);

        Optional<String> newState = underTest.getChangedStateForCaseReview(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals(STATE_GRANT_TYPE_ADMON, newState.get());
    }

    @Test
    public void shouldChangeStateForGrantTypeProbate() {
        when(caseDataMock.getSolsWillType()).thenReturn(WILL_TYPE_PROBATE);

        Optional<String> newState = underTest.getChangedStateForGrantType(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals(STATE_GRANT_TYPE_PROBATE, newState.get());
    }

    @Test
    public void shouldChangeStateForGrantTypeIntestacy() {
        when(caseDataMock.getSolsWillType()).thenReturn(WILL_TYPE_INTESTACY);

        Optional<String> newState = underTest.getChangedStateForGrantType(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals(STATE_GRANT_TYPE_INTESTACY, newState.get());
    }

    @Test
    public void shouldChangeStateForGrantTypeAdmon() {
        when(caseDataMock.getSolsWillType()).thenReturn(WILL_TYPE_ADMON);

        Optional<String> newState = underTest.getChangedStateForGrantType(caseDataMock);

        assertTrue(newState.isPresent());
        assertEquals(STATE_GRANT_TYPE_ADMON, newState.get());
    }

    @Test
    public void shouldNOTChangeStateForReview() {
        when(updateApplicationRule.isChangeNeeded(caseDataMock)).thenReturn(false);

        Optional<String> newState = underTest.getChangedStateForCaseReview(caseDataMock);

        assertFalse(newState.isPresent());
    }

}
