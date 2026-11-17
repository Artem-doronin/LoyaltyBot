package com.example.LoyaltyBot.controller;


import com.example.LoyaltyBot.service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/test/s3")
public class S3TestController {

    private static final String BUCKET = "test-telegram";

    private final S3Service s3Service;

    public S3TestController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String key = file.getOriginalFilename();

        s3Service.persistObject(
                BUCKET,
                key,
                file.getBytes()
        );

        return ResponseEntity.ok(key);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(
            @RequestParam("key") String key
    ) {
        byte[] content = s3Service.readObject(BUCKET, key);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"file\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(content);
    }
}
