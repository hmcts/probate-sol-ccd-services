'use strict';

const Helper = codecept_helper;
const testConfig = require('src/test/config.js');

class WebDriverHelper extends Helper {

    async clickBrowserBackButton() {
        const browser = this.helpers.WebDriverIO.browser;

        await browser.back();
    }

    /**
     * waits for naigation to complete, optionally provide a button to click
     * to start the navigation
     * @param {object} locator - a locator to a button to click, or null 
     * @returns {object} - Promise
     */
    async waitForNavigationToComplete(locator) {
        const helper = this.helpers.WebDriverIO;

        if (locator) {
            // must be a button to click
            await helper.click(locator);
        }

        // so for ie11 / selenium webdriver this isn't that reliable,
        // is best combined with JSWaits amOnLoadedPage in next page
        await helper.wait(3);
    }

    async downloadPdfIfNotIE11(pdfLink) {
        const browserName = this.helpers.WebDriverIO.config.browser;
        const helper = this.helpers.WebDriverIO;

        if (browserName !== 'internet explorer') {
            await helper.click(pdfLink);
        }
    }

    async uploadDocumentIfNotMicrosoftEdge() {
        const browserName = this.helpers.WebDriverIO.config.browser;
        const helper = this.helpers.WebDriverIO;

        if (browserName !== 'MicrosoftEdge') {
            await helper.waitForElement('.dz-hidden-input', testConfig.TestWaitForElementToAppear * testConfig.TestOneMilliSecond);
            await helper.attachFile('.dz-hidden-input', testConfig.TestDocumentToUpload);
            await helper.waitForEnabled('#button', testConfig.TestWaitForElementToAppear);
        }
    }
}
module.exports = WebDriverHelper;
