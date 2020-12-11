package uk.gov.hmcts.probate.htmlRendering;

import uk.gov.hmcts.probate.model.htmlTemplate.DetailsComponentHtmlTemplate;

import static java.lang.String.format;

public class DetailsComponentRenderer {
    private DetailsComponentRenderer() {
        throw new IllegalStateException("Utility class");
    }

    public static String renderByReplace(String title, String detailsText) {
        return DetailsComponentHtmlTemplate.DETAILS_TEMPLATE
                .replaceFirst("<title/>", title)
                .replaceFirst("<detailsText/>", detailsText);
    }
}
