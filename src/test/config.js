module.exports = {
    TestBackOfficeUrl: process.env.TEST_E2E_URL || 'http://localhost:3451',
    TestShowBrowserWindow: process.env.SHOW_BROWSER_WINDOW || false,
    TestRetryFeatures: process.env.RETRY_FEATURES || 0,
    TestRetryScenarios: process.env.RETRY_SCENARIOS || 3,
    TestPathToRun: process.env.BO_E2E_TEST_PATH_TO_RUN || './paths/**/*.js',
    TestOutputDir: process.env.E2E_OUTPUT_DIR || './output',
    TestDocumentToUpload: 'uploadDocuments/test_file_for_document_upload.png',
    TestTimeToWaitForText: parseInt(process.env.BO_E2E_TEST_TIME_TO_WAIT_FOR_TEXT || 60),
    TestActionWaitTime: parseInt(process.env.BO_E2E_TEST_ACTION_WAIT_TIME || '1500'),
    TestAutoDelayEnabled: process.env.BO_E2E_AUTO_DELAY_ENABLED === 'false' ? false : true,
    TestEnvUser: process.env.CW_USER_EMAIL || 'ProbateSolCW1@gmail.com',
    TestEnvPassword: process.env.CW_USER_PASSWORD || 'Pa55word11',
    TestEnvProfUser: process.env.PROF_USER_EMAIL || 'ProbateSolicitor1@gmail.com',
    TestEnvProfPassword: process.env.CW_USER_PASSWORD || 'Pa55word11',

};
