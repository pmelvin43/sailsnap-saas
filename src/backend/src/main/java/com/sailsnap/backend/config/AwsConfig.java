package com.sailsnap.backend.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class AwsConfig {

    @Value("${AWS_REGION:us-west-1}") // Default to us-west-1 if not set
    private String awsRegion;

    @Value("${AWS_ACCESS_KEY:}") // Empty default if not set
    private String awsAccessKey;

    @Value("${AWS_SECRET_KEY:}") // Empty default if not set
    private String awsSecretKey;

    @Bean
    public AwsCredentialsProvider staticAwsCredentialsProvider() {
        // Check if credentials are provided (not empty)
        if (awsAccessKey == null || awsAccessKey.isEmpty() ||
                awsSecretKey == null || awsSecretKey.isEmpty()) {
            // If no credentials provided, use default credential chain
            // This will work with IAM roles in production
            return software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider.create();
        }

        AwsBasicCredentials creds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
        return StaticCredentialsProvider.create(creds);
    }

    @Bean
    public S3Client provideS3Client(Region region, AwsCredentialsProvider credentialsProvider) {
        // If using LocalStack, override endpoint
        if ("localstack".equals(System.getProperty("env"))) {
            return S3Client.builder()
                    .endpointOverride(URI.create("http://localhost:4566")) // localstack endpoint
                    .region(region)
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("test", "test")))
                    .serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(true) // required for socalStack
                            .build())
                    .build();
        }

        // Normal AWS S3
        return S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public Region provideRegion() {
        return Region.of(awsRegion);
    }
}