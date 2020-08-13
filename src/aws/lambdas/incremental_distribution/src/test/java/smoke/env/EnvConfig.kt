package smoke.env

import com.fasterxml.jackson.annotation.JsonProperty

data class EnvConfig(
    @JsonProperty("activation_keys_submission_endpoint") val activationKeysSubmissionEndpoint: String,
    @JsonProperty("analytics_processing_function") val analytics_processing_function: String,
    @JsonProperty("analytics_processing_output_store") val analytics_processing_output_store: String,
    @JsonProperty("analytics_submission_endpoint") val analyticsSubmissionEndpoint: String,
    @JsonProperty("analytics_submission_store") val analytics_submission_store: String,
    @JsonProperty("availability_android_distribution_endpoint") val availabilityAndroidDistUrl: String,
    @JsonProperty("availability_android_distribution_store") val availability_android_distribution_store: String,
    @JsonProperty("availability_ios_distribution_endpoint") val availabilityIosDistUrl: String,
    @JsonProperty("availability_ios_distribution_store") val availability_ios_distribution_store: String,
    @JsonProperty("base_distribution_endpoint") val base_distribution_endpoint: String,
    @JsonProperty("diagnosis_keys_distribution_2hourly_endpoint") val diagnosisKeysDist2hourlyEndpoint: String,
    @JsonProperty("diagnosis_keys_distribution_daily_endpoint") val diagnosis_keys_distribution_daily_endpoint: String,
    @JsonProperty("diagnosis_keys_distribution_store") val diagnosis_keys_distribution_store: String,
    @JsonProperty("diagnosis_keys_processing_function") val diagnosisKeysProcessingFunction: String,
    @JsonProperty("diagnosis_keys_submission_endpoint") val diagnosisKeysSubmissionEndpoint: String,
    @JsonProperty("diagnosis_keys_submission_store") val diagnosis_keys_submission_store: String,
    @JsonProperty("exposure_configuration_distribution_endpoint") val exposureConfigurationDistUrl: String,
    @JsonProperty("exposure_configuration_distribution_store") val exposure_configuration_distribution_store: String,
    @JsonProperty("exposure_notification_circuit_breaker_endpoint") val exposureNotificationCircuitBreakerEndpoint: String,
    @JsonProperty("post_districts_distribution_endpoint") val postDistrictsDistUrl: String,
    @JsonProperty("post_districts_distribution_store") val post_districts_distribution_store: String,
    @JsonProperty("risky_post_districts_upload_endpoint") val riskyPostDistrictsUploadEndpoint: String,
    @JsonProperty("risky_venues_circuit_breaker_endpoint") val riskyVenuesCircuitBreakerEndpoint: String,
    @JsonProperty("risky_venues_distribution_endpoint") val riskyVenuesDistUrl: String,
    @JsonProperty("risky_venues_distribution_store") val risky_venues_distribution_store: String,
    @JsonProperty("risky_venues_upload_endpoint") val riskyVenuesUploadEndpoint: String,
    @JsonProperty("self_isolation_distribution_endpoint") val selfIsolationDistUrl: String,
    @JsonProperty("self_isolation_distribution_store") val self_isolation_distribution_store: String,
    @JsonProperty("symptomatic_questionnaire_distribution_endpoint") val symptomaticQuestionnaireDistUrl: String,
    @JsonProperty("symptomatic_questionnaire_distribution_store") val symptomatic_questionnaire_distribution_store: String,
    @JsonProperty("test_results_upload_endpoint") val testResultsUploadEndpoint: String,
    @JsonProperty("virology_kit_endpoint") val virologyKitEndpoint: String,
    @JsonProperty("virology_table_results") val virology_table_results: String,
    @JsonProperty("virology_table_submission_tokens") val virology_table_submission_tokens: String,
    @JsonProperty("virology_table_test_orders") val virology_table_test_orders: String,
    @JsonProperty("auth_headers") val authHeaders: AuthHeaders
)

data class AuthHeaders(
    @JsonProperty("mobile") val mobile: String,
    @JsonProperty("testResultUpload") val testResultUpload: String,
    @JsonProperty("highRiskVenuesCodeUpload") val highRiskVenuesCodeUpload: String,
    @JsonProperty("highRiskPostCodeUpload") val highRiskPostCodeUpload: String
)