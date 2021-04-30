'use strict';

const testConfig = require('src/test/config');
const createGrantOfProbateConfig = require('./createGrantOfProbateConfig');
const commonConfig = require('src/test/end-to-end/pages/common/commonConfig');

module.exports = async function (crud) {

    const I = this;

    if (crud === 'create') {
        await I.waitForText(createGrantOfProbateConfig.page9_waitForText, testConfig.TestTimeToWaitForText);
        await I.click(`#deceasedDomicileInEngWales-${createGrantOfProbateConfig.page9_deceasedDomicileInEngWalesYes}`);
        await I.fillField('#domicilityCountry', createGrantOfProbateConfig.page9_domicilityCountry);
        await I.click('#ukEstate > div > button:nth-child(2)');
        if (!testConfig.TestAutoDelayEnabled) {
            await I.wait(testConfig.ManualDelayMedium);
        }
        await I.fillField('#ukEstate_0_item', createGrantOfProbateConfig.page9_ukEstate_0_item);
        await I.fillField('#ukEstate_0_value', createGrantOfProbateConfig.page9_ukEstate_0_value);
        await I.click(`#domicilityIHTCert-${createGrantOfProbateConfig.page9_domicilityIHTCertYes}`);
    }

    if (crud === 'update') {
        await I.waitForText(createGrantOfProbateConfig.page9_amend_waitForText, testConfig.TestTimeToWaitForText);
        await I.selectOption('#selectionList', createGrantOfProbateConfig.page9_list1_update_option);
        await I.waitForNavigationToComplete(commonConfig.continueButton);

        const locator = {css: `#deceasedDomicileInEngWales-${createGrantOfProbateConfig.page9_deceasedDomicileInEngWalesNo}`};
        await I.waitForElement(locator);
        await I.click(locator);
        await I.fillField({css: '#domicilityCountry'}, createGrantOfProbateConfig.page9_domicilityCountry);
    }

    if (!testConfig.TestAutoDelayEnabled) {
        await I.wait(testConfig.ManualDelayMedium);
    }

    await I.waitForNavigationToComplete(commonConfig.continueButton);
};