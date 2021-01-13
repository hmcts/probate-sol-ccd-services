'use strict';

const testConfig = require('src/test/config.js');

module.exports = async function (endState) {
    const I = this;
    await I.waitForText('Event History');
    await I.click('Event History');
    await I.waitForText(endState, testConfig.TestTimeToWaitForText);
};
