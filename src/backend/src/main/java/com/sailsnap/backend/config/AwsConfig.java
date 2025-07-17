package com.sailsnap.backend.config;

import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static com.sailsnap.backend.config.Credentials.AWS_REGION;
import static com.sailsnap.backend.config.Credentials.AWS_SECRET_KEY;
import static com.sailsnap.backend.config.Credentials.AWS_ACCESS_KEY;

public class AwsConfig {

    @Bean
    public AwsCredentialsProvider staticAwsCredentialsProvider() {
        AwsBasicCredentials creds = AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        return StaticCredentialsProvider.create(creds);
    }

    @Bean
    public S3Client provideS3Client(Region region, AwsCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public Region provideRegion() {
        return Region.of(AWS_REGION);
    }
}
