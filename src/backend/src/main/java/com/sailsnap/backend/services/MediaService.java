package com.sailsnap.backend.services;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sailsnap.backend.entities.Media;
import com.sailsnap.backend.enums.FileType;
import com.sailsnap.backend.enums.CompressionLevel;
import com.sailsnap.backend.repositories.MediaRepository;
import com.sailsnap.backend.repositories.S3Repository;

import lombok.extern.log4j.Log4j2;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Value;

@Service
@Log4j2
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private S3Repository s3Repository;

    @Value("${ffmpeg.path:/usr/bin/ffmpeg}")
    private String ffmpegPath;

    @Value("${ffprobe.path:/usr/bin/ffprobe}")
    private String ffprobePath;

    public Media uploadMedia(MultipartFile file, Long businessId, Long galleryId, String businessName,
            CompressionLevel compressionLevel) {

        String contentType = file.getContentType();
        long originalSize = file.getSize();
        FileType fileType = (contentType != null && contentType.startsWith("video")) ? FileType.VIDEO : FileType.PHOTO;

        int videoBitrate = compressionLevel.getVideoBitrate();
        double imageQuality = compressionLevel.getImageQuality();

        InputStream inputStream = null;
        long finalSize;

        File inputTemp = null;
        File outputTemp = null;

        try {
            if (fileType == FileType.PHOTO) {
                byte[] compressedBytes = compressImage(file, imageQuality);
                finalSize = compressedBytes.length;
                inputStream = new ByteArrayInputStream(compressedBytes);
            } else {
                // Compress video safely
                log.info("Compressing video file before upload...");

                inputTemp = File.createTempFile("input-", ".mp4");
                outputTemp = File.createTempFile("compressed-", ".mp4");

                try {
                    file.transferTo(inputTemp);
                    compressVideo(inputTemp, outputTemp, videoBitrate);

                    finalSize = outputTemp.length();
                    inputStream = new FileInputStream(outputTemp);

                } catch (Exception e) {
                    log.error("Video compression failed, uploading original file.", e);
                    inputStream = file.getInputStream();
                    finalSize = originalSize;
                }
            }

            // Upload to S3
            String s3Key = s3Repository.saveFile(
                    inputStream,
                    "gallery-" + galleryId,
                    contentType,
                    finalSize,
                    businessName);

            // Create DB record
            Media media = new Media();
            media.setBusinessId(businessId);
            media.setGalleryId(galleryId);
            media.setFileKey(s3Key);
            media.setFileType(fileType);
            media.setUploadedAt(LocalDateTime.now());
            media.setFileSize(finalSize);
            media.setIsWatermarked(false);

            return mediaRepository.save(media);

        } catch (IOException e) {
            log.error("Error uploading media for business {}: {}", businessId, e.getMessage(), e);
            throw new RuntimeException("Failed to upload media", e);

        } finally {
            // Cleanup
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }

            if (inputTemp != null && inputTemp.exists())
                inputTemp.delete();
            if (outputTemp != null && outputTemp.exists())
                outputTemp.delete();
        }
    }

    private byte[] compressImage(MultipartFile file, double quality) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .scale(1.0)
                    .outputQuality(quality)
                    .toOutputStream(baos);
            return baos.toByteArray();
        }
    }

    private void compressVideo(File input, File output, int bitrate) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(Objects.requireNonNull(ffmpegPath, "ffmpeg.path must be set"));
        FFprobe ffprobe = new FFprobe(Objects.requireNonNull(ffprobePath, "ffprobe.path must be set"));

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input.getAbsolutePath())
                .overrideOutputFiles(true)
                .addOutput(output.getAbsolutePath())
                .setFormat("mp4")
                .setVideoCodec("libx264")
                .setVideoBitRate(bitrate)
                .setAudioCodec("aac")
                .setAudioBitRate(128_000)
                .done();

        new FFmpegExecutor(ffmpeg, ffprobe)
                .createJob(builder)
                .run();
    }

    public List<Media> listMedia(int galleryId) {
        return mediaRepository.findByGalleryId(galleryId);
    }

    public boolean deleteMedia(int id) {
        mediaRepository.deleteById(id);
        return true;
    }
}