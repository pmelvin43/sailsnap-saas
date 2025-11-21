package com.sailsnap.backend.repositories;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

@Log4j2
@Repository
public class S3Repository {

    @Value("${aws.s3.endpoint:http://localhost:4566}")
    private String s3Endpoint;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3Repository(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    /**
     * Creates a bucket for a business and returns the actual bucket name used
     */
    public String createBucketForBusiness(String businessName) {
        String bucketName = generateBucketName(businessName);

        log.info("Creating bucket for business: '{}' -> '{}'", businessName, bucketName);

        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            CreateBucketResponse createBucketResponse = s3Client.createBucket(createBucketRequest);
            log.info("Bucket created: {} (Location: {})", bucketName, createBucketResponse.location());
            return bucketName;

        } catch (S3Exception e) {
            log.error("Failed to create bucket for business '{}': {}", businessName, e.awsErrorDetails().errorMessage(),
                    e);
            throw new RuntimeException("Failed to create bucket for business: " + businessName, e);
        }
    }

    /**
     * Saves a file to S3
     */
    public String saveFile(InputStream compressedStream, String galleryName, String contentType, long contentLength,
            String bucketName) {

        String objectKey = generateObjectKey(galleryName);

        log.info("Uploading file to S3 - Bucket: '{}', Key: '{}', Size: {} bytes",
                bucketName, objectKey, contentLength);

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(compressedStream, contentLength));

            log.info("File uploaded successfully: {}", objectKey);
            return objectKey;

        } catch (S3Exception e) {
            log.error("Failed to upload file to {}/{}: {}", bucketName, objectKey, e.awsErrorDetails().errorMessage(),
                    e);
            throw new RuntimeException("Failed to save file to S3", e);
        }
    }

    /**
     * Generates presigned URLs for temporary access to media files
     */
    public String generatePresignedUrl(String bucketName, String fileKey, Duration expiration) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(expiration)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();

            // Convert to configured endpoint
            if (s3Endpoint.contains("localhost")) {
                url = url.replaceFirst("http://[^/]+/", s3Endpoint + "/" + bucketName + "/");
            }

            return url;

        } catch (S3Exception e) {
            log.error("Error generating presigned URL for {}/{}", bucketName, fileKey, e);
            throw new RuntimeException("Failed to generate access URL", e);
        }
    }

    /**
     * Gets all media objects for a gallery
     */
    public List<String> getGalleryMediaUrls(String bucketName, String galleryPrefix, Duration expiration) {
        List<String> mediaUrls = new ArrayList<>();

        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(galleryPrefix)
                    .build();

            ListObjectsV2Iterable result = s3Client.listObjectsV2Paginator(request);

            result.stream()
                    .flatMap(response -> response.contents().stream())
                    .forEach(s3Object -> {
                        String presignedUrl = generatePresignedUrl(bucketName, s3Object.key(), expiration);
                        mediaUrls.add(presignedUrl);
                    });

            return mediaUrls;

        } catch (S3Exception e) {
            log.error("Failed to retrieve gallery media from bucket '{}'", bucketName, e);
            throw new RuntimeException("Failed to retrieve gallery media", e);
        }
    }

    /**
     * Lists objects in a bucket (legacy method - consider updating to use
     * bucketName directly)
     */
    public Optional<ListObjectsV2Iterable> retrieveObjects(String businessName, String key) {
        String bucketName = generateBucketName(businessName);

        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(key)
                    .build();

            return Optional.of(s3Client.listObjectsV2Paginator(request));

        } catch (S3Exception e) {
            log.error("Failed to list objects in bucket '{}' with prefix '{}'", businessName, key, e);
            return Optional.empty();
        }
    }

    /**
     * Generates a valid S3 bucket name from a business name
     */
    private String generateBucketName(String businessName) {
        String sanitizedName = businessName.toLowerCase()
                .replaceAll("[^a-z0-9.-]", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^[.-]+", "")
                .replaceAll("[.-]+$", "");

        // Ensure minimum length
        if (sanitizedName.length() < 3) {
            sanitizedName = sanitizedName + "-biz";
            sanitizedName = sanitizedName.replaceAll("[.-]+$", "");
        }

        // Use short UUID for uniqueness
        String shortUuid = UUID.randomUUID().toString().substring(0, 8);
        String bucketName = sanitizedName + "-" + shortUuid;

        // Ensure total length doesn't exceed 63 characters
        if (bucketName.length() > 63) {
            int maxNameLength = 63 - shortUuid.length() - 1;
            sanitizedName = sanitizedName.substring(0, maxNameLength)
                    .replaceAll("[.-]+$", "");
            bucketName = sanitizedName + "-" + shortUuid;
        }

        return bucketName;
    }

    /**
     * Generates an object key with date-based folder structure
     */
    private String generateObjectKey(String galleryName) {
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = UUID.randomUUID().toString();

        return String.format("%s/%s/%s", datePath, galleryName, fileName);
    }
}