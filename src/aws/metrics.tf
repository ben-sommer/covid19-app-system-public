module "cloudwatch_analytics" {
  source                     = "./modules/metrics"
  env                        = terraform.workspace
  cloudfront_distribution_id = module.distribution_apis.distribution_id
  cloudfront_submission_id   = module.submission_apis.distribution_id
  cloudfront_upload_id       = module.upload_apis.distribution_id
  monitored_buckets = [
    module.diagnosis_keys_distribution_store.bucket_id,
    module.analytics_submission.store_id,
    module.diagnosis_keys_submission.store_id,
    module.analytics_processing.output_store_id
  ]
  request_triggered = [
    module.risky_venues_upload.lambda_function_name,
    module.risky_post_districts_upload.lambda_function_name,
    module.virology_submission.lambda_function_name,
    module.diagnosis_keys_submission.function,
    module.risky_venues_circuit_breaker.function,
    module.exposure_notification_circuit_breaker.function,
    module.analytics_submission.function,
    module.virology_upload.lambda_function_name,
    module.activation_keys_submission.function
  ]
  gateways = [
    module.activation_keys_submission.gateway_id,
    module.diagnosis_keys_submission.gateway_id,
    module.risky_post_districts_upload.gateway_id,
    module.virology_upload.gateway_id,
    module.analytics_submission.gateway_id,
    module.risky_venues_upload.gateway_id,
    module.virology_submission.gateway_id,
    module.risky_venues_circuit_breaker.gateway_id,
    module.exposure_notification_circuit_breaker.gateway_id
  ]

  activation_keys_submission_function            = module.activation_keys_submission.function
  analytics_submission_function                  = module.analytics_submission.function
  analytics_processing_function                  = module.analytics_processing.function
  diagnosis_keys_submission_function             = module.diagnosis_keys_submission.function
  federation_keys_processing_function            = module.federation_keys_processing.function
  exposure_notification_circuit_breaker_function = module.exposure_notification_circuit_breaker.function
  diagnosis_keys_processing_function             = module.diagnosis_keys_processing.function
  risky_post_districts_upload_function           = module.risky_post_districts_upload.lambda_function_name
  risky_venues_circuit_breaker_function          = module.risky_venues_circuit_breaker.function
  risky_venues_upload_function                   = module.risky_venues_upload.lambda_function_name
  virology_submission_function                   = module.virology_submission.lambda_function_name
  virology_upload_function                       = module.virology_upload.lambda_function_name
  advanced_analytics_function                    = module.advanced_analytics.lambda_function_name
  virology_submission_api_gateway_id             = module.virology_submission.gateway_id
  virology_upload_api_gateway_id                 = module.virology_upload.gateway_id
}
