package uk.gov.hmcts.probate.model.htmlTemplate;

public class CaseEscalatedToRegistrarHtmlTemplate {
    public static final String BASE_TEMPLATE = "<p>The case was escalated on <escalationDate>.</p>\n" +
        "<p>The case will be reviewed by the Registrar and you will be notified by email if we need any information from you to progress the case.</p>\n" +
        "<p>Only contact the CTSC staff if your case has been escalated for <numWeeks> weeks or more and you have not received any communication since then.</p>\n";

    private CaseEscalatedToRegistrarHtmlTemplate() {
        throw new IllegalStateException("Utility class");
    }
}