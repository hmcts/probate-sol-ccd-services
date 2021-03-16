'use strict';

const testConfig = require('src/test/config.js');
const createCaseConfig = require('./createCaseConfig');

module.exports = async function (jurisdiction, caseType, event) {

    const I = this;
    await I.waitForText(createCaseConfig.waitForText, testConfig.TestTimeToWaitForText || 60);
    //In saucelabs this page is not able to load so waiting for more time
    if (testConfig.TestForCrossBrowser) {
        await I.wait(5);
    }
    await I.waitForVisible({css: '#cc-jurisdiction'}, testConfig.TestTimeToWaitForText || 60);
    /* eslint-disable no-console */
    console.log(jurisdiction);
    /* eslint-disable no-console */
    await I.retry(5).selectOption('#cc-jurisdiction', jurisdiction);
    await I.waitForVisible({css: '#cc-case-type'}, testConfig.TestTimeToWaitForText || 60);
    await I.retry(5).selectOption('#cc-case-type', caseType);
    await I.waitForVisible({css: '#cc-event'}, testConfig.TestTimeToWaitForText || 60);
    await I.retry(5).selectOption('#cc-event', event);

    await I.waitForVisible(createCaseConfig.startButton, testConfig.TestTimeToWaitForText || 60);
    await I.waitForNavigationToComplete(createCaseConfig.startButton);
};
