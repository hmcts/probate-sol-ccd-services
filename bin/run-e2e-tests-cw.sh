export TEST_E2E_URL=${TEST_CCD_E2E_URL}
export E2E_TEST_PATH="./paths/caseworker/**/*.js"
export TEST_USER_EMAIL=${CW_USER_EMAIL}
export TEST_USER_PASSWORD=${CW_USER_PASSWORD}
export TESTS_FOR_XUI_SERVICE='true'

yarn test:functional