'use strict';

const testConfig = require('src/test/config.js');
const commonConfig = require('src/test/end-to-end/pages/common/commonConfig');

module.exports = async function () {
    const I = this;

    await I.waitForText('Check your answers', testConfig.TestTimeToWaitForText);
    await I.runAccessibilityTest();
    await I.wait(0.5);

    await I.waitForNavigationToComplete(commonConfig.continueButton);
};
