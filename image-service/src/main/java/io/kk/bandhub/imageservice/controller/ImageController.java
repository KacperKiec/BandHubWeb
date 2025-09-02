package io.kk.bandhub.imageservice.controller;

import io.kk.bandhub.imageservice.service.ImageService;
import io.kk.bandhub.imageservice.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Map<String, String>> saveImage(@RequestParam("file") MultipartFile file, Authentication authentication) {
        var uid = SecurityUtils.getUserIdFromAuthentication(authentication);
        var result = imageService.save(file, uid.intValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("url", result));
    }
}
