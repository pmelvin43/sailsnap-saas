package com.sailsnap.backend.repositories;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class S3Repository {
    private final S3Client s3Client;

    public S3Repository(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Creates the bucket for a business, to be used on their registration so we can then save.
     * Uses the name of the business since it should be a unique name for them.
     * */
    public void createBucket(String businessName) {
        String sanitizedBucketName = sanitizeBucketName(businessName);

        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(sanitizedBucketName)
                    .build();

            CreateBucketResponse createBucketResponse = s3Client.createBucket(createBucketRequest);
            log.info("Bucket {} created", createBucketResponse.location());
        } catch (S3Exception e) {
            log.error("Error creating bucket '{}': {}", businessName, e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Returns a paginated object that will have all the pages in the bucket
     * @param businessName, unique name of the business, doubles as bucket name
     * @param key, where the files are stored, should be retrieved from the db before this call can be made
     * */
    public ListObjectsV2Iterable retrieveObjects(String businessName, String key) {
        String sanitizedBucketName = sanitizeBucketName(businessName);

        try {
            log.info("Retrieving objects from S3: {}", sanitizedBucketName);
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(sanitizedBucketName)
                    .prefix(key)
                    .build();

            return s3Client.listObjectsV2Paginator(request);
        } catch (S3Exception e) {
            log.error("Error listing objects in bucket '{}' with prefix '{}'", businessName, key, e);
        }

        return null;
    }

    /**
     * Saves the file in S3 using the passed gallery name and business name.
     * @param compressedStream, the image as an input stream
     * @param galleryName, the name of the gallery that the business specified
     * @param contentType, image or video
     * @param contentLength, size of the compressedStream
     * @param businessName, the name of the business, this is also their bucket name
     * */
    public void saveFile(InputStream compressedStream, String galleryName, String contentType, long contentLength, String businessName) {
        String key = createKey(galleryName);
        String sanitizedBucketName = sanitizeBucketName(businessName);

        try {
            log.info("Saving file to S3: {}", key);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(sanitizedBucketName)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(compressedStream, contentLength));
        } catch (S3Exception e) {
            log.error("Error saving file to S3. Bucket: {}, Key: {}", businessName, key, e);
        }
    }

    /**
     * This method uses the business, gallery name. As well as the date and time to create the key for batch-saving
     * media files. For example, if lakers made a gallery called okanagan-lake, on July 5th 2025 the key would be:
     * lakers/2025/07/05/okanagan-lake. Then all the files would be stored there
     * */
    private String createKey(String galleryName) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDateTime = now.format(formatter);

        return String.format("%s/%s", formattedDateTime, galleryName);
    }

    /**
     * Sanitize bucket name to adhere to s3 conventions
     * */
    private String sanitizeBucketName(String businessName) {
        String sanitizedBucketName = businessName.toLowerCase()
                .replaceAll("[^a-z0-9.-]", "-")
                .replaceAll("-{2,}", "-");

        sanitizedBucketName = sanitizedBucketName.replaceAll("^[.-]+", "").replaceAll("[.-]+$", "");
        if (sanitizedBucketName.length() < 3) {
            sanitizedBucketName = sanitizedBucketName + "-".repeat(3 - sanitizedBucketName.length());
        } else if (sanitizedBucketName.length() > 63) {
            sanitizedBucketName = sanitizedBucketName.substring(0, 63).replaceAll("[.-]+$", "");
        }
        return sanitizedBucketName;
    }
}
