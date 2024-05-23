package com.example.helpdesk;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HelpdeskLogger {

    public static void logFile(MultipartFile file) throws IOException {
        String directoryPath = "/tmp/";

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }
        File filePath = new File(directory, Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(filePath);
    }

}
