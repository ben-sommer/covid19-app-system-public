resource "aws_athena_workgroup" "this" {
  name          = "${terraform.workspace}_${var.name}"
  force_destroy = true

  configuration {
    enforce_workgroup_configuration = true

    result_configuration {
      output_location = "s3://${var.athena_output_store}"

      encryption_configuration {
        encryption_option = "SSE_S3"
      }
    }
  }
}
