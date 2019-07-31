package uk.gov.hmcts.probate.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.probate.insights.AppInsights;
import uk.gov.hmcts.probate.model.ocr.OCRField;
import uk.gov.hmcts.probate.model.ocr.OCRRequest;
import uk.gov.hmcts.probate.service.ocr.OCRMapper;
import uk.gov.hmcts.probate.service.ocr.OCRToCCDMandatoryField;
import uk.gov.hmcts.probate.util.TestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BulkScanningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AppInsights appInsights;

    @Autowired
    private TestUtils testUtils;

    @Mock
    private OCRMapper ocrMapper;

    @Mock
    private OCRToCCDMandatoryField ocrToCCDMandatoryField;

    private static final String EXPECTED_ATTACH_SCAN_DOC_FROM_UI_ERROR =
            "You cannot attach a document to a case using this event. Please use Upload Documents instead.";

    private static final String BASIC_CASE_PAYLOAD = "{\"case_details\": {\"id\": 1528365719153338} }";
    private String ocrPayload;
    private List<OCRField> ocrFields = new ArrayList<>();
    private List<String> warnings;

    @Before
    public void setUp() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ocrPayload = testUtils.getStringFromFile("expectedOCRData.json");
        OCRField field1 = OCRField.builder()
                .name("deceasedForenames")
                .value("John")
                .description("Deceased forename").build();
        ocrFields.add(field1);
        when(ocrMapper.ocrMapper(any())).thenReturn(ocrFields);
        when(ocrToCCDMandatoryField.ocrToCCDMandatoryFields(ocrFields)).thenReturn(EMPTY_LIST);
    }

    @Test
    public void displayAttachScanDocErrorIfUsedFromUI() throws Exception {
        mockMvc.perform(post("/bulk-scanning/attach-scanned-docs-error")
                .content(BASIC_CASE_PAYLOAD)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_ATTACH_SCAN_DOC_FROM_UI_ERROR)));
    }

    @Test
    public void test() throws Exception { //doesnt work yet for some reason
        mockMvc.perform(post("/bulk-scanning/PA1P/validate-ocr-data")
                .content(ocrPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SUCCESS")));
    }
}