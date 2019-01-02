INSERT INTO STANDING_SEARCHES_FLAT
(
ID, SS_NUMBER, PROBATE_NUMBER, PROBATE_VERSION, DECEASED_ID, DECEASED_FORENAMES, DECEASED_SURNAME, DATE_OF_BIRTH, DATE_OF_DEATH1, CCD_CASE_NO,
DECEASED_ADDRESS, APPLICANT_ADDRESS,
REGISTRY_REG_LOCATION_CODE, SS_APPLICANT_FORENAME, SS_APPLICANT_SURNAME, SS_APPLICANT_HONOURS, SS_APPLICANT_TITLE, SS_DATE_LAST_EXTENDED, SS_DATE_OF_ENTRY, SS_DATE_OF_EXPIRY, SS_WITHDRAWN_DATE, STANDING_SEARCH_TEXT, DNM_IND, ALIAS_NAMES
)
VALUES (
999, 'SS-01','PRO-01', 1, 1, 'DeadFN1 DeadFN2', 'DeadSN', '01/01/1900', '01/01/2018', '1111222233334444',
'DeadAddL1, DeadAddL2, DeadAddL3, DeadCity, DeadCountry, DeadPC','AppAddL1, AppAddL2, AppAddL3, AppCity, AppCountry, AppPC',
1, 'SSFN', 'SSSN', 'Sir', 'Mr','01/01/2018', '01/01/2018', '01/01/2018', '01/01/2018', 'SS-Text', 'Y', 'SSAN1 SSAN2 SSAN3'
);
