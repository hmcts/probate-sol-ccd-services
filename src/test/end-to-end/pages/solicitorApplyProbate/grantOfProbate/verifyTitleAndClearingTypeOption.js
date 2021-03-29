'use strict';

const assert = require('assert');
const testConfig = require('src/test/config.js');

module.exports = async function (optName) {
    const I = this;

    const optLocator = {css: `#titleAndClearingType-${optName}`};
    await I.waitForElement(optLocator);
    await I.click(optLocator);
    if (!testConfig.TestAutoDelayEnabled) {
        await I.wait(0.25);
    }
    const isNa = optName === 'TCTNoT';
    const isTrustOption = optName.startsWith('TCTTrustCorp');

    const isSuccessorFirm = optName === 'TCTPartSuccPowerRes' || optName === 'TCTSolePrinSucc' || optName === 'TCTPartSuccAllRenouncing';
    const allRenouncing = optName === 'TCTPartSuccAllRenouncing' || optName === 'TCTPartAllRenouncing';

    const nameOfFirmNamedInWillVisible = (await I.grabNumberOfVisibleElements ({css: '#nameOfFirmNamedInWill'})) > 0;
    const nameOfSucceededFirmVisible = (await I.grabNumberOfVisibleElements ({css: '#nameOfSucceededFirm'})) > 0;
    const morePartnersHoldingPowerReservedVisible = (await I.grabNumberOfVisibleElements ({css: '#morePartnersHoldingPowerReserved'})) > 0;
    const otherPartnersApplyingAsExecutorsVisible = (await I.grabNumberOfVisibleElements ({css: '#otherPartnersApplyingAsExecutors'})) > 0;

    assert (isNa || isTrustOption ? !nameOfFirmNamedInWillVisible : nameOfFirmNamedInWillVisible);
    assert (isNa || isTrustOption || !isSuccessorFirm ? !nameOfSucceededFirmVisible : nameOfSucceededFirmVisible);
    assert (isNa || isTrustOption ? !morePartnersHoldingPowerReservedVisible : morePartnersHoldingPowerReservedVisible);
    assert (isNa || allRenouncing ? !otherPartnersApplyingAsExecutorsVisible : otherPartnersApplyingAsExecutorsVisible);

    if (!isNa && !isTrustOption && isSuccessorFirm) {
        await I.waitForText('Name of firm named in will');
        await I.scrollTo('#nameOfFirmNamedInWill');
        await I.waitForClickable({css: '#otherPartnersApplyingAsExecutors button'});
    }
};
