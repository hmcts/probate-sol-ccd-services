'use strict';

const testConfig = require('src/test/config');
const createWillLodgementConfig = require('./createWillLodgementConfig');
const commonConfig = require('src/test/end-to-end/pages/common/commonConfig');

module.exports = async function (crud, unique_deceased_user) {

    const I = this;

    if (crud === 'create') {
        await I.waitForText(createWillLodgementConfig.page2_waitForText, testConfig.TestTimeToWaitForText);

        await I.fillField('#deceasedForenames', createWillLodgementConfig.page2_forenames + '_' + unique_deceased_user);
        await I.fillField('#deceasedSurname', createWillLodgementConfig.page2_surname + '_' + unique_deceased_user);

        await I.selectOption('#deceasedGender', createWillLodgementConfig.page2_gender);

        await I.fillField('#deceasedDateOfBirth-day', createWillLodgementConfig.page2_dateOfBirth_day);
        await I.fillField('#deceasedDateOfBirth-month', createWillLodgementConfig.page2_dateOfBirth_month);
        await I.fillField('#deceasedDateOfBirth-year', createWillLodgementConfig.page2_dateOfBirth_year);

        await I.fillField('#deceasedDateOfDeath-day', createWillLodgementConfig.page2_dateOfDeath_day);
        await I.fillField('#deceasedDateOfDeath-month', createWillLodgementConfig.page2_dateOfDeath_month);
        await I.fillField('#deceasedDateOfDeath-year', createWillLodgementConfig.page2_dateOfDeath_year);

        await I.fillField('#deceasedTypeOfDeath', createWillLodgementConfig.page2_typeOfDeath);

        await I.click(`#deceasedAnyOtherNames-${createWillLodgementConfig.page2_hasAliasYes}`);

        /* eslint-disable no-await-in-loop */
        for (let i=0; i < Object.keys(createWillLodgementConfig).length; i++) {
            const value = Object.keys(createWillLodgementConfig)[i];
            if (value.includes('page2_alias_')) {
                await I.click(createWillLodgementConfig.page2_addAliasButton);
                await I.fillField(`#deceasedFullAliasNameList_${i}_FullAliasName`, createWillLodgementConfig[value]);
            }
        }

        await I.fillField('#deceasedFullAliasNameList_0_FullAliasName', createWillLodgementConfig.page2_alias_1 + '_' + unique_deceased_user);

        await I.click(createWillLodgementConfig.UKpostcodeLink);
        await I.fillField('#deceasedAddress_AddressLine1', createWillLodgementConfig.address_line1);
        await I.fillField('#deceasedAddress_AddressLine2', createWillLodgementConfig.address_line2);
        await I.fillField('#deceasedAddress_AddressLine3', createWillLodgementConfig.address_line3);
        await I.fillField('#deceasedAddress_PostTown', createWillLodgementConfig.address_town);
        await I.fillField('#deceasedAddress_County', createWillLodgementConfig.address_county);
        await I.fillField('#deceasedAddress_PostCode', createWillLodgementConfig.address_postcode);
        await I.fillField('#deceasedAddress_Country', createWillLodgementConfig.address_country);
        await I.fillField('#deceasedEmailAddress', createWillLodgementConfig.page2_email);
    }

    if (crud === 'update') {
        await I.waitForText(createWillLodgementConfig.page2_amend_waitForText, testConfig.TestTimeToWaitForText);

        await I.fillField('#deceasedForenames', createWillLodgementConfig.page2_forenames + '_' + unique_deceased_user + ' UPDATED' + unique_deceased_user);
        await I.fillField('#deceasedSurname', createWillLodgementConfig.page2_surname + '_' + unique_deceased_user + ' UPDATED' + unique_deceased_user);
        await I.fillField('#deceasedFullAliasNameList_0_FullAliasName', createWillLodgementConfig.page2_alias_1 + '_' + unique_deceased_user + ' UPDATED' + unique_deceased_user);

        await I.fillField('#deceasedDateOfDeath-day', createWillLodgementConfig.page2_dateOfDeath_day_update);
        await I.fillField('#deceasedDateOfDeath-month', createWillLodgementConfig.page2_dateOfDeath_month_update);
        await I.fillField('#deceasedDateOfDeath-year', createWillLodgementConfig.page2_dateOfDeath_year_update);
        await I.fillField('#deceasedDateOfBirth-day', createWillLodgementConfig.page2_dateOfBirth_day_update);
        await I.fillField('#deceasedDateOfBirth-month', createWillLodgementConfig.page2_dateOfBirth_month_update);
        await I.fillField('#deceasedDateOfBirth-year', createWillLodgementConfig.page2_dateOfBirth_year_update);
    }

    if (crud === 'update2orig') {

        // "reverting" update back to defaults - to enable case-match with matching case
        await I.waitForNavigationToComplete(commonConfig.continueButton);
        await I.waitForText(createWillLodgementConfig.page2_amend_waitForText, testConfig.TestTimeToWaitForText);

        await I.fillField('#deceasedDateOfDeath-day', createWillLodgementConfig.page2_dateOfDeath_day);
        await I.fillField('#deceasedDateOfDeath-month', createWillLodgementConfig.page2_dateOfDeath_month);
        await I.fillField('#deceasedDateOfDeath-year', createWillLodgementConfig.page2_dateOfDeath_year);
        await I.fillField('#deceasedDateOfBirth-day', createWillLodgementConfig.page2_dateOfBirth_day);
        await I.fillField('#deceasedDateOfBirth-month', createWillLodgementConfig.page2_dateOfBirth_month);
        await I.fillField('#deceasedDateOfBirth-year', createWillLodgementConfig.page2_dateOfBirth_year);
        await I.waitForNavigationToComplete(commonConfig.continueButton);
    }

    await I.waitForNavigationToComplete(commonConfig.continueButton);
};
