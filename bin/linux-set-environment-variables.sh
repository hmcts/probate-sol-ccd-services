export CCD_CASEWORKER_AUTOTEST_EMAIL="auto.test.cnp@gmail.com"
export CCD_CASEWORKER_AUTOTEST_PASSWORD="Pa55word11"
export CCD_CASEWORKER_AUTOTEST_FE_PASSWORD="Pa55word11"
export CCD_IMPORT_AUTOTEST_EMAIL="auto.test.cnp@gmail.com"
export CCD_IMPORT_AUTOTEST_PASSWORD="Pa55word11"
export CCD_GW_SERVICE_NAME="ccd_gw"
export CCD_GW_SERVICE_SECRET="AAAAAAAAAAAAAAAC"
export CCD_PRIVATE_CASEWORKER_SOLICITOR_EMAIL="auto.test.cnp+solc@gmail.com"
export CCD_PRIVATE_CASEWORKER_SOLICITOR_PASSWORD="Pa55word11"
export CCD_PRIVATE_CROSS_CASE_TYPE_CASEWORKER_EMAIL="auto.test12.cnp+private@gmail.com"
export CCD_PRIVATE_CROSS_CASE_TYPE_CASEWORKER_PASSWORD="Pa55word11"
export CCD_PRIVATE_CROSS_CASE_TYPE_SOLICITOR_EMAIL="auto.test12.cnp+solc@gmail.com"
export CCD_PRIVATE_CROSS_CASE_TYPE_SOLICITOR_PASSWORD="Pa55word11"
export CCD_PRIVATE_CASEWORKER_EMAIL="auto.test.cnp+private@gmail.com"
export CCD_PRIVATE_CASEWORKER_PASSWORD="Pa55word11"
export CCD_RESTRICTED_CASEWORKER_EMAIL="auto.test.cnp+senior@gmail.com"
export CCD_RESTRICTED_CASEWORKER_PASSWORD="Pa55word11"
export CCD_RESTRICTED_CROSS_CASE_TYPE_CASEWORKER_EMAIL="auto.test12.cnp+senior@gmail.com"
export CCD_RESTRICTED_CROSS_CASE_TYPE_CASEWORKER_PASSWORD="Pa55word11"
export DATA_STORE_IDAM_KEY="AAAAAAAAAAAAAAAB"
export DEFINITION_STORE_HOST="http://localhost:4451"
export DEFINITION_STORE_IDAM_KEY="AAAAAAAAAAAAAAAA"
export ELASTIC_SEARCH_ENABLED="true"
export ELASTIC_SEARCH_HOST="localhost"
export ELASTIC_SEARCH_HOSTS="http://localhost:9200"
export ELASTIC_SEARCH_PORT="9200"
export ELASTIC_SEARCH_SCHEME="http"
export IDAM_OAUTH2_CLIENT_ID="ccd_gateway"
export IDAM_OAUTH2_CLIENT_SECRET="ccd_gateway_secret"
export IDAM_S2S_URL="http://localhost:4502"
export IDAM_URL="http://localhost:5000"
export IDAM_KEY_CCD_GATEWAY="AAAAAAAAAAAAAAAC"
export OAUTH2_CLIENT_ID="ccd_gateway"
export OAUTH2_CLIENT_CCD_GATEWAY="ccd_gateway_secret"
export USER="someuser"
export OAUTH2_CLIENT_SECRET="ccd_gateway_secret"
export OAUTH2_REDIRECT_URI="http://localhost:3451/oauth2redirect"
export S2S_URL="http://localhost:4502"
export TEST_URL="http://localhost:4451"
export DATA_STORE_S2S_AUTHORISED_SERVICES="ccd_gw,fpl_case_service,ccd_data,ccd_ps"
export DEFINITION_STORE_S2S_AUTHORISED_SERVICES="ccd_data,ccd_gw,ccd_admin"
export USER_PROFILE_S2S_AUTHORISED_SERVICES="ccd_data,ccd_definition,ccd_admin"
export DATA_STORE_TOKEN_SECRET="iuasbcuasdcbasdgcasdgcuysachjsacyasdgjcgasdj"
export USER_PROFILE_HOST="http://ccd-user-profile-api:4451"
export IDAM_USER_URL="http://localhost:5000"
export DB_USERNAME=ccd
export DB_PASSWORD=Pa55word11
export IDAM_KEY_CCD_DEFINITION_STORE="AAAAAAAAAAAAAAAA"
export IDAM_KEY_CCD_DEFINITION_DESIGNER="AAAAAAAAAAAAAAAE"
export IDAM_KEY_CCD_DATA_STORE="AAAAAAAAAAAAAAAB"
export IDAM_KEY_CCD_ADMIN="AAAAAAAAAAAAAAAD"
export IDAM_KEY_DM_STORE="AAAAAAAAAAAAAAAA"
export IDAM_KEY_FPL_CASE_SERVICE="AABBCCDDEEFFGGHH"
export OAUTH2_CLIENT_CCD_ADMIN="IIIIIIIIIIIIIIII"
export APPINSIGHTS_INSTRUMENTATIONKEY="SomeRandomStringForLocalDocker"
export DEFINITION_STORE_DB_USE_SSL="false"
export ES_ENABLED_DOCKER="false"
export DB_USE_SSL="false"
export MY_IP=$(ifconfig tun0 | grep 'inet ' | cut -b 14-26 | awk '{print $1}')
