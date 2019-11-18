package uk.gov.hmcts.probate.service.exceptionrecord.mapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.model.exceptionrecord.ExceptionRecordOCRFields;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class ExceptionRecordGrantOfRepresentationMapperAfterMappingTest {

    @Autowired
    private ExceptionRecordGrantOfRepresentationMapper exceptionRecordGrantOfRepresentationMapper;

    @Autowired
    OCRFieldAddressMapper ocrFieldAddressMapper;

    @Autowired
    OCRFieldAdditionalExecutorsApplyingMapper ocrFieldAdditionalExecutorsApplyingMapper;

    @Autowired
    OCRFieldAdditionalExecutorsNotApplyingMapper ocrFieldAdditionalExecutorsNotApplyingMapper;

    @Autowired
    OCRFieldDefaultLocalDateFieldMapper ocrFieldDefaultLocalDateFieldMapper;

    @Autowired
    OCRFieldYesOrNoMapper ocrFieldYesOrNoMapper;

    @Autowired
    OCRFieldMartialStatusMapper ocrFieldMartialStatusMapper;

    @Autowired
    OCRFieldAdoptiveRelativesMapper ocrFieldAdoptiveRelativesMapper;

    @Autowired
    OCRFieldIhtMoneyMapper ocrFieldIhtMoneyMapper;

    @Autowired
    OCRFieldRelationshipMapper ocrFieldRelationshipMapper;

    @Autowired
    OCRFieldNumberMapper ocrFieldNumberMapper;

    @Autowired
    OCRFieldPaymentMethodMapper ocrFieldPaymentMethodMapper;

    private static GrantOfRepresentationData caseData;

    @Configuration
    public static class Config {

        @Bean
        public OCRFieldAddressMapper ocrFieldAddressMapper() {
            return new OCRFieldAddressMapper();
        }

        @Bean
        public OCRFieldAdditionalExecutorsApplyingMapper ocrFieldAdditionalExecutorsApplyingMapper() {
            return new OCRFieldAdditionalExecutorsApplyingMapper();
        }

        @Bean
        public OCRFieldAdditionalExecutorsNotApplyingMapper ocrFieldAdditionalExecutorsNotApplyingMapper() {
            return new OCRFieldAdditionalExecutorsNotApplyingMapper();
        }

        @Bean
        public OCRFieldDefaultLocalDateFieldMapper ocrFieldDefaultLocalDateFieldMapper() {
            return new OCRFieldDefaultLocalDateFieldMapper();
        }

        @Bean
        public OCRFieldYesOrNoMapper ocrFieldYesOrNoMapper() {
            return new OCRFieldYesOrNoMapper();
        }

        @Bean
        public OCRFieldMartialStatusMapper ocrFieldMartialStatusMapper() {
            return new OCRFieldMartialStatusMapper();
        }

        @Bean
        public OCRFieldAdoptiveRelativesMapper ocrFieldAdoptiveRelativesMapper() {
            return new OCRFieldAdoptiveRelativesMapper();
        }

        @Bean
        public OCRFieldIhtMoneyMapper ocrFieldIhtMoneyMapper() {
            return new OCRFieldIhtMoneyMapper();
        }

        @Bean
        public OCRFieldRelationshipMapper ocrFieldRelationshipMapper() {
            return new OCRFieldRelationshipMapper();
        }

        @Bean
        public OCRFieldPaymentMethodMapper ocrFieldNumberMapper() {
            return new OCRFieldPaymentMethodMapper();
        }

        @Bean
        public OCRFieldNumberMapper ocrFieldPaymentMethodMapper() {
            return new OCRFieldNumberMapper();
        }

        @Bean
        public ExceptionRecordGrantOfRepresentationMapper mainMapper() {
            return Mappers.getMapper(ExceptionRecordGrantOfRepresentationMapper.class);
        }
    }

    @Test
    public void testSetDomicilityIHTCertTrue() {
        ExceptionRecordOCRFields ocrFields = ExceptionRecordOCRFields.builder()
                .domicilityEntrustingDocument(null)
                .domicilitySuccessionIHTCert("true")
                .build();
        GrantOfRepresentationData response = exceptionRecordGrantOfRepresentationMapper.toCcdData(ocrFields);
        assertTrue(response.getDomicilityIHTCert());
    }

    @Test
    public void testSetDomicilityIHTCertNullIfNotTrue() {
        ExceptionRecordOCRFields ocrFields = ExceptionRecordOCRFields.builder()
                .domicilityEntrustingDocument("false")
                .domicilitySuccessionIHTCert(null)
                .build();
        GrantOfRepresentationData response = exceptionRecordGrantOfRepresentationMapper.toCcdData(ocrFields);
        assertNull(response.getDomicilityIHTCert());
    }

    @Test
    public void setDerivedFamilyBooleansTrue() {
        ExceptionRecordOCRFields ocrFields = ExceptionRecordOCRFields.builder()
                .childrenUnderEighteenSurvived("1")
                .childrenOverEighteenSurvived(null)
                .childrenDiedUnderEighteen("")
                .childrenDiedUnderEighteen("3")
                .grandChildrenSurvivedUnderEighteen("2")
                .grandChildrenSurvivedOverEighteen("0")
                .parentsExistUnderEighteenSurvived("2")
                .parentsExistOverEighteenSurvived("0")
                .wholeBloodSiblingsSurvivedUnderEighteen("1")
                .wholeBloodSiblingsSurvivedOverEighteen("0")
                .wholeBloodSiblingsDiedUnderEighteen("1")
                .wholeBloodSiblingsDiedOverEighteen(null)
                .wholeBloodNeicesAndNephewsUnderEighteen("1")
                .wholeBloodNeicesAndNephewsOverEighteen("")
                .halfBloodSiblingsSurvivedUnderEighteen("3")
                .halfBloodSiblingsSurvivedOverEighteen(null)
                .halfBloodSiblingsDiedUnderEighteen("1")
                .halfBloodSiblingsDiedOverEighteen("0")
                .halfBloodNeicesAndNephewsUnderEighteen("2")
                .halfBloodNeicesAndNephewsOverEighteen(null)
                .grandparentsDiedUnderEighteen("1")
                .grandparentsDiedOverEighteen("")
                .wholeBloodUnclesAndAuntsSurvivedUnderEighteen("1")
                .wholeBloodUnclesAndAuntsSurvivedOverEighteen(null)
                .wholeBloodUnclesAndAuntsDiedUnderEighteen("2")
                .wholeBloodUnclesAndAuntsDiedOverEighteen("0")
                .wholeBloodCousinsSurvivedUnderEighteen("1")
                .wholeBloodCousinsSurvivedOverEighteen(null)
                .halfBloodUnclesAndAuntsSurvivedUnderEighteen("1")
                .halfBloodUnclesAndAuntsSurvivedOverEighteen("0")
                .halfBloodUnclesAndAuntsDiedUnderEighteen("1")
                .halfBloodUnclesAndAuntsDiedOverEighteen(null)
                .halfBloodCousinsSurvivedUnderEighteen("2")
                .halfBloodCousinsSurvivedOverEighteen("")
                .build();
        GrantOfRepresentationData response = exceptionRecordGrantOfRepresentationMapper.toCcdData(ocrFields);
        assertTrue(response.getChildrenSurvived());
        assertTrue(response.getChildrenDied());
        assertTrue(response.getGrandChildrenSurvived());
        assertTrue(response.getParentsExistSurvived());
        assertTrue(response.getWholeBloodSiblingsSurvived());
        assertTrue(response.getWholeBloodSiblingsDied());
        assertTrue(response.getWholeBloodNeicesAndNephews());
        assertTrue(response.getHalfBloodSiblingsSurvived());
        assertTrue(response.getHalfBloodSiblingsDied());
        assertTrue(response.getHalfBloodNeicesAndNephews());
        assertTrue(response.getGrandparentsDied());
        assertTrue(response.getWholeBloodUnclesAndAuntsSurvived());
        assertTrue(response.getWholeBloodUnclesAndAuntsDied());
        assertTrue(response.getWholeBloodCousinsSurvived());
        assertTrue(response.getHalfBloodUnclesAndAuntsSurvived());
        assertTrue(response.getHalfBloodUnclesAndAuntsDied());
        assertTrue(response.getHalfBloodCousinsSurvived());
    }

    @Test
    public void setDerivedFamilyBooleansFalse() {
        ExceptionRecordOCRFields ocrFields = ExceptionRecordOCRFields.builder()
                .childrenUnderEighteenSurvived("0")
                .childrenOverEighteenSurvived(null)
                .childrenDiedUnderEighteen("")
                .childrenDiedUnderEighteen("0")
                .grandChildrenSurvivedUnderEighteen("0")
                .grandChildrenSurvivedOverEighteen("0")
                .parentsExistUnderEighteenSurvived("0")
                .parentsExistOverEighteenSurvived("0")
                .wholeBloodSiblingsSurvivedUnderEighteen("0")
                .wholeBloodSiblingsSurvivedOverEighteen("0")
                .wholeBloodSiblingsDiedUnderEighteen("0")
                .wholeBloodSiblingsDiedOverEighteen(null)
                .wholeBloodNeicesAndNephewsUnderEighteen("0")
                .wholeBloodNeicesAndNephewsOverEighteen("")
                .halfBloodSiblingsSurvivedUnderEighteen("0")
                .halfBloodSiblingsSurvivedOverEighteen(null)
                .halfBloodSiblingsDiedUnderEighteen("0")
                .halfBloodSiblingsDiedOverEighteen("0")
                .halfBloodNeicesAndNephewsUnderEighteen("0")
                .halfBloodNeicesAndNephewsOverEighteen(null)
                .grandparentsDiedUnderEighteen("0")
                .grandparentsDiedOverEighteen("")
                .wholeBloodUnclesAndAuntsSurvivedUnderEighteen("0")
                .wholeBloodUnclesAndAuntsSurvivedOverEighteen(null)
                .wholeBloodUnclesAndAuntsDiedUnderEighteen("0")
                .wholeBloodUnclesAndAuntsDiedOverEighteen("0")
                .wholeBloodCousinsSurvivedUnderEighteen("0")
                .wholeBloodCousinsSurvivedOverEighteen(null)
                .halfBloodUnclesAndAuntsSurvivedUnderEighteen("0")
                .halfBloodUnclesAndAuntsSurvivedOverEighteen("0")
                .halfBloodUnclesAndAuntsDiedUnderEighteen("0")
                .halfBloodUnclesAndAuntsDiedOverEighteen(null)
                .halfBloodCousinsSurvivedUnderEighteen("0")
                .halfBloodCousinsSurvivedOverEighteen("")
                .build();
        GrantOfRepresentationData response = exceptionRecordGrantOfRepresentationMapper.toCcdData(ocrFields);
        assertFalse(response.getChildrenSurvived());
        assertFalse(response.getChildrenDied());
        assertFalse(response.getGrandChildrenSurvived());
        assertFalse(response.getParentsExistSurvived());
        assertFalse(response.getWholeBloodSiblingsSurvived());
        assertFalse(response.getWholeBloodSiblingsDied());
        assertFalse(response.getWholeBloodNeicesAndNephews());
        assertFalse(response.getHalfBloodSiblingsSurvived());
        assertFalse(response.getHalfBloodSiblingsDied());
        assertFalse(response.getHalfBloodNeicesAndNephews());
        assertFalse(response.getGrandparentsDied());
        assertFalse(response.getWholeBloodUnclesAndAuntsSurvived());
        assertFalse(response.getWholeBloodUnclesAndAuntsDied());
        assertFalse(response.getWholeBloodCousinsSurvived());
        assertFalse(response.getHalfBloodUnclesAndAuntsSurvived());
        assertFalse(response.getHalfBloodUnclesAndAuntsDied());
        assertFalse(response.getHalfBloodCousinsSurvived());
    }

    @Test
    public void testSetApplyingAsAnAttorneyBooleanTrue() {
        ExceptionRecordOCRFields ocrFields = ExceptionRecordOCRFields.builder()
                .attorneyOnBehalfOfName("Fred and Sons")
                .attorneyOnBehalfOfAddressLine1("12 Grren Park")
                .attorneyOnBehalfOfAddressTown("London")
                .attorneyOnBehalfOfAddressPostCode("NW1 1LO")
                .build();
        GrantOfRepresentationData response = exceptionRecordGrantOfRepresentationMapper.toCcdData(ocrFields);
        assertTrue(response.getApplyingAsAnAttorney());
    }

    @Test
    public void testSetApplyingAsAnAttorneyBooleanFalse() {
        ExceptionRecordOCRFields ocrFields = ExceptionRecordOCRFields.builder().build();
        GrantOfRepresentationData response = exceptionRecordGrantOfRepresentationMapper.toCcdData(ocrFields);
        assertFalse(response.getApplyingAsAnAttorney());
    }
}
