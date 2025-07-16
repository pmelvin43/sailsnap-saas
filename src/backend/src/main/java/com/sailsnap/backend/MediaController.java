package com.sailsnap.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.domain.JpaSort.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// building a REST API that allows file uploads via POST
// saves files to a folder (/uploads)
// lists uploaded files
// lets users download / view them via browser 

// later this will link with AWS S3, database records, authentication, etc. 

@RestController
@RequestMapping("/api")
public class MediaController {

    // allows uploading a file from the frontend
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return null;
        // Save file to disk
    }

    @GetMapping("/media")
    public ResponseEntity<List<String>> listFiles() throws IOException {
        // try (Stream<Path> paths = Files.walk(Paths.get(UPLOAD_DIR), 1)) {
        //     List<String> filenames = paths
        //             .filter(Files::isRegularFile)
        //             .map(p -> p.getFileName().toString())
        //             .toList();
        //     return ResponseEntity.ok(filenames);
        // }

        return null;
    }

}
