'use strict';

const Helper = codecept_helper;
const helperName = 'Puppeteer';

class PuppeteerHelper extends Helper {

    async clickBrowserBackButton() {
        const page = this.helpers[helperName].page;
        return await page.goBack();
    }

    async waitForNavigationToComplete(locator) {
        const page = this.helpers[helperName].page;

        const promises = [
            page.waitForNavigation({ timeout: 60000, waitUntil: ['domcontentloaded', 'networkidle0'] })  // The promise resolves after navigation has finished
        ];

        if (locator) {
            promises.push(page.click(locator));
        }

        await Promise.all(promises);
    }

    async navigateToPage(url) {
        await this.amOnPage(url);
        await this.waitForNavigationToComplete();
    }

    async downloadPdfIfNotIE11(pdfLink) {
        const helper = this.helpers[helperName];
        await helper.click(pdfLink);
    }

    async uploadDocumentIfNotMicrosoftEdge() {
        const helper = this.helpers[helperName];
        await helper.waitForElement('.dz-hidden-input', testConfig.TestWaitForElementToAppear * testConfig.TestOneMilliSecond);
        await helper.attachFile('.dz-hidden-input', testConfig.TestDocumentToUpload);
        await helper.waitForEnabled('#button', testConfig.TestWaitForElementToAppear);
    }    

    replaceAll(string, search, replace) {
        if (!string) {
            return null;
        }
        return string.split(search).join(replace);
    }

    htmlEquals(html1, html2) {
        if ((html1 && !html2) || (html2 && !html1)) {
            return false;
        }
        if (!html1 && !html2) {
            return true;
        }
        return this.replaceAll(this.replaceAll(this.replaceAll(html1, '-c16'), '-c17'), '-c18') ===
            this.replaceAll(this.replaceAll(this.replaceAll(html2, '-c16'), '-c17'), '-c18') ? true : false;
    }

    async performAsyncActionForElements(locator, actionFunc) {
        const elements = await this.helpers['Puppeteer']._locate(locator);
        if (!elements || elements.length === 0) {
            return;
        }
        for (let i = 0; i < elements.length; i++) {
            await actionFunc(el[i]);
        }
    }

    isArray(obj){
        return !!obj && obj.constructor === Array;
    }

    async getNumElements(locator) {
        const elements = await this.helpers['Puppeteer']._locate(locator);
        if (!elements) {
            return 0;
        }
        if (!isArray(elements)) {
            return 1;
        }
        return elements.length;
    }
}
module.exports = PuppeteerHelper;
