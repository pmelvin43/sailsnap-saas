package com.sailsnap.backend.enums;

public enum CompressionLevel {
    LOW(2_000_000, 0.9),
    MEDIUM(1_000_000, 0.8),
    HIGH(500_000, 0.6);

    private final int videoBitrate;
    private final double imageQuality;

    CompressionLevel(int videoBitrate, double imageQuality) {
        this.videoBitrate = videoBitrate;
        this.imageQuality = imageQuality;
    }

    public int getVideoBitrate() {
        return videoBitrate;
    }

    public double getImageQuality() {
        return imageQuality;
    }
}
