'use strict';
const testConfig = require('src/test/config.js');

// Solicitor - navigate back to case
module.exports = async function (caseRef) {
    const I = this;
    
    // if this hangs, then case progress tab has not been generated / not been generated correctly and test fails    
    //Make sure case stopped is available in dropdown
    await I.waitForText('Case stopped', 2, {css: '#wb-case-state option'});

    // while we're here, make sure escalated to registrar is vailable as an option to choose from
    await I.waitForText('Registrar escalation', 2, {css: '#wb-case-state option'});

    await I.navigateToPage(`${testConfig.TestBackOfficeUrl}/v2/case/${await I.replaceAll(caseRef,'-','')}`);

};
