package com.sailsnap.backend.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Credentials {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String AWS_ACCESS_KEY = dotenv.get("AWS_ACCESS_KEY");
    public static final String AWS_REGION = dotenv.get("AWS_REGION");
    public static final String AWS_SECRET_KEY = dotenv.get("AWS_SECRET_KEY");
}
