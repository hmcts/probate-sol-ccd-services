'use strict';

const testConfig = require('src/test/config');
const createCaveatConfig = require('./createCaveatConfig');
const commonConfig = require('src/test/end-to-end/pages/common/commonConfig');

module.exports = async function (crud, unique_deceased_user) {

    const I = this;

    if (crud === 'create') {
        await I.waitForText(createCaveatConfig.page2_waitForText, testConfig.TestTimeToWaitForText);

        await I.fillField('#deceasedForenames', createCaveatConfig.page2_forenames+unique_deceased_user);
        await I.fillField('#deceasedSurname', createCaveatConfig.page2_surname+unique_deceased_user);

        await I.fillField('#deceasedDateOfDeath-day', createCaveatConfig.page2_dateOfDeath_day);
        await I.fillField('#deceasedDateOfDeath-month', createCaveatConfig.page2_dateOfDeath_month);
        await I.fillField('#deceasedDateOfDeath-year', createCaveatConfig.page2_dateOfDeath_year);

        await I.fillField('#deceasedDateOfBirth-day', createCaveatConfig.page2_dateOfBirth_day);
        await I.fillField('#deceasedDateOfBirth-month', createCaveatConfig.page2_dateOfBirth_month);
        await I.fillField('#deceasedDateOfBirth-year', createCaveatConfig.page2_dateOfBirth_year);

        await I.click(`#deceasedAnyOtherNames-${createCaveatConfig.page2_hasAliasYes}`);

        let idx = 0;
        for (let i=0; i < Object.keys(createCaveatConfig).length; i++) {
            let value = Object.keys(createCaveatConfig)[i];
            if (value.includes('page2_alias_')) {
                await I.click(createCaveatConfig.page2_addAliasButton);
                let locator = {css: `#deceasedFullAliasNameList_${idx++}_FullAliasName`};
                // deceasedFullAliasNameList_0_FullAliasName
                await I.waitForElement(locator, testConfig.TestTimeToWaitForText);
                await I.fillField(locator, createCaveatConfig[value]);
            }
        }

        await I.click(createCaveatConfig.UKpostcodeLink);
        await I.fillField('#deceasedAddress_AddressLine1', createCaveatConfig.address_line1);
        await I.fillField('#deceasedAddress_AddressLine2', createCaveatConfig.address_line2);
        await I.fillField('#deceasedAddress_AddressLine3', createCaveatConfig.address_line3);
        await I.fillField('#deceasedAddress_PostTown', createCaveatConfig.address_town);
        await I.fillField('#deceasedAddress_County', createCaveatConfig.address_county);
        await I.fillField('#deceasedAddress_PostCode', createCaveatConfig.address_postcode);
        await I.fillField('#deceasedAddress_Country', createCaveatConfig.address_country);
    }

    if (crud === 'update') {
        await I.waitForText(createCaveatConfig.page2_amend_waitForText, testConfig.TestTimeToWaitForText);
        await I.fillField('#deceasedForenames', createCaveatConfig.page2_forenames_update+unique_deceased_user);
        await I.fillField('#deceasedSurname', createCaveatConfig.page2_surname_update+unique_deceased_user);
        await I.fillField('#deceasedDateOfBirth-day', createCaveatConfig.page2_dateOfBirth_day_update);
        await I.fillField('#deceasedDateOfBirth-month', createCaveatConfig.page2_dateOfBirth_month_update);
        await I.fillField('#deceasedDateOfBirth-year', createCaveatConfig.page2_dateOfBirth_year_update);
    }

    await I.waitForNavigationToComplete(commonConfig.continueButton);
};
