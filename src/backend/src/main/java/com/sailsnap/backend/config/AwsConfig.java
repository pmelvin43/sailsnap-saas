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
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class AwsConfig {

    @Value("${AWS_REGION:us-west-1}")
    private String awsRegion;

    @Value("${AWS_ACCESS_KEY:test}")
    private String awsAccessKey;

    @Value("${AWS_SECRET_KEY:test}")
    private String awsSecretKey;

    @Value("${AWS_ENDPOINT:}")  // ← ADD THIS
    private String awsEndpoint;

    @Bean
    public AwsCredentialsProvider staticAwsCredentialsProvider() {
        // For LocalStack, always use test credentials
        if (isLocalStack()) {
            return StaticCredentialsProvider.create(
                AwsBasicCredentials.create("test", "test")
            );
        }
        
        // Check if credentials are provided (not empty)
        if (awsAccessKey == null || awsAccessKey.isEmpty() ||
                awsSecretKey == null || awsSecretKey.isEmpty()) {
            return software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider.create();
        }

        AwsBasicCredentials creds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
        return StaticCredentialsProvider.create(creds);
    }

    @Bean
    public S3Client provideS3Client(Region region, AwsCredentialsProvider credentialsProvider) {
        S3ClientBuilder builder = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider);

        // If using LocalStack, override endpoint and enable path style
        if (isLocalStack()) {
            builder.endpointOverride(URI.create(awsEndpoint))
                   .serviceConfiguration(S3Configuration.builder()
                       .pathStyleAccessEnabled(true)
                       .build());
        }

        return builder.build();
    }

    @Bean
    public Region provideRegion() {
        return Region.of(awsRegion);
    }

    private boolean isLocalStack() {
        return awsEndpoint != null && !awsEndpoint.isEmpty();
    }
}