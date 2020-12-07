module.exports = {
    TestBackOfficeUrl: process.env.TEST_E2E_URL || 'http://localhost:3451',
    TestShowBrowserWindow: process.env.SHOW_BROWSER_WINDOW || false,
    TestRetryFeatures: process.env.RETRY_FEATURES || 0,
    TestRetryScenarios: process.env.RETRY_SCENARIOS || 2,
    TestPathToRun: process.env.E2E_TEST_PATH  || './paths/**/*.js',
    TestOutputDir: process.env.E2E_OUTPUT_DIR || './functional-output',
    TestDocumentToUpload: 'uploadDocuments/test_file_for_document_upload.png',
    TestTimeToWaitForText: parseInt(process.env.E2E_TEST_TIME_TO_WAIT_FOR_TEXT || 60),
    TestAutoDelayEnabled: process.env.E2E_AUTO_DELAY_ENABLED === 'false' ? false : true,
    TestEnvUser: process.env.CW_USER_EMAIL || 'ProbateSolCW1@gmail.com',
    TestEnvPassword: process.env.CW_USER_PASSWORD || 'Pa55word11',
    TestEnvProfUser: process.env.PROF_USER_EMAIL || 'ProbateSolicitor1@gmail.com',
    TestEnvProfPassword: process.env.CW_USER_PASSWORD || 'Pa55word11',
};
