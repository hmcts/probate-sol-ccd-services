'use strict';
const commonConfig = require('src/test/end-to-end/pages/common/commonConfig');

// deceased details part 3
module.exports = async function () {
    const I = this;
    // if this hangs, then case progress tab has not been generated / not been generated correctly and test fails
    await I.waitForElement('#willDispose-Yes'); 
    await I.click('#willDispose-Yes');
    await I.click('#englishWill-Yes');
    await I.click('#appointExec-Yes');    
    await I.waitForNavigationToComplete(commonConfig.continueButton);    
};
