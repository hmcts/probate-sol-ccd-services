package uk.gov.hmcts.probate.service.docmosis.assembler;

import org.junit.Test;
import uk.gov.hmcts.probate.model.ccd.raw.DynamicList;
import uk.gov.hmcts.probate.model.ccd.raw.DynamicListItem;
import uk.gov.hmcts.probate.model.ccd.raw.ParagraphDetail;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.probate.model.Constants.YES;

public class AssemblerBaseTest {

    private AssemblerBase assemblerBase = new AssemblerBase();

    @Test
    public void testCreate2ListItems() {
        String item1Code = "codeOne";
        String item1Label = "labelOne";
        String item2Code = "codeTwo";
        String item2Label = "labelTwo";

        List<DynamicListItem> response = assemblerBase.create2ListItems(item1Code, item1Label, item2Code, item2Label);
        assertEquals(response.get(0).getCode(), item1Code);
        assertEquals(response.get(0).getLabel(), item1Label);
        assertEquals(response.get(1).getCode(), item2Code);
        assertEquals(response.get(1).getLabel(), item2Label);
    }

    @Test
    public void testGetStaticParagraphsDetails() {

        List<ParagraphDetail> response = assemblerBase.getStaticParagraphDetails(ParagraphCode.Caseworker);
        assertEquals(response.get(0).getCode(),
                ParagraphField.valueOf(ParagraphField.CASEWORKER.toString()).getFieldCode());
        assertEquals(response.get(0).getEnableType().name(), "Static");
        assertEquals(response.get(0).getLabel(),
                ParagraphField.valueOf(ParagraphField.CASEWORKER.toString()).getFieldCode());
        assertEquals(response.get(0).getTemplateName(), null);
    }

    @Test
    public void testGetTextParagraphsDetails() {

        List<ParagraphDetail> response = assemblerBase.getTextParagraphDetails(ParagraphCode.EntExecNoAcc);
        assertEquals(response.get(0).getEnableType().name(), "Text");
        assertEquals(response.get(0).getLabel(), "Executor not accounted for");
        assertEquals(response.get(0).getCode(), "EntExecNoAcc");
        assertEquals(response.get(0).getTemplateName(), "FL-PRB-GNO-ENG-00123.docx");
    }

    @Test
    public void testGetTextAreaParagraphsDetails() {

        List<ParagraphDetail> response = assemblerBase.getTextAreaParagraphDetails(ParagraphCode.FreeText);
        assertEquals(response.get(0).getEnableType().name(), "TextArea");
        assertEquals(response.get(0).getLabel(), "Free Text");
        assertEquals(response.get(0).getCode(), "FreeText");
        assertEquals(response.get(0).getTemplateName(), null);
    }

    @Test
    public void testGetTextParagraphDetailWithDefaultValue() {

        List<String> textValues = new ArrayList<>();
        textValues.add("test value one");
        List<ParagraphDetail> response = assemblerBase.getTextParagraphDetailWithDefaultValue(ParagraphCode.IHT421Await, textValues);
        assertEquals(response.get(0).getEnableType().name(), "Text");
        assertEquals(response.get(0).getLabel(), "Awaiting IHT421");
        assertEquals(response.get(0).getTextValue(), "test value one");
        assertEquals(response.get(0).getCode(), "IHT421Await");
        assertEquals(response.get(0).getTemplateName(), "FL-PRB-GNO-ENG-00125.docx");
    }

    @Test
    public void testCreateDynamicListParagraphDetail() {
        DynamicListItem dynamicListItem = DynamicListItem.builder().code("IHT421Await").label("Awaiting IHT421").build();

        List<DynamicListItem> dynamicList = new ArrayList<DynamicListItem>();
        dynamicList.add(dynamicListItem);

        List<List<DynamicListItem>> listItems = new ArrayList<List<DynamicListItem>>();
        listItems.add(dynamicList);

        DynamicList dynamicList1 = DynamicList.builder().listItems(listItems.get(0)).value(DynamicListItem.builder().build()).build();

        List<ParagraphDetail> response = assemblerBase.createDynamicListParagraphDetail(ParagraphCode.IHT421Await, listItems);
        assertEquals(response.get(0).getEnableType().name(), "List");
        assertEquals(response.get(0).getDynamicList(), dynamicList1);
        assertEquals(response.get(0).getLabel(), "Awaiting IHT421");
        assertEquals(response.get(0).getCode(), "IHT421Await");
    }
}
